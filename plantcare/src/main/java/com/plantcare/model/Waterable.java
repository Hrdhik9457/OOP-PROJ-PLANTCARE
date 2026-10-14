package com.plantcare.model;

/**
 * INTERFACE — Waterable
 *
 * OOP Concept: Interface / Abstraction
 * Defines the contract for any plant that can be watered.
 * Classes that implement this must provide their own watering logic.
 */
public interface Waterable {

    /** Water the plant — implementation varies by plant type. */
    void water();

    /** How many days between waterings. */
    int getWateringFrequencyDays();

    /** Returns true if the plant is due for watering. */
    boolean needsWatering();
}
