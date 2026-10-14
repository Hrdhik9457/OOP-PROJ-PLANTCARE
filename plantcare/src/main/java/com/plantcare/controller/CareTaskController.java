package com.plantcare.controller;

import com.plantcare.model.CareTask;
import com.plantcare.model.TaskType;
import com.plantcare.service.CareTaskService;
import com.plantcare.service.PlantService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

/**
 * CONTROLLER — CareTaskController
 * Handles care calendar, task schedules, and task status updates.
 */
@Controller
@RequestMapping("/tasks")
public class CareTaskController {

    private final CareTaskService taskService;
    private final PlantService plantService;

    public CareTaskController(CareTaskService taskService, PlantService plantService) {
        this.taskService = taskService;
        this.plantService = plantService;
    }

    @GetMapping
    public String listTasks(@RequestParam(value = "filter", defaultValue = "all") String filter,
                            Model model) {
        List<CareTask> displayTasks = switch (filter) {
            case "today" -> taskService.getTodayTasks();
            case "overdue" -> taskService.getOverdueTasks();
            case "upcoming" -> taskService.getUpcomingTasks();
            case "completed" -> taskService.getCompletedTasks();
            default -> taskService.getAllTasks();
        };

        model.addAttribute("tasks", displayTasks);
        model.addAttribute("currentFilter", filter);
        model.addAttribute("overdueCount", taskService.getOverdueTasks().size());
        model.addAttribute("todayCount", taskService.getTodayTasks().size());
        model.addAttribute("upcomingCount", taskService.getUpcomingTasks().size());
        model.addAttribute("completedCount", taskService.getCompletedTasks().size());
        model.addAttribute("totalPending", taskService.getPendingTasks().size());
        model.addAttribute("plants", plantService.getAllPlants());
        model.addAttribute("taskTypes", TaskType.values());
        model.addAttribute("todayDate", LocalDate.now());

        return "tasks/list";
    }

    @PostMapping("/toggle/{id}")
    public String toggleTask(@PathVariable int id, RedirectAttributes ra) {
        boolean ok = taskService.toggleComplete(id);
        if (ok) {
            ra.addFlashAttribute("success", "Task status updated!");
        } else {
            ra.addFlashAttribute("error", "Task not found.");
        }
        return "redirect:/tasks";
    }

    @PostMapping("/add")
    public String addTask(@RequestParam("plantId") int plantId,
                          @RequestParam("taskType") TaskType taskType,
                          @RequestParam("dueDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate,
                          @RequestParam(value = "notes", required = false) String notes,
                          RedirectAttributes ra) {
        try {
            taskService.addTask(plantId, taskType, dueDate, notes);
            ra.addFlashAttribute("success", "🌱 New care task scheduled successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Failed to add task: " + e.getMessage());
        }
        return "redirect:/tasks";
    }

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable int id, RedirectAttributes ra) {
        taskService.deleteTask(id);
        ra.addFlashAttribute("success", "Task removed.");
        return "redirect:/tasks";
    }
}
