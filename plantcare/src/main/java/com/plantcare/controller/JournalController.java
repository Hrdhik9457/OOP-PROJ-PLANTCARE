package com.plantcare.controller;

import com.plantcare.model.JournalEntry;
import com.plantcare.service.JournalService;
import com.plantcare.service.PlantService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

/**
 * CONTROLLER — JournalController
 * Serves Phase 7 Growth Journal pages and handles journal entries.
 */
@Controller
@RequestMapping("/journal")
public class JournalController {

    private final JournalService journalService;
    private final PlantService plantService;

    public JournalController(JournalService journalService, PlantService plantService) {
        this.journalService = journalService;
        this.plantService = plantService;
    }

    @GetMapping
    public String showJournal(@RequestParam(value = "plantId", required = false) Integer plantId,
                              Model model) {
        List<JournalEntry> entries;
        if (plantId != null && plantId > 0) {
            entries = journalService.getEntriesForPlant(plantId);
            model.addAttribute("selectedPlantId", plantId);
        } else {
            entries = journalService.getAllEntries();
            model.addAttribute("selectedPlantId", 0);
        }

        model.addAttribute("entries", entries);
        model.addAttribute("plants", plantService.getAllPlants());
        model.addAttribute("todayDate", LocalDate.now());
        return "journal/index";
    }

    @PostMapping("/add")
    public String addEntry(@RequestParam("plantId") int plantId,
                           @RequestParam("title") String title,
                           @RequestParam("content") String content,
                           @RequestParam(value = "growthStage", defaultValue = "Vegetative") String growthStage,
                           @RequestParam(value = "moodEmoji", defaultValue = "🌿") String moodEmoji,
                           @RequestParam("entryDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate entryDate,
                           RedirectAttributes ra) {
        try {
            journalService.addEntry(plantId, title, content, growthStage, moodEmoji, entryDate);
            ra.addFlashAttribute("success", "📔 Growth journal entry saved successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Failed to save entry: " + e.getMessage());
        }
        return "redirect:/journal";
    }

    @PostMapping("/delete/{id}")
    public String deleteEntry(@PathVariable int id, RedirectAttributes ra) {
        journalService.deleteEntry(id);
        ra.addFlashAttribute("success", "Journal entry deleted.");
        return "redirect:/journal";
    }
}
