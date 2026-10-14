package com.plantcare.model;

/**
 * SUBCLASS — OutdoorPlant extends Plant
 *
 * OOP Concepts demonstrated:
 *   - Inheritance   : extends Plant, reuses all base fields and logic
 *   - Polymorphism  : overrides getCareRecommendation() with outdoor-specific advice
 *   - Encapsulation : adds its own private fields with getters/setters
 *
 * Outdoor plants have a sun-exposure requirement and may be edible (fruits/veg).
 */
public class OutdoorPlant extends Plant {

    /** Enum for how much direct sun an outdoor plant needs. */
    public enum SunExposure { FULL_SUN, PARTIAL_SHADE, FULL_SHADE }

    // ── OutdoorPlant-specific fields ───────────────────────────────────────
    private SunExposure sunExposure;
    private boolean     isEdible;     // fruits, vegetables, herbs
    private String      season;       // e.g. "Spring–Summer", "Year-round"

    // ── Constructor ────────────────────────────────────────────────────────
    public OutdoorPlant(int id, String name, String species, String description,
                        String emoji, int wateringFrequencyDays,
                        double soilMoisture, double lightLevel, double temperature,
                        SunExposure sunExposure, boolean isEdible, String season) {
        super(id, name, species, description, emoji,
              wateringFrequencyDays, soilMoisture, lightLevel, temperature);
        this.sunExposure = sunExposure;
        this.isEdible    = isEdible;
        this.season      = season != null ? season : "Year-round";
    }

    // ── Polymorphism: outdoor-specific care recommendation ─────────────────
    @Override
    public String getCareRecommendation() {
        StringBuilder advice = new StringBuilder();

        // Watering advice
        if (needsWatering()) {
            advice.append("💧 Water needed! Check soil before watering to avoid overwatering. ");
        } else {
            advice.append("✅ Watering schedule is fine. Monitor soil if it rains heavily. ");
        }

        // Sun exposure advice
        switch (sunExposure) {
            case FULL_SUN ->
                advice.append("☀️ Needs 6+ hours of direct sunlight daily. Ideal spot: south-facing garden. ");
            case PARTIAL_SHADE ->
                advice.append("⛅ Thrives with 3–6 hours of sun. Protect from harsh afternoon sun. ");
            case FULL_SHADE ->
                advice.append("🌿 Prefers shade. Place under a canopy or north-facing position. ");
        }

        // Temperature advice for outdoor plants
        if (getTemperature() < 5) {
            advice.append("❄️ Risk of frost! Consider covering the plant or bringing it indoors. ");
        } else if (getTemperature() > 35) {
            advice.append("🌡 Extreme heat — water more frequently and add mulch to retain moisture. ");
        }

        // Edible plant tip
        if (isEdible) {
            advice.append("🥬 Edible plant: avoid chemical pesticides. Use organic treatments where possible.");
        }

        return advice.toString().trim();
    }

    @Override
    public String getPlantType() { return "Outdoor Plant"; }

    // ── Getters / Setters ──────────────────────────────────────────────────
    public SunExposure getSunExposure()               { return sunExposure; }
    public void setSunExposure(SunExposure s)         { this.sunExposure = s; }

    public boolean isEdible()                         { return isEdible; }
    public void setEdible(boolean edible)             { this.isEdible = edible; }

    public String getSeason()                         { return season; }
    public void setSeason(String season)              { this.season = season; }

    public String getSunExposureLabel() {
        return switch (sunExposure) {
            case FULL_SUN     -> "Full Sun";
            case PARTIAL_SHADE -> "Partial Shade";
            case FULL_SHADE   -> "Full Shade";
        };
    }
}
