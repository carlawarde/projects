package com.example.cs4227_project.order.command_pattern;

import android.util.Log;

import com.example.cs4227_project.util.database_controllers.ProductDatabaseController;
import com.example.cs4227_project.util.database_controllers.StockDatabaseController;
import com.example.cs4227_project.util.LogTags;
import com.example.cs4227_project.util.enums.ProductDatabaseFields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddStock implements Command {
    private final Stock abcStock;
    private final StockDatabaseController stockDb = new StockDatabaseController();

    public AddStock(Stock abcStock, int quantity, String size){
        Log.d(LogTags.COMMAND_DP, "Add stock values: quantity = " + quantity + " Size = " + size);
        this.abcStock = abcStock;
        Map<String, String> sizesQ = abcStock.getSizeQuantity();
        Log.d(LogTags.COMMAND_DP, "Getting from hashmap " + sizesQ.get(size));
        int val = 0;
        if(sizesQ.containsKey(size)){
            val = Integer.parseInt(sizesQ.get(size));
        }else{
            val = 0;
        }
        int q = val + quantity;

        sizesQ.put(size, Integer.toString(q));
        abcStock.setSizeQuantity(sizesQ);
        Log.d(LogTags.COMMAND_DP, "Size quantity = " + sizesQ.toString());

        stockDb.updateStock(abcStock.getId(), "sizeQuantity", sizesQ);
        updateProductSizes();
    }

    public AddStock(Stock abcStock){
        this.abcStock = abcStock;
        stockDb.addStockToDB(abcStock.getId(), this.abcStock);
    }

    public void execute() {
        abcStock.addStock();
    }

    public void updateProductSizes(){
        Map<String, String> sizesQ = abcStock.getSizeQuantity();
        List<String> productSize = new ArrayList<>();
        for(Map.Entry<String, String> entry : sizesQ.entrySet()){
            productSize.add(entry.getKey());
        }

        ProductDatabaseController productDb = new ProductDatabaseController();
        productDb.updateProductField(abcStock.getId(), ProductDatabaseFields.SIZES, productSize);
    }
}
