package com.example.cs4227_project.order.command_pattern;

import android.util.Log;

import com.example.cs4227_project.util.LogTags;
import com.example.cs4227_project.order.memento_pattern.Memento;

import java.util.Map;

public class Stock {
    private String id;
    private Map<String, String> sizeQuantity;
    private String type;
    private Stock state;
    boolean female;

    public Stock(String id, Map<String, String> sizeQuantity, String type, boolean female){
        this.id = id;
        this.sizeQuantity = sizeQuantity;
        this.type = type;
        this.female = female;
    }

    public Stock() {}

    //For admin adding products to site
    public void addStock(){
        Log.d(LogTags.ADDSTOCK, "Stock added");
    }

    // When customer buys products
    public void sell(){
        Log.d(LogTags.SELLSTOCK, "Stock sold");
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public Map<String, String> getSizeQuantity() { return sizeQuantity; }

    public void setSizeQuantity(Map<String, String> sizeQuantity) { this.sizeQuantity = sizeQuantity; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public boolean isFemale() { return female; }

    public void setFemale(boolean female) { this.female = female; }

    public String toString(){
        return this.id;
    }

    public void setState(Stock state){ this.state = state; }

    public Stock getState(){
        return state;
    }

    public Memento saveStateToMemento(){
        return new Memento(state);
    }

    public void getStateFromMemento(Memento memento){
        state = memento.getState();
    }
}
