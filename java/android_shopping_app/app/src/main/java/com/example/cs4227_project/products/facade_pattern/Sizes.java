package com.example.cs4227_project.products.facade_pattern;

import android.util.Log;

import com.example.cs4227_project.util.enums.ProductType;
import com.example.cs4227_project.util.LogTags;
import com.example.cs4227_project.util.ui_controllers.ProductTypeController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Sizes implements Attributes {
    private Map<String,List<String>> sizesMap = new HashMap<>();

    public Sizes() {}

    public Sizes(Map<String,Object> data) {
        for (Map.Entry<String,Object> entry: data.entrySet()) {
            sizesMap.put(entry.getKey(), (List<String>) entry.getValue());
        }
    }

    @Override
    public List<String> getAttributes() {
        String type = findProductType();
        return sizesMap.get(type);
    }

    @Override
    public void removeAttribute(String attribute) {
        String type = findProductType();
        if(Objects.requireNonNull(sizesMap.get(type)).contains(attribute)) {
            Objects.requireNonNull(sizesMap.get(type)).remove(attribute);
            Log.d(LogTags.ATTRIBUTE_MANAGER,"Removed attribute from "+ findProductType()+" sizes");
        }
    }

    @Override
    public void addAttribute(String attribute) {
        String type = findProductType();
        if(!(Objects.requireNonNull(sizesMap.get(type)).contains(attribute))) {
            Objects.requireNonNull(sizesMap.get(type)).add(attribute);
            Log.d(LogTags.ATTRIBUTE_MANAGER,"Added attribute to "+ findProductType()+" sizes");
        }
    }

    @Override
    public void addAttributes(Map<String,List<String>> attributes) {
        sizesMap = attributes;
    }

    @Override
    public String findProductType() {
        if(ProductTypeController.getType().equals(ProductType.ACCESSORIES)) {
            return ProductTypeController.getType().getValue();
        }
        else {
            return ProductTypeController.getType().getValue() + ProductTypeController.isFemale();
        }
    }
}
