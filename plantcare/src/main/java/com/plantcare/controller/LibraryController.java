package com.plantcare.controller;

import com.plantcare.service.PlantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * CONTROLLER — LibraryController
 * Serves the Plant Species Reference Library.
 */
@Controller
public class LibraryController {

    private final PlantService plantService;

    public LibraryController(PlantService plantService) {
        this.plantService = plantService;
    }

    @GetMapping("/library")
    public String showLibrary(Model model) {
        model.addAttribute("plants", plantService.getAllPlants());
        return "library/index";
    }
}
