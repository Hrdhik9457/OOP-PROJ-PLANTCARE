package com.plantcare.service;

import com.plantcare.model.JournalEntry;
import com.plantcare.model.Plant;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * SERVICE — JournalService
 *
 * OOP Concept: Collections & Domain Service
 * Stores growth observations in a chronological in-memory list.
 */
@Service
public class JournalService {

    private final List<JournalEntry> entries = new ArrayList<>();
    private final PlantService plantService;
    private int nextId = 1;

    public JournalService(PlantService plantService) {
        this.plantService = plantService;
        seedSampleEntries();
    }

    public List<JournalEntry> getAllEntries() {
        return entries.stream()
                .sorted(Comparator.comparing(JournalEntry::getEntryDate).reversed()
                        .thenComparing(Comparator.comparing(JournalEntry::getId).reversed()))
                .toList();
    }

    public List<JournalEntry> getEntriesForPlant(int plantId) {
        return entries.stream()
                .filter(e -> e.getPlantId() == plantId)
                .sorted(Comparator.comparing(JournalEntry::getEntryDate).reversed())
                .toList();
    }

    public JournalEntry addEntry(int plantId, String title, String content,
                                 String growthStage, String moodEmoji, LocalDate entryDate) {
        Plant plant = plantService.getById(plantId);
        JournalEntry entry = new JournalEntry(
                nextId++,
                plant.getId(),
                plant.getName(),
                plant.getEmoji(),
                entryDate != null ? entryDate : LocalDate.now(),
                title,
                content,
                growthStage,
                moodEmoji
        );
        entries.add(entry);
        return entry;
    }

    public boolean deleteEntry(int id) {
        return entries.removeIf(e -> e.getId() == id);
    }

    private void seedSampleEntries() {
        LocalDate today = LocalDate.now();

        entries.add(new JournalEntry(nextId++, 1, "Monstera", "🌿",
                today,
                "New Split Leaf Unfurling!",
                "Noticed a beautiful new leaf emerging with three distinct fenestrations (splits). The leaf is a light vibrant green and very tender.",
                "Mature", "✨"));

        entries.add(new JournalEntry(nextId++, 7, "Cherry Tomato", "🍅",
                today.minusDays(2),
                "First Yellow Blossoms",
                "Clusters of small bright yellow flowers have opened up across the upper branches. Pollinators were active this morning.",
                "Flowering", "🌸"));

        entries.add(new JournalEntry(nextId++, 2, "Peace Lily", "🌸",
                today.minusDays(4),
                "White Spathe Emerged",
                "A fresh white sail-shaped spathe has risen above the deep green foliage after repotting into fresh nutrient-rich compost.",
                "Flowering", "✨"));

        entries.add(new JournalEntry(nextId++, 4, "Golden Pothos", "🍃",
                today.minusDays(7),
                "Stem Propagation Growing Roots",
                "The water cuttings placed on the kitchen window shelf have developed 2cm healthy white water roots. Ready for potting soon.",
                "Vegetative", "🌿"));

        entries.add(new JournalEntry(nextId++, 3, "Snake Plant", "🪴",
                today.minusDays(12),
                "New Pup Detected",
                "Spotted a brand new shoot pup pushing up from the rhizome near the edge of the pot.",
                "Vegetative", "☀️"));
    }
}
