package com.plantcare.exception;

/**
 * EXCEPTION — InvalidPlantDataException
 *
 * OOP Concept: Exception Handling
 * Thrown when plant data fails validation (e.g. null name, out-of-range sensor value).
 * Extends RuntimeException so callers are not forced to catch it, keeping service
 * code readable while still allowing explicit handling where needed.
 */
public class InvalidPlantDataException extends RuntimeException {

    private final String field;

    public InvalidPlantDataException(String field, String message) {
        super("Invalid value for '" + field + "': " + message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
