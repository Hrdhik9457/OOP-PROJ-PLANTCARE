package com.plantcare.model;

import java.time.LocalDate;

/**
 * ENTITY — JournalEntry
 *
 * OOP Concepts demonstrated:
 *   - Encapsulation: Private state for tracking plant growth milestones and observations.
 *   - Association: Associates observations with a specific Plant instance.
 */
public class JournalEntry {

    private int id;
    private int plantId;
    private String plantName;
    private String plantEmoji;
    private LocalDate entryDate;
    private String title;
    private String content;
    private String growthStage; // "Seedling", "Vegetative", "Budding", "Flowering", "Mature"
    private String moodEmoji;   // "🌿", "🌸", "✨", "☀️", "💧"

    public JournalEntry() {
        this.entryDate = LocalDate.now();
        this.growthStage = "Vegetative";
        this.moodEmoji = "🌿";
    }

    public JournalEntry(int id, int plantId, String plantName, String plantEmoji,
                        LocalDate entryDate, String title, String content,
                        String growthStage, String moodEmoji) {
        this.id = id;
        this.plantId = plantId;
        this.plantName = plantName != null ? plantName : "General Garden";
        this.plantEmoji = plantEmoji != null ? plantEmoji : "🌿";
        this.entryDate = entryDate != null ? entryDate : LocalDate.now();
        this.title = title != null ? title : "Observation";
        this.content = content != null ? content : "";
        this.growthStage = growthStage != null ? growthStage : "Vegetative";
        this.moodEmoji = moodEmoji != null ? moodEmoji : "🌿";
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

    public LocalDate getEntryDate() { return entryDate; }
    public void setEntryDate(LocalDate entryDate) { this.entryDate = entryDate; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getGrowthStage() { return growthStage; }
    public void setGrowthStage(String growthStage) { this.growthStage = growthStage; }

    public String getMoodEmoji() { return moodEmoji; }
    public void setMoodEmoji(String moodEmoji) { this.moodEmoji = moodEmoji; }
}
