package com.plantcare.controller;

import com.plantcare.exception.InvalidPlantDataException;
import com.plantcare.model.Plant;
import com.plantcare.model.PlantForm;
import com.plantcare.service.PlantService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * CONTROLLER — PlantController
 * Handles all HTTP routes for plant management (Phase 3 & 4).
 * Demonstrates Spring MVC: GET for display, POST for mutations.
 */
@Controller
@RequestMapping("/plants")
public class PlantController {

    private final PlantService plantService;

    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    // ── Phase 3: Dashboard — list all plants ──────────────────────────────

    @GetMapping
    public String listPlants(Model model) {
        model.addAttribute("plants",       plantService.getAllPlants());
        model.addAttribute("needingWater", plantService.getPlantsNeedingWater().size());
        model.addAttribute("totalIndoor",  plantService.countIndoor());
        model.addAttribute("totalOutdoor", plantService.countOutdoor());
        return "plants/list";
    }

    // ── Plant detail page ─────────────────────────────────────────────────

    @GetMapping("/{id}")
    public String plantDetail(@PathVariable int id, Model model,
                              RedirectAttributes ra) {
        try {
            Plant plant = plantService.getById(id);
            model.addAttribute("plant", plant);
            return "plants/detail";
        } catch (InvalidPlantDataException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/plants";
        }
    }

    // ── Phase 4: Add plant — show form ────────────────────────────────────

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("plantForm", new PlantForm());
        model.addAttribute("editMode", false);
        return "plants/form";
    }

    // ── Add plant — process form ──────────────────────────────────────────

    @PostMapping("/add")
    public String addPlant(@Valid @ModelAttribute("plantForm") PlantForm form,
                           BindingResult result, Model model,
                           RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("editMode", false);
            return "plants/form";
        }
        try {
            Plant created = plantService.createFromForm(form);
            ra.addFlashAttribute("success", "🌱 " + created.getName() + " added successfully!");
            return "redirect:/plants";
        } catch (InvalidPlantDataException e) {
            model.addAttribute("editMode", false);
            model.addAttribute("error", e.getMessage());
            return "plants/form";
        }
    }

    // ── Edit plant — show pre-filled form ────────────────────────────────

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model,
                               RedirectAttributes ra) {
        try {
            Plant plant = plantService.getById(id);
            model.addAttribute("plantForm", plantService.toForm(plant));
            model.addAttribute("plantId",  id);
            model.addAttribute("editMode", true);
            return "plants/form";
        } catch (InvalidPlantDataException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/plants";
        }
    }

    // ── Edit plant — process update ───────────────────────────────────────

    @PostMapping("/edit/{id}")
    public String updatePlant(@PathVariable int id,
                              @Valid @ModelAttribute("plantForm") PlantForm form,
                              BindingResult result, Model model,
                              RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("plantId",  id);
            model.addAttribute("editMode", true);
            return "plants/form";
        }
        try {
            plantService.updateFromForm(id, form);
            ra.addFlashAttribute("success", "✅ Plant updated successfully!");
            return "redirect:/plants/" + id;
        } catch (InvalidPlantDataException e) {
            model.addAttribute("plantId",  id);
            model.addAttribute("editMode", true);
            model.addAttribute("error", e.getMessage());
            return "plants/form";
        }
    }

    // ── Delete plant ──────────────────────────────────────────────────────

    @PostMapping("/delete/{id}")
    public String deletePlant(@PathVariable int id, RedirectAttributes ra) {
        try {
            Plant plant = plantService.getById(id);
            String name = plant.getName();
            plantService.deletePlant(id);
            ra.addFlashAttribute("success", "🗑 " + name + " removed.");
        } catch (InvalidPlantDataException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/plants";
    }

    // ── Water plant (quick action) ────────────────────────────────────────

    @PostMapping("/water/{id}")
    public String waterPlant(@PathVariable int id, RedirectAttributes ra) {
        try {
            plantService.waterPlant(id);
            ra.addFlashAttribute("success", "💧 Plant watered!");
        } catch (InvalidPlantDataException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/plants/" + id;
    }
}
