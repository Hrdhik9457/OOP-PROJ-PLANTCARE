package com.plantcare.service;

import com.plantcare.model.EnvironmentTelemetry;
import com.plantcare.model.Plant;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * SERVICE — MonitoringService
 *
 * OOP Concept: Business Service / Sensor Simulation Engine
 * Generates and updates realistic telemetry values for in-memory plants.
 * Strictly simulated in Java for demonstration and college viva evaluation.
 */
@Service
public class MonitoringService {

    private final PlantService plantService;
    private final Random random = new Random();

    public MonitoringService(PlantService plantService) {
        this.plantService = plantService;
    }

    public List<EnvironmentTelemetry> getTelemetryList() {
        return plantService.getAllPlants().stream()
                .map(EnvironmentTelemetry::new)
                .toList();
    }

    /**
     * Simulates fresh sensor readings across all plants with slight variations.
     * Demonstrates dynamic mutation of in-memory objects.
     */
    public void refreshSimulatedReadings() {
        for (Plant plant : plantService.getAllPlants()) {
            // Soil moisture drops slightly over time (0.5% to 2.5%), capped at 5% min, 95% max
            double deltaMoisture = (random.nextDouble() * 3.0) - 1.5;
            double newMoisture = Math.max(5.0, Math.min(95.0, plant.getSoilMoisture() + deltaMoisture));
            plant.setSoilMoisture(Math.round(newMoisture * 10.0) / 10.0);

            // Light level fluctuates between ±5%
            double deltaLight = (random.nextDouble() * 10.0) - 5.0;
            double newLight = Math.max(10.0, Math.min(100.0, plant.getLightLevel() + deltaLight));
            plant.setLightLevel(Math.round(newLight * 10.0) / 10.0);

            // Ambient temperature fluctuates between ±0.8°C
            double deltaTemp = (random.nextDouble() * 1.6) - 0.8;
            double newTemp = Math.max(16.0, Math.min(32.0, plant.getTemperature() + deltaTemp));
            plant.setTemperature(Math.round(newTemp * 10.0) / 10.0);
        }
    }

    public double getAverageMoisture() {
        List<Plant> plants = plantService.getAllPlants();
        if (plants.isEmpty()) return 0.0;
        return plants.stream().mapToDouble(Plant::getSoilMoisture).average().orElse(0.0);
    }

    public double getAverageLight() {
        List<Plant> plants = plantService.getAllPlants();
        if (plants.isEmpty()) return 0.0;
        return plants.stream().mapToDouble(Plant::getLightLevel).average().orElse(0.0);
    }

    public double getAverageTemperature() {
        List<Plant> plants = plantService.getAllPlants();
        if (plants.isEmpty()) return 0.0;
        return plants.stream().mapToDouble(Plant::getTemperature).average().orElse(0.0);
    }

    public long getAlertCount() {
        return plantService.getAllPlants().stream()
                .filter(p -> p.getSoilMoisture() < 40.0 || p.getTemperature() > 28.0 || p.getTemperature() < 18.0)
                .count();
    }
}
