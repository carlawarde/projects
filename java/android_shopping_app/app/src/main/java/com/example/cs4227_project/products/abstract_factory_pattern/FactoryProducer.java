package com.example.cs4227_project.products.abstract_factory_pattern;

public class FactoryProducer {
    private FactoryProducer() {
        throw new IllegalStateException("Utility class");
    }

    public static AbstractFactory getFactory(boolean female){
        if(female) {
            return new WomensFactory();
        } else {
            return new MensFactory();
        }
    }
}
