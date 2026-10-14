package com.plantcare.controller;

import com.plantcare.service.CareTaskService;
import com.plantcare.service.PlantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final PlantService plantService;
    private final CareTaskService taskService;

    public HomeController(PlantService plantService, CareTaskService taskService) {
        this.plantService = plantService;
        this.taskService = taskService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("totalPlants", plantService.getAllPlants().size());
        model.addAttribute("needsWater", plantService.getPlantsNeedingWater().size());
        model.addAttribute("todayTasks", taskService.getTodayTasks().size());
        model.addAttribute("totalPendingTasks", taskService.getPendingTasks().size());
        return "index";
    }

    @GetMapping("/offline")
    public String offline() {
        return "offline";
    }
}
