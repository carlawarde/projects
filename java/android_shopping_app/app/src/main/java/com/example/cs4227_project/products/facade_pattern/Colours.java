package com.example.cs4227_project.products.facade_pattern;

import android.util.Log;

import com.example.cs4227_project.util.LogTags;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Colours implements Attributes {
    private Map<String,List<String>> coloursMap = new HashMap<>();

    public Colours() {}

    public Colours(Map<String,Object> data) {
        for (Map.Entry<String,Object> entry: data.entrySet()) {
            coloursMap.put(entry.getKey(), (List<String>) entry.getValue());
        }
    }

    @Override
    public List<String> getAttributes() {
        String type = findProductType();
        return coloursMap.get(type);
    }

    @Override
    public void removeAttribute(String attribute) {
        String type = findProductType();
        if(Objects.requireNonNull(coloursMap.get(type)).contains(attribute)) {
            Objects.requireNonNull(coloursMap.get(type)).remove(attribute);
            Log.d(LogTags.ATTRIBUTE_MANAGER,"Removed attribute from "+ findProductType()+" styles");
        }
    }

    @Override
    public void addAttribute(String attribute) {
        String type = findProductType();
        if(!(Objects.requireNonNull(coloursMap.get(type)).contains(attribute))) {
            Objects.requireNonNull(coloursMap.get(type)).add(attribute);
            Log.d(LogTags.ATTRIBUTE_MANAGER,"Added attribute to "+ findProductType()+" styles");
        }
    }

    @Override
    public void addAttributes(Map<String,List<String>>attributes) {
        coloursMap = attributes;
    }

    @Override
    public String findProductType() {
        return "colour";
    }
}
