package com.example.cs4227_project.order.command_pattern;

import android.util.Log;

import com.example.cs4227_project.util.database_controllers.StockDatabaseController;

import java.util.Map;

public class SellStock implements Command {
    private final Stock abcStock;
    private final StockDatabaseController stockDb;
    private Map<String, String> sizesQ;

    /**
     * Command DP - SellStock
     * @author Aine Reynolds
     * Updates the quantity value for a particular stock.
     * Then updates the database.
     */
    public SellStock(Stock abcStock, int quantity, String size){
        this.stockDb = new StockDatabaseController();
        this.abcStock = abcStock;
        this.sizesQ = abcStock.getSizeQuantity();
        int val = Integer.parseInt(sizesQ.get(size));
        sizesQ.put(size, Integer.toString(val-quantity));
    }

    public void execute() {
        Log.d("STOCKS", "Executing sellStock");
        stockDb.updateStock(abcStock.getId(), "sizeQuantity", sizesQ);
        abcStock.sell();
    }
}
