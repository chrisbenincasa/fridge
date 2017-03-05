package com.chrisbenincasa.food.db.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum FoodItemUnit {
    Whole("whole");

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase();
    }

    private final String formatted;

    FoodItemUnit(String formatted) {
        this.formatted = formatted;
    }

    private static Map<String, FoodItemUnit> FORMAT_MAP = Stream.of(FoodItemUnit.values()).collect(
            Collectors.toMap(s -> s.formatted, Function.identity()));

    @JsonCreator // This is the factory method and must be static
    public static FoodItemUnit fromString(String string) {
        FoodItemUnit status = FORMAT_MAP.get(string);
        if (status == null) {
            throw new IllegalArgumentException(string + " has no corresponding value");
        }
        return status;
    }
}
