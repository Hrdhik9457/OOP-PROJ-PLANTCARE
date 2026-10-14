package com.plantcare.model;

/**
 * ENUM — TaskType
 *
 * OOP Concept: Typesafe Enumeration
 * Represents distinct types of plant care actions.
 */
public enum TaskType {
    WATERING("Watering", "💧"),
    FERTILIZING("Fertilizing", "🧪"),
    REPOTTING("Repotting", "🪴"),
    PRUNING("Pruning", "✂️");

    private final String displayName;
    private final String emoji;

    TaskType(String displayName, String emoji) {
        this.displayName = displayName;
        this.emoji = emoji;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmoji() {
        return emoji;
    }
}
