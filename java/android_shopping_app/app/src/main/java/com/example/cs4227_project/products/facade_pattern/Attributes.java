package com.example.cs4227_project.products.facade_pattern;

import java.util.Map;
import java.util.List;

public interface Attributes {
    //Returns an ArrayList of attributes
    public List<String> getAttributes();
    //Removes specified attribute from an ArrayList in the HashMap
    public void removeAttribute(String attribute);
    //Adds a specified attribute to anArrayList in the HashMap
    public void addAttribute(String attribute);
    //Sets the attributes HashMap to the argument HashMap
    public void addAttributes(Map<String,List<String>> attributes);
    //Returns a string of the ProductType
    public String findProductType();
}
