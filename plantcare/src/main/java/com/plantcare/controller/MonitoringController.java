package com.plantcare.controller;

import com.plantcare.service.MonitoringService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * CONTROLLER — MonitoringController
 * Serves Phase 6 simulated telemetry dashboard and controls.
 */
@Controller
@RequestMapping("/monitoring")
public class MonitoringController {

    private final MonitoringService monitoringService;

    public MonitoringController(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    @GetMapping
    public String showMonitoring(Model model) {
        model.addAttribute("telemetryList", monitoringService.getTelemetryList());
        model.addAttribute("avgMoisture", monitoringService.getAverageMoisture());
        model.addAttribute("avgLight", monitoringService.getAverageLight());
        model.addAttribute("avgTemp", monitoringService.getAverageTemperature());
        model.addAttribute("alertCount", monitoringService.getAlertCount());
        return "monitoring/dashboard";
    }

    @PostMapping("/refresh")
    public String refreshReadings(RedirectAttributes ra) {
        monitoringService.refreshSimulatedReadings();
        ra.addFlashAttribute("success", "🔄 Fresh simulated sensor readings generated successfully!");
        return "redirect:/monitoring";
    }
}
