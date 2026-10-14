package com.plantcare.model;

/**
 * SUBCLASS — IndoorPlant extends Plant
 *
 * OOP Concepts demonstrated:
 *   - Inheritance   : extends Plant, reuses all base fields and logic
 *   - Polymorphism  : overrides getCareRecommendation() with indoor-specific advice
 *   - Encapsulation : adds its own private fields with getters/setters
 *
 * Indoor plants have a light requirement level and may purify air.
 */
public class IndoorPlant extends Plant {

    /** Enum for how much light an indoor plant needs. */
    public enum LightRequirement { LOW, MEDIUM, HIGH }

    // ── IndoorPlant-specific fields ────────────────────────────────────────
    private LightRequirement lightRequirement;
    private boolean          isAirPurifying;
    private String           idealRoom;   // e.g. "Living Room", "Bedroom"

    // ── Constructor ────────────────────────────────────────────────────────
    public IndoorPlant(int id, String name, String species, String description,
                       String emoji, int wateringFrequencyDays,
                       double soilMoisture, double lightLevel, double temperature,
                       LightRequirement lightRequirement, boolean isAirPurifying,
                       String idealRoom) {
        super(id, name, species, description, emoji,
              wateringFrequencyDays, soilMoisture, lightLevel, temperature);
        this.lightRequirement = lightRequirement;
        this.isAirPurifying   = isAirPurifying;
        this.idealRoom        = idealRoom != null ? idealRoom : "Any room";
    }

    // ── Polymorphism: indoor-specific care recommendation ──────────────────
    @Override
    public String getCareRecommendation() {
        StringBuilder advice = new StringBuilder();

        // Watering advice
        if (needsWatering()) {
            advice.append("💧 Time to water! Soil moisture is low. ");
        } else {
            advice.append("✅ Watering is on schedule. ");
        }

        // Light advice based on light requirement
        switch (lightRequirement) {
            case LOW ->
                advice.append("🌑 Keep away from direct sunlight. A shaded corner is ideal. ");
            case MEDIUM ->
                advice.append("🌤 Place near a window with indirect light. ");
            case HIGH ->
                advice.append("☀️ Needs bright, indirect light — place near a south-facing window. ");
        }

        // Temperature advice
        if (getTemperature() < 15) {
            advice.append("🧥 Too cold! Move away from drafts and heating vents. ");
        } else if (getTemperature() > 30) {
            advice.append("🌡 Too warm! Ensure good air circulation indoors. ");
        }

        // Air-purifying note
        if (isAirPurifying) {
            advice.append("🌬 Bonus: this plant helps purify indoor air quality.");
        }

        return advice.toString().trim();
    }

    @Override
    public String getPlantType() { return "Indoor Plant"; }

    // ── Getters / Setters ──────────────────────────────────────────────────
    public LightRequirement getLightRequirement()         { return lightRequirement; }
    public void setLightRequirement(LightRequirement lr)  { this.lightRequirement = lr; }

    public boolean isAirPurifying()                       { return isAirPurifying; }
    public void setAirPurifying(boolean airPurifying)     { this.isAirPurifying = airPurifying; }

    public String getIdealRoom()                          { return idealRoom; }
    public void setIdealRoom(String idealRoom)            { this.idealRoom = idealRoom; }

    public String getLightRequirementLabel() {
        return switch (lightRequirement) {
            case LOW    -> "Low Light";
            case MEDIUM -> "Medium Light";
            case HIGH   -> "Bright Light";
        };
    }
}
