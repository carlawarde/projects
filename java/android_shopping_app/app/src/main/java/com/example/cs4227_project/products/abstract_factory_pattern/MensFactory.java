package com.example.cs4227_project.products.abstract_factory_pattern;

import com.example.cs4227_project.util.enums.ProductDatabaseFields;
import com.example.cs4227_project.util.enums.ProductType;

import java.util.List;
import java.util.Map;

public class MensFactory extends AbstractFactory {

    @Override
    public Product getProduct(ProductType productType) {
        switch (productType) {
            case CLOTHES:
                return new MensClothes();
            case SHOE:
                return new MensShoe();
            case ACCESSORIES:
                return new MensAccessory();
        }
        return null;
    }

    @Override
    public Product getProduct(ProductType type, Map<String, Object> data) {
        switch (type) {
            case ACCESSORIES:
                return new MensAccessory((String) data.get(ProductDatabaseFields.ID.getValue()),(String) data.get(ProductDatabaseFields.NAME.getValue()), (double) data.get(ProductDatabaseFields.PRICE.getValue()),
                        (String) data.get(ProductDatabaseFields.BRAND.getValue()), (String) data.get(ProductDatabaseFields.COLOUR.getValue()),
                        (String) data.get(ProductDatabaseFields.STYLE.getValue()), (List<String>) data.get(ProductDatabaseFields.SIZES.getValue()),(String) data.get(ProductDatabaseFields.IMAGEURL.getValue()));
            case SHOE:
                return new MensShoe((String) data.get(ProductDatabaseFields.ID.getValue()),(String) data.get(ProductDatabaseFields.NAME.getValue()), (double) data.get(ProductDatabaseFields.PRICE.getValue()),
                        (String) data.get(ProductDatabaseFields.BRAND.getValue()), (String) data.get(ProductDatabaseFields.COLOUR.getValue()),
                        (String) data.get(ProductDatabaseFields.STYLE.getValue()),(List<String>) data.get(ProductDatabaseFields.SIZES.getValue()), (String) data.get(ProductDatabaseFields.IMAGEURL.getValue()));
            case CLOTHES:
                return new MensClothes((String) data.get(ProductDatabaseFields.ID.getValue()),(String) data.get(ProductDatabaseFields.NAME.getValue()), (double) data.get(ProductDatabaseFields.PRICE.getValue()),
                        (String) data.get(ProductDatabaseFields.BRAND.getValue()), (String) data.get(ProductDatabaseFields.COLOUR.getValue()),
                        (String) data.get(ProductDatabaseFields.STYLE.getValue()), (List<String>) data.get(ProductDatabaseFields.SIZES.getValue()),(String) data.get(ProductDatabaseFields.IMAGEURL.getValue()));
        }
        return  null;
    }
}
