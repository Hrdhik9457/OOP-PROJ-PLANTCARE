package com.plantcare.model;

/**
 * INTERFACE — Monitorable
 *
 * OOP Concept: Interface / Abstraction
 * Defines the contract for plants whose environment can be monitored.
 * Any class implementing this must provide sensor-style readings.
 */
public interface Monitorable {

    /** Soil moisture as a percentage (0–100). */
    double getSoilMoisture();

    /** Ambient light level as a percentage (0–100). */
    double getLightLevel();

    /** Ambient temperature in Celsius. */
    double getTemperature();

    /** Returns a human-readable health status based on readings. */
    String getHealthStatus();
}
