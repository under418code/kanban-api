package com.undere418code.kanban_api.util;

import com.undere418code.kanban_api.exception.ValidationException;
import java.time.LocalDate;

public final class FieldCheckUtils {

    private FieldCheckUtils() {
    }

    // -----------------------------
    // String
    // -----------------------------
    public static void requireNotBlankaAndNotNull(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new ValidationException(fieldName + " cannot be blank or null");
        }
    }

    // -----------------------------
    // Integer
    // -----------------------------
    public static void requireNotNull(Integer value, String fieldName) {
        if (value == null) {
            throw new ValidationException(fieldName + " cannot be null");
        }
    }

    public static void requireNotNullandPositive(Integer value, String fieldName) {
        if (value == null || value <= 0) {
            throw new ValidationException(fieldName + " must be positive");
        }
    }

    // public static void requireNotNullandPositive(JsonNullable<Integer> value, String fieldName) {
    //     if (value.isPresent()) {
    //         requireNotNullandPositive(value.get(),fieldName);
    //     }
    // }

    // -----------------------------
    // Enum (CardStatus)
    // -----------------------------
    public static void requireNonNull(Enum<?> value, String fieldName) {
        if (value == null) {
            throw new ValidationException(fieldName + " cannot be null");
        }
    }

    // -----------------------------
    // LocalDate
    // -----------------------------
    public static void requireNonNull(LocalDate value, String fieldName) {
        if (value == null) {
            throw new ValidationException(fieldName + " cannot be null");
        }
    }

    public static void requireNotPast(LocalDate value, String fieldName) {
        if (value != null && value.isBefore(LocalDate.now())) {
            throw new ValidationException(fieldName + " cannot be in the past");
        }
    }
}
