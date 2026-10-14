package com.plantcare.service;

import com.plantcare.model.CareTask;
import com.plantcare.model.Plant;
import com.plantcare.model.TaskType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * SERVICE — CareTaskService
 *
 * OOP Concepts demonstrated:
 *   - Collections: Manages an in-memory ArrayList of CareTask instances.
 *   - Composition & Coordination: Interacts directly with PlantService when tasks complete.
 */
@Service
public class CareTaskService {

    private final List<CareTask> tasks = new ArrayList<>();
    private final PlantService plantService;
    private int nextId = 1;

    public CareTaskService(PlantService plantService) {
        this.plantService = plantService;
        seedSampleTasks();
    }

    public List<CareTask> getAllTasks() {
        return tasks.stream()
                .sorted(Comparator.comparing(CareTask::isCompleted)
                        .thenComparing(CareTask::getDueDate))
                .toList();
    }

    public List<CareTask> getPendingTasks() {
        return tasks.stream()
                .filter(t -> !t.isCompleted())
                .sorted(Comparator.comparing(CareTask::getDueDate))
                .toList();
    }

    public List<CareTask> getOverdueTasks() {
        return tasks.stream()
                .filter(CareTask::isOverdue)
                .sorted(Comparator.comparing(CareTask::getDueDate))
                .toList();
    }

    public List<CareTask> getTodayTasks() {
        return tasks.stream()
                .filter(CareTask::isDueToday)
                .toList();
    }

    public List<CareTask> getUpcomingTasks() {
        return tasks.stream()
                .filter(CareTask::isUpcoming)
                .sorted(Comparator.comparing(CareTask::getDueDate))
                .toList();
    }

    public List<CareTask> getCompletedTasks() {
        return tasks.stream()
                .filter(CareTask::isCompleted)
                .sorted(Comparator.comparing(CareTask::getCompletedDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
    }

    public List<CareTask> getTasksForPlant(int plantId) {
        return tasks.stream()
                .filter(t -> t.getPlantId() == plantId)
                .sorted(Comparator.comparing(CareTask::getDueDate))
                .toList();
    }

    public Optional<CareTask> findById(int id) {
        return tasks.stream().filter(t -> t.getId() == id).findFirst();
    }

    public CareTask addTask(int plantId, TaskType taskType, LocalDate dueDate, String notes) {
        Plant plant = plantService.getById(plantId);
        CareTask task = new CareTask(nextId++, plant.getId(), plant.getName(), plant.getEmoji(),
                taskType, dueDate, notes);
        tasks.add(task);
        return task;
    }

    /**
     * Toggles task completion.
     * OOP Coordination: If a WATERING task is completed, it also waters the plant via PlantService!
     */
    public boolean toggleComplete(int taskId) {
        Optional<CareTask> opt = findById(taskId);
        if (opt.isPresent()) {
            CareTask task = opt.get();
            if (task.isCompleted()) {
                task.markPending();
            } else {
                task.markComplete();
                if (task.getTaskType() == TaskType.WATERING) {
                    try {
                        plantService.waterPlant(task.getPlantId());
                    } catch (Exception ignored) {}
                }
            }
            return true;
        }
        return false;
    }

    public boolean deleteTask(int taskId) {
        return tasks.removeIf(t -> t.getId() == taskId);
    }

    private void seedSampleTasks() {
        LocalDate today = LocalDate.now();

        // Overdue tasks
        tasks.add(new CareTask(nextId++, 2, "Peace Lily", "🌸",
                TaskType.WATERING, today.minusDays(1), "Soil surface is completely dry. Water thoroughly."));
        tasks.add(new CareTask(nextId++, 6, "Lavender", "💜",
                TaskType.PRUNING, today.minusDays(2), "Trim back faded summer flower spikes."));

        // Due today
        tasks.add(new CareTask(nextId++, 1, "Monstera", "🌿",
                TaskType.WATERING, today, "Check bottom tray drainage after watering."));
        tasks.add(new CareTask(nextId++, 7, "Cherry Tomato", "🍅",
                TaskType.FERTILIZING, today, "Apply organic potassium-rich liquid feed."));

        // Upcoming tasks
        tasks.add(new CareTask(nextId++, 3, "Snake Plant", "🪴",
                TaskType.WATERING, today.plusDays(4), "Light watering only; allow to dry out completely."));
        tasks.add(new CareTask(nextId++, 5, "Fiddle Leaf Fig", "🌳",
                TaskType.REPOTTING, today.plusDays(7), "Roots emerging from drainage holes. Move to larger terracotta pot."));
        tasks.add(new CareTask(nextId++, 4, "Golden Pothos", "🍃",
                TaskType.PRUNING, today.plusDays(10), "Cut back leggy vines to encourage bushier growth."));

        // Already completed
        CareTask comp = new CareTask(nextId++, 8, "Garden Fern", "🌿",
                TaskType.WATERING, today.minusDays(2), "Misted fronds and watered root ball.");
        comp.markComplete();
        comp.setCompletedDate(today.minusDays(2));
        tasks.add(comp);
    }
}
