package com.example.cs4227_project.util.database_controllers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cs4227_project.database.Database;
import com.example.cs4227_project.database.StockReadListener;
import com.example.cs4227_project.util.LogTags;
import com.example.cs4227_project.order.command_pattern.Stock;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockDatabaseController {
    private final Database db = Database.getInstance();
    private final List<Stock> stockList = new ArrayList<>();
    private StockReadListener stockL;
    private Stock stockItem;

    //constant strings
    private static final String STOCK = "stock";
    private static final String SUCCESS = "success";

    public StockDatabaseController() {}

    public StockDatabaseController(StockReadListener ml){
        this.stockL = ml;
    }


    public void addStockToDB(Stock s) {
        db.post(STOCK, s);
    }

    public void addStockToDB(String id, Stock s) {
        db.put(STOCK, id, s);
    }

    /**
     * Updates a document in the database.
     * @author Aine Reynolds
     * @param id - the document id that needs to be updated.
     * @param field - the name of the field that is to be changed.
     * @param val - the object to updated the field to.
     */
    public void updateStock(String id, String field, Object val){
        db.patch(STOCK, id, field, val);
    }

    /**
     * Gets the stock collection from the database
     * @author Aine Reynolds
     */
    public void getStockCollection() {
        stockList.clear();
        //get reference to collection from database
        CollectionReference colRef = db.get(STOCK);
        colRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(LogTags.DB_GET, document.getId() + " => " + document.getData());
                                //convert document to Product and add to List of data
                                readStockIntoList(document);
                            }
                            stockL.stockCallback(SUCCESS);
                        } else {
                            Log.d(LogTags.DB_GET, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /**
     * Retrives Documents from database and puts them in arraylist.
     * @author Aine Reynolds
     * @param ids - list of the document ids that are wanted.
     */
    public void getStockDocs(final List<String> ids){
        stockList.clear();
        for(String id : ids){
            DocumentReference docRef = db.get(STOCK, id);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Stock s = getStock(document.getData());
                            stockList.add(s);
                            Log.d(LogTags.DB_GET, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(LogTags.DB_GET, "No such document");
                        }
                    } else {
                        Log.d(LogTags.DB_GET, "get failed with ", task.getException());
                    }
                    stockL.stockCallback(SUCCESS);
                }
            });
        }
    }

    public void getStockDoc(String id){
        DocumentReference docRef = db.get(STOCK, id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Stock s = getStock(document.getData());
                            stockItem = s;
                            Log.d(LogTags.DB_GET, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(LogTags.DB_GET, "No such document");
                        }
                    } else {
                        Log.d(LogTags.DB_GET, "get failed with ", task.getException());
                    }
                    stockL.stockCallback(SUCCESS);
                }
            });
    }

    /**
     * Converts Map into stock object
     * @author Aine Reynolds
     * @param stock - the map that was retrieved from the database
     * @return Stock object.
     */
    public Stock getStock(Map<String, Object> stock) {
        return new Stock((String)stock.get("id"), (HashMap<String, String>)stock.get("sizeQuantity"), (String)stock.get("type"), (boolean)stock.get("female"));
    }

    /**
     * Calls getStock method and adds it to an arraylist.
     * @author Aine Reynolds
     */
    public void readStockIntoList(QueryDocumentSnapshot document) {
        Stock s = getStock(document.getData());
        stockList.add(s);
    }

    /**
     * Returns list of Stock
     * @author Aine Reynolds
     */
    public List<Stock> getStockArray() {
        return stockList;
    }

    public Stock getStockItem(){ return stockItem; }
}
