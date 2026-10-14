package com.plantcare.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * TELEMETRY — EnvironmentTelemetry
 *
 * OOP Concept: Composition & Data Transfer Object
 * Captures environmental sensor readings for a plant.
 * Explicitly flags all data as simulated to adhere to academic integrity guidelines.
 */
public class EnvironmentTelemetry {

    private final int plantId;
    private final String plantName;
    private final String plantEmoji;
    private final String plantType;
    private final double soilMoisture;
    private final double lightLevel;
    private final double temperature;
    private final String healthStatus;
    private final String healthBadgeClass;
    private final String moistureStatus;
    private final String lightStatus;
    private final String temperatureStatus;
    private final LocalDateTime timestamp;
    private final boolean simulated = true;

    public EnvironmentTelemetry(Plant plant) {
        this.plantId = plant.getId();
        this.plantName = plant.getName();
        this.plantEmoji = plant.getEmoji();
        this.plantType = plant.getPlantType();
        this.soilMoisture = plant.getSoilMoisture();
        this.lightLevel = plant.getLightLevel();
        this.temperature = plant.getTemperature();
        this.healthStatus = plant.getHealthStatus();
        this.healthBadgeClass = plant.getHealthBadgeClass();
        this.moistureStatus = plant.getSoilMoistureStatus();
        this.lightStatus = plant.getLightStatus();
        this.temperatureStatus = plant.getTemperatureStatus();
        this.timestamp = LocalDateTime.now();
    }

    public int getPlantId() { return plantId; }
    public String getPlantName() { return plantName; }
    public String getPlantEmoji() { return plantEmoji; }
    public String getPlantType() { return plantType; }
    public double getSoilMoisture() { return soilMoisture; }
    public double getLightLevel() { return lightLevel; }
    public double getTemperature() { return temperature; }
    public String getHealthStatus() { return healthStatus; }
    public String getHealthBadgeClass() { return healthBadgeClass; }
    public String getMoistureStatus() { return moistureStatus; }
    public String getLightStatus() { return lightStatus; }
    public String getTemperatureStatus() { return temperatureStatus; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public boolean isSimulated() { return simulated; }

    public String getFormattedTimestamp() {
        return timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
