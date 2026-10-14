package com.plantcare.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * ENTITY — CareTask
 *
 * OOP Concepts demonstrated:
 *   - Encapsulation: Private state with validated mutators and domain query methods.
 *   - Association / Composition: A CareTask is associated with a specific Plant.
 */
public class CareTask {

    private int id;
    private int plantId;
    private String plantName;
    private String plantEmoji;
    private TaskType taskType;
    private LocalDate dueDate;
    private LocalDate completedDate;
    private boolean completed;
    private String notes;

    public CareTask() {
        this.dueDate = LocalDate.now();
        this.taskType = TaskType.WATERING;
        this.notes = "";
        this.completed = false;
    }

    public CareTask(int id, int plantId, String plantName, String plantEmoji,
                    TaskType taskType, LocalDate dueDate, String notes) {
        this.id = id;
        this.plantId = plantId;
        this.plantName = plantName != null ? plantName : "Unknown Plant";
        this.plantEmoji = plantEmoji != null ? plantEmoji : "🌿";
        this.taskType = taskType != null ? taskType : TaskType.WATERING;
        this.dueDate = dueDate != null ? dueDate : LocalDate.now();
        this.notes = notes != null ? notes : "";
        this.completed = false;
    }

    // ── Domain Methods ─────────────────────────────────────────────────────

    public void markComplete() {
        this.completed = true;
        this.completedDate = LocalDate.now();
    }

    public void markPending() {
        this.completed = false;
        this.completedDate = null;
    }

    public boolean isOverdue() {
        return !completed && dueDate != null && dueDate.isBefore(LocalDate.now());
    }

    public boolean isDueToday() {
        return !completed && dueDate != null && dueDate.isEqual(LocalDate.now());
    }

    public boolean isUpcoming() {
        return !completed && dueDate != null && dueDate.isAfter(LocalDate.now());
    }

    public long getDaysUntilDue() {
        if (dueDate == null) return 0;
        return ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
    }

    public String getStatusLabel() {
        if (completed) return "Completed";
        if (isOverdue()) return "Overdue (" + Math.abs(getDaysUntilDue()) + "d)";
        if (isDueToday()) return "Due Today";
        return "In " + getDaysUntilDue() + " day(s)";
    }

    public String getStatusBadgeClass() {
        if (completed) return "badge-green";
        if (isOverdue()) return "badge-red";
        if (isDueToday()) return "badge-yellow";
        return "badge-blue";
    }

    // ── Getters and Setters ────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPlantId() { return plantId; }
    public void setPlantId(int plantId) { this.plantId = plantId; }

    public String getPlantName() { return plantName; }
    public void setPlantName(String plantName) { this.plantName = plantName; }

    public String getPlantEmoji() { return plantEmoji; }
    public void setPlantEmoji(String plantEmoji) { this.plantEmoji = plantEmoji; }

    public TaskType getTaskType() { return taskType; }
    public void setTaskType(TaskType taskType) { this.taskType = taskType; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getCompletedDate() { return completedDate; }
    public void setCompletedDate(LocalDate completedDate) { this.completedDate = completedDate; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
