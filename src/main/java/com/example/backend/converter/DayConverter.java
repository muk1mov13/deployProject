package com.example.backend.converter;

import jakarta.persistence.AttributeConverter;

import java.util.Arrays;

public class DayConverter implements AttributeConverter<String[], String> {
    @Override
    public String convertToDatabaseColumn(String[] days) {
        return Arrays.toString(days);
    }
    @Override
    public String[] convertToEntityAttribute(String s) {
        return null;
    }
}

