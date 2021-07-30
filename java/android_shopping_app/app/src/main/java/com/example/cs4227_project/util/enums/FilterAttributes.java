package com.example.cs4227_project.util.enums;

public enum FilterAttributes {
    BRANDS("brand"),
    COLOURS("colour"),
    SIZES("size"),
    STYLES("style");

    private final String stringValue;

    private FilterAttributes(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getValue() {
        return this.stringValue;
    }
}
