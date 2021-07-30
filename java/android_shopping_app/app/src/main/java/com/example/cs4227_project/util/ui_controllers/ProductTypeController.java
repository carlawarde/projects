package com.example.cs4227_project.util.ui_controllers;

import com.example.cs4227_project.util.enums.ProductType;

public class ProductTypeController {
    private static ProductType type = ProductType.CLOTHES;
    private static boolean female = true;

    private ProductTypeController() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isFemale() {
        return female;
    }

    public static void setFemale(boolean gender) {
        female = gender;
    }

    public static ProductType getType() {
        return type;
    }

    public static void setType(ProductType product) {
        type = product;
    }
}
