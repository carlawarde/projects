package com.example.cs4227_project.products.abstract_factory_pattern;

import com.example.cs4227_project.util.enums.ProductType;

import java.util.Map;

public abstract class AbstractFactory {
    public abstract Product getProduct(ProductType productType);
    public abstract Product getProduct(ProductType productType, Map<String, Object> data);
}
