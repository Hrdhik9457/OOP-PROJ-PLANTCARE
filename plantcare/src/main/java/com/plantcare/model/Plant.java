package com.plantcare.model;

import com.plantcare.exception.InvalidPlantDataException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * ABSTRACT CLASS — Plant
 *
 * OOP Concepts demonstrated:
 *   - Abstraction   : abstract methods force subclasses to provide implementations
 *   - Encapsulation : all fields are private; accessed only via validated getters/setters
 *   - Inheritance   : IndoorPlant and OutdoorPlant extend this class
 *   - Interfaces    : implements Waterable and Monitorable
 */
public abstract class Plant implements Waterable, Monitorable {

    // ── Encapsulation: private fields ──────────────────────────────────────
    private int    id;
    private String name;
    private String species;
    private String description;
    private String emoji;

    private LocalDate lastWateredDate;
    private int       wateringFrequencyDays;

    // Simulated sensor readings (0–100 scale)
    private double soilMoisture;   // percentage
    private double lightLevel;     // percentage
    private double temperature;    // °C

    // ── Constructor ────────────────────────────────────────────────────────
    protected Plant(int id, String name, String species, String description,
                    String emoji, int wateringFrequencyDays,
                    double soilMoisture, double lightLevel, double temperature) {
        this.id          = id;
        setName(name);
        setSpecies(species);
        this.description = description != null ? description : "";
        this.emoji       = emoji != null ? emoji : "🌿";
        setWateringFrequencyDays(wateringFrequencyDays);
        setSoilMoisture(soilMoisture);
        setLightLevel(lightLevel);
        setTemperature(temperature);
        this.lastWateredDate = LocalDate.now().minusDays(1); // default: watered yesterday
    }

    // ── Abstract Methods (Abstraction + Polymorphism) ──────────────────────

    /**
     * ABSTRACT — subclasses must override this.
     * Returns care advice specific to the plant type (indoor vs outdoor).
     * Demonstrates polymorphism: same method call, different behaviour.
     */
    public abstract String getCareRecommendation();

    /**
     * ABSTRACT — returns a label like "Indoor Plant" or "Outdoor Plant".
     */
    public abstract String getPlantType();

    // ── Waterable implementation ───────────────────────────────────────────

    @Override
    public void water() {
        this.lastWateredDate = LocalDate.now();
        // Watering boosts soil moisture to a healthy level
        this.soilMoisture = Math.min(100.0, this.soilMoisture + 30.0);
    }

    @Override
    public int getWateringFrequencyDays() {
        return wateringFrequencyDays;
    }

    @Override
    public boolean needsWatering() {
        if (lastWateredDate == null) return true;
        long daysSince = ChronoUnit.DAYS.between(lastWateredDate, LocalDate.now());
        return daysSince >= wateringFrequencyDays || soilMoisture < 30.0;
    }

    // ── Monitorable implementation ─────────────────────────────────────────

    @Override
    public double getSoilMoisture() { return soilMoisture; }

    @Override
    public double getLightLevel() { return lightLevel; }

    @Override
    public double getTemperature() { return temperature; }

    /**
     * Derives a health status string from sensor readings.
     * Demonstrates logic centralised in the base class.
     */
    @Override
    public String getHealthStatus() {
        int score = 0;
        if (soilMoisture >= 40 && soilMoisture <= 80) score++;
        if (lightLevel  >= 30 && lightLevel  <= 90) score++;
        if (temperature >= 15 && temperature <= 30) score++;
        if (!needsWatering()) score++;

        return switch (score) {
            case 4    -> "Excellent";
            case 3    -> "Good";
            case 2    -> "Fair";
            default   -> "Needs Attention";
        };
    }

    /** CSS badge class mapped to health status — used by Thymeleaf templates. */
    public String getHealthBadgeClass() {
        return switch (getHealthStatus()) {
            case "Excellent" -> "badge-green";
            case "Good"      -> "badge-green";
            case "Fair"      -> "badge-yellow";
            default          -> "badge-red";
        };
    }

    /** Days since last watering — used in UI display. */
    public long getDaysSinceWatered() {
        if (lastWateredDate == null) return wateringFrequencyDays;
        return ChronoUnit.DAYS.between(lastWateredDate, LocalDate.now());
    }

    public String getClassName() {
        return getClass().getSimpleName();
    }

    public boolean isIndoor() {
        return this instanceof IndoorPlant;
    }

    public boolean isOutdoor() {
        return this instanceof OutdoorPlant;
    }

    public String getSoilMoistureStatus() {
        if (soilMoisture >= 40 && soilMoisture <= 80) return "✅ Optimal";
        if (soilMoisture < 40) return "⚠ Too Dry";
        return "⚠ Too Wet";
    }

    public String getLightStatus() {
        if (lightLevel >= 30 && lightLevel <= 90) return "✅ Good";
        return "⚠ Adjust";
    }

    public String getTemperatureStatus() {
        if (temperature >= 15 && temperature <= 30) return "✅ Comfortable";
        return "⚠ Out of range";
    }

    // ── Getters (Encapsulation) ────────────────────────────────────────────
    public int       getId()              { return id; }
    public String    getName()            { return name; }
    public String    getSpecies()         { return species; }
    public String    getDescription()     { return description; }
    public String    getEmoji()           { return emoji; }
    public LocalDate getLastWateredDate() { return lastWateredDate; }

    // ── Setters with validation (Encapsulation) ───────────────────────────
    public void setId(int id) { this.id = id; }

    public void setName(String name) {
        if (name == null || name.isBlank())
            throw new InvalidPlantDataException("name", "Plant name cannot be empty.");
        if (name.length() > 100)
            throw new InvalidPlantDataException("name", "Plant name must be 100 characters or fewer.");
        this.name = name.trim();
    }

    public void setSpecies(String species) {
        if (species == null || species.isBlank())
            throw new InvalidPlantDataException("species", "Species cannot be empty.");
        this.species = species.trim();
    }

    public void setDescription(String description) {
        this.description = description != null ? description : "";
    }

    public void setEmoji(String emoji) {
        this.emoji = (emoji != null && !emoji.isBlank()) ? emoji : "🌿";
    }

    public void setLastWateredDate(LocalDate date) {
        this.lastWateredDate = date;
    }

    public void setWateringFrequencyDays(int days) {
        if (days < 1 || days > 365)
            throw new InvalidPlantDataException("wateringFrequencyDays",
                    "Watering frequency must be between 1 and 365 days.");
        this.wateringFrequencyDays = days;
    }

    public void setSoilMoisture(double value) {
        if (value < 0 || value > 100)
            throw new InvalidPlantDataException("soilMoisture",
                    "Soil moisture must be between 0 and 100.");
        this.soilMoisture = value;
    }

    public void setLightLevel(double value) {
        if (value < 0 || value > 100)
            throw new InvalidPlantDataException("lightLevel",
                    "Light level must be between 0 and 100.");
        this.lightLevel = value;
    }

    public void setTemperature(double value) {
        if (value < -20 || value > 60)
            throw new InvalidPlantDataException("temperature",
                    "Temperature must be between -20 and 60 °C.");
        this.temperature = value;
    }

    @Override
    public String toString() {
        return getPlantType() + "[id=" + id + ", name=" + name +
               ", species=" + species + ", health=" + getHealthStatus() + "]";
    }
}
