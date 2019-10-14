package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDateTime;

public class MealToValidationUtils {
    public static void validateCaloriesIsManadatory(MealTo mealTo) {
        if (mealTo.getCalories() == 0) {
            throw new IllegalArgumentException("Calories should be specified");
        }
    }

    public static void validateDescriptionIsMandatory(MealTo mealTo) {
        String description = mealTo.getDescription();
        if (description == null || "".equals(description)) {
            throw new IllegalArgumentException("Description should be specified");
        }
    }

    public static void validateDateIsMandatory(MealTo mealTo) {
        LocalDateTime dateTime = mealTo.getDateTime();
        if (dateTime == null) {
            throw new IllegalArgumentException("Date should be specified");
        }
    }

    public static void validateIsNew(MealTo mealTo) {
        if (mealTo.getId() != null)
            throw new IllegalArgumentException(mealTo + " must be new ");
    }
}
