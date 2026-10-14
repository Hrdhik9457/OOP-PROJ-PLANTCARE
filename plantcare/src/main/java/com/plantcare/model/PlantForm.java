package com.plantcare.model;

import jakarta.validation.constraints.*;

/**
 * FORM DTO — PlantForm
 * Bridges the HTML form submission to the abstract Plant hierarchy.
 * Uses Jakarta Validation annotations so Spring can validate before the
 * controller processes the data.
 */
public class PlantForm {

    @NotBlank(message = "Plant name is required.")
    @Size(max = 100, message = "Name must be 100 characters or fewer.")
    private String name = "";

    @NotBlank(message = "Species is required.")
    private String species = "";

    private String description = "";
    private String emoji = "🌿";

    /** "INDOOR" or "OUTDOOR" */
    @NotBlank(message = "Please select a plant type.")
    private String plantType = "INDOOR";

    @Min(value = 1,   message = "Watering frequency must be at least 1 day.")
    @Max(value = 365, message = "Watering frequency cannot exceed 365 days.")
    private int wateringFrequencyDays = 7;

    @DecimalMin(value = "0",   message = "Soil moisture must be 0–100.")
    @DecimalMax(value = "100", message = "Soil moisture must be 0–100.")
    private double soilMoisture = 50.0;

    @DecimalMin(value = "0",   message = "Light level must be 0–100.")
    @DecimalMax(value = "100", message = "Light level must be 0–100.")
    private double lightLevel = 60.0;

    @DecimalMin(value = "-20", message = "Temperature must be -20 to 60 °C.")
    @DecimalMax(value = "60",  message = "Temperature must be -20 to 60 °C.")
    private double temperature = 22.0;

    // — Indoor-only fields —
    private String  lightRequirement = "MEDIUM"; // LOW | MEDIUM | HIGH
    private boolean airPurifying     = false;
    private String  idealRoom        = "Living Room";

    // — Outdoor-only fields —
    private String  sunExposure = "FULL_SUN"; // FULL_SUN | PARTIAL_SHADE | FULL_SHADE
    private boolean edible      = false;
    private String  season      = "Year-round";

    // ── Getters / Setters ──────────────────────────────────────────────────
    public String getName()                        { return name; }
    public void   setName(String name)             { this.name = name; }

    public String getSpecies()                     { return species; }
    public void   setSpecies(String species)       { this.species = species; }

    public String getDescription()                 { return description; }
    public void   setDescription(String d)         { this.description = d; }

    public String getEmoji()                       { return emoji; }
    public void   setEmoji(String emoji)           { this.emoji = emoji; }

    public String getPlantType()                   { return plantType; }
    public void   setPlantType(String t)           { this.plantType = t; }

    public int  getWateringFrequencyDays()         { return wateringFrequencyDays; }
    public void setWateringFrequencyDays(int d)    { this.wateringFrequencyDays = d; }

    public double getSoilMoisture()                { return soilMoisture; }
    public void   setSoilMoisture(double v)        { this.soilMoisture = v; }

    public double getLightLevel()                  { return lightLevel; }
    public void   setLightLevel(double v)          { this.lightLevel = v; }

    public double getTemperature()                 { return temperature; }
    public void   setTemperature(double v)         { this.temperature = v; }

    public String  getLightRequirement()           { return lightRequirement; }
    public void    setLightRequirement(String lr)  { this.lightRequirement = lr; }

    public boolean isAirPurifying()                { return airPurifying; }
    public void    setAirPurifying(boolean v)      { this.airPurifying = v; }

    public String  getIdealRoom()                  { return idealRoom; }
    public void    setIdealRoom(String r)          { this.idealRoom = r; }

    public String  getSunExposure()                { return sunExposure; }
    public void    setSunExposure(String v)        { this.sunExposure = v; }

    public boolean isEdible()                      { return edible; }
    public void    setEdible(boolean v)            { this.edible = v; }

    public String  getSeason()                     { return season; }
    public void    setSeason(String s)             { this.season = s; }
}
