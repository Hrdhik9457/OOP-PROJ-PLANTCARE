package com.plantcare.service;

import com.plantcare.exception.InvalidPlantDataException;
import com.plantcare.model.IndoorPlant;
import com.plantcare.model.IndoorPlant.LightRequirement;
import com.plantcare.model.OutdoorPlant;
import com.plantcare.model.OutdoorPlant.SunExposure;
import com.plantcare.model.Plant;
import com.plantcare.model.PlantForm;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * SERVICE — PlantService
 *
 * OOP Concepts demonstrated:
 *   - Collections    : plants stored in an ArrayList<Plant> (in-memory)
 *   - Polymorphism   : the list holds both IndoorPlant and OutdoorPlant objects
 *   - Encapsulation  : list is private; external code uses service methods
 *   - Exception Handling : catches and re-throws InvalidPlantDataException
 *
 * Data resets on application restart (no database — by design for this project).
 */
@Service
public class PlantService {

    // COLLECTION: polymorphic list — holds IndoorPlant and OutdoorPlant objects
    private final List<Plant> plants = new ArrayList<>();
    private int nextId = 1;

    // ── Constructor: seed sample data ──────────────────────────────────────
    public PlantService() {
        seedSamplePlants();
    }

    // ── CRUD operations ────────────────────────────────────────────────────

    public List<Plant> getAllPlants() {
        return new ArrayList<>(plants); // defensive copy
    }

    public Optional<Plant> findById(int id) {
        return plants.stream().filter(p -> p.getId() == id).findFirst();
    }

    public Plant getById(int id) {
        return findById(id)
                .orElseThrow(() -> new InvalidPlantDataException("id",
                        "No plant found with id " + id));
    }

    public Plant addPlant(Plant plant) {
        plant.setId(nextId++);
        plants.add(plant);
        return plant;
    }

    public Plant updatePlant(int id, Plant updated) {
        Plant existing = getById(id);
        existing.setName(updated.getName());
        existing.setSpecies(updated.getSpecies());
        existing.setDescription(updated.getDescription());
        existing.setWateringFrequencyDays(updated.getWateringFrequencyDays());
        existing.setSoilMoisture(updated.getSoilMoisture());
        existing.setLightLevel(updated.getLightLevel());
        existing.setTemperature(updated.getTemperature());
        return existing;
    }

    public boolean deletePlant(int id) {
        return plants.removeIf(p -> p.getId() == id);
    }

    /** Creates a Plant subclass from form data and stores it. */
    public Plant createFromForm(PlantForm form) {
        Plant plant;
        if ("OUTDOOR".equalsIgnoreCase(form.getPlantType())) {
            plant = new OutdoorPlant(nextId++,
                    form.getName(), form.getSpecies(), form.getDescription(), form.getEmoji(),
                    form.getWateringFrequencyDays(), form.getSoilMoisture(),
                    form.getLightLevel(), form.getTemperature(),
                    OutdoorPlant.SunExposure.valueOf(form.getSunExposure()),
                    form.isEdible(), form.getSeason());
        } else {
            plant = new IndoorPlant(nextId++,
                    form.getName(), form.getSpecies(), form.getDescription(), form.getEmoji(),
                    form.getWateringFrequencyDays(), form.getSoilMoisture(),
                    form.getLightLevel(), form.getTemperature(),
                    IndoorPlant.LightRequirement.valueOf(form.getLightRequirement()),
                    form.isAirPurifying(), form.getIdealRoom());
        }
        plants.add(plant);
        return plant;
    }

    /** Updates an existing plant's fields from form data. Plant type cannot change on edit. */
    public void updateFromForm(int id, PlantForm form) {
        Plant existing = getById(id);
        existing.setName(form.getName());
        existing.setSpecies(form.getSpecies());
        existing.setDescription(form.getDescription());
        existing.setEmoji(form.getEmoji());
        existing.setWateringFrequencyDays(form.getWateringFrequencyDays());
        existing.setSoilMoisture(form.getSoilMoisture());
        existing.setLightLevel(form.getLightLevel());
        existing.setTemperature(form.getTemperature());
        if (existing instanceof IndoorPlant ip) {
            ip.setLightRequirement(IndoorPlant.LightRequirement.valueOf(form.getLightRequirement()));
            ip.setAirPurifying(form.isAirPurifying());
            ip.setIdealRoom(form.getIdealRoom());
        } else if (existing instanceof OutdoorPlant op) {
            op.setSunExposure(OutdoorPlant.SunExposure.valueOf(form.getSunExposure()));
            op.setEdible(form.isEdible());
            op.setSeason(form.getSeason());
        }
    }

    /** Converts an existing Plant into a pre-filled PlantForm for the edit page. */
    public PlantForm toForm(Plant plant) {
        PlantForm form = new PlantForm();
        form.setName(plant.getName());
        form.setSpecies(plant.getSpecies());
        form.setDescription(plant.getDescription());
        form.setEmoji(plant.getEmoji());
        form.setWateringFrequencyDays(plant.getWateringFrequencyDays());
        form.setSoilMoisture(plant.getSoilMoisture());
        form.setLightLevel(plant.getLightLevel());
        form.setTemperature(plant.getTemperature());
        if (plant instanceof IndoorPlant ip) {
            form.setPlantType("INDOOR");
            form.setLightRequirement(ip.getLightRequirement().name());
            form.setAirPurifying(ip.isAirPurifying());
            form.setIdealRoom(ip.getIdealRoom());
        } else if (plant instanceof OutdoorPlant op) {
            form.setPlantType("OUTDOOR");
            form.setSunExposure(op.getSunExposure().name());
            form.setEdible(op.isEdible());
            form.setSeason(op.getSeason());
        }
        return form;
    }

    /** Records watering for a plant by id. */
    public void waterPlant(int id) {
        getById(id).water();
    }

    // ── Query helpers used by dashboard / UI ──────────────────────────────

    public List<Plant> getPlantsNeedingWater() {
        return plants.stream().filter(Plant::needsWatering).toList();
    }

    public long countIndoor()  { return plants.stream().filter(p -> p instanceof IndoorPlant).count(); }
    public long countOutdoor() { return plants.stream().filter(p -> p instanceof OutdoorPlant).count(); }

    public long countByHealth(String status) {
        return plants.stream().filter(p -> p.getHealthStatus().equals(status)).count();
    }

    // ── Sample data ────────────────────────────────────────────────────────

    private void seedSamplePlants() {

        // — Indoor Plants —

        IndoorPlant monstera = new IndoorPlant(
                nextId++,
                "Monstera",
                "Monstera deliciosa",
                "The iconic Swiss Cheese Plant. Known for its large, glossy, split leaves. Easy to care for and makes a dramatic statement indoors.",
                "🌿", 7, 65.0, 60.0, 22.0,
                LightRequirement.MEDIUM, true, "Living Room");
        monstera.setLastWateredDate(LocalDate.now().minusDays(3));
        plants.add(monstera);

        IndoorPlant peaceLily = new IndoorPlant(
                nextId++,
                "Peace Lily",
                "Spathiphyllum wallisii",
                "Elegant white blooms and glossy dark leaves. One of the best air-purifying plants, ideal for low-light spaces.",
                "🌸", 5, 55.0, 35.0, 21.0,
                LightRequirement.LOW, true, "Bedroom");
        peaceLily.setLastWateredDate(LocalDate.now().minusDays(6));
        plants.add(peaceLily);

        IndoorPlant snakePlant = new IndoorPlant(
                nextId++,
                "Snake Plant",
                "Sansevieria trifasciata",
                "Nearly indestructible! Tolerates low light and infrequent watering. Perfect for beginners and busy plant owners.",
                "🪴", 14, 30.0, 45.0, 23.0,
                LightRequirement.LOW, true, "Office");
        snakePlant.setLastWateredDate(LocalDate.now().minusDays(10));
        plants.add(snakePlant);

        IndoorPlant pothos = new IndoorPlant(
                nextId++,
                "Golden Pothos",
                "Epipremnum aureum",
                "Fast-growing trailing vine with heart-shaped golden-green leaves. Thrives in almost any indoor condition.",
                "🍃", 7, 50.0, 55.0, 24.0,
                LightRequirement.MEDIUM, true, "Kitchen");
        pothos.setLastWateredDate(LocalDate.now().minusDays(2));
        plants.add(pothos);

        IndoorPlant fiddle = new IndoorPlant(
                nextId++,
                "Fiddle Leaf Fig",
                "Ficus lyrata",
                "Large, wavy leaves that create a bold focal point. Needs consistent bright light and dislikes being moved.",
                "🌳", 7, 45.0, 80.0, 22.0,
                LightRequirement.HIGH, false, "Living Room");
        fiddle.setLastWateredDate(LocalDate.now().minusDays(5));
        plants.add(fiddle);

        // — Outdoor Plants —

        OutdoorPlant lavender = new OutdoorPlant(
                nextId++,
                "Lavender",
                "Lavandula angustifolia",
                "Fragrant purple flower spikes that attract bees and butterflies. Drought-tolerant once established. Great for borders.",
                "💜", 10, 25.0, 90.0, 26.0,
                SunExposure.FULL_SUN, false, "Summer");
        lavender.setLastWateredDate(LocalDate.now().minusDays(8));
        plants.add(lavender);

        OutdoorPlant tomatoPlant = new OutdoorPlant(
                nextId++,
                "Cherry Tomato",
                "Solanum lycopersicum var. cerasiforme",
                "Compact tomato variety producing sweet, bite-sized fruits. Grows well in pots or garden beds with full sun.",
                "🍅", 2, 70.0, 85.0, 28.0,
                SunExposure.FULL_SUN, true, "Spring–Autumn");
        tomatoPlant.setLastWateredDate(LocalDate.now().minusDays(1));
        plants.add(tomatoPlant);

        OutdoorPlant fern = new OutdoorPlant(
                nextId++,
                "Garden Fern",
                "Dryopteris filix-mas",
                "Lush, feathery fronds that bring texture to shaded garden corners. Loves moist, rich soil.",
                "🌿", 3, 75.0, 20.0, 18.0,
                SunExposure.FULL_SHADE, false, "Year-round");
        fern.setLastWateredDate(LocalDate.now().minusDays(2));
        plants.add(fern);
    }
}
