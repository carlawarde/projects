package com.example.cs4227_project.order.memento_pattern;

import android.util.Log;

import com.example.cs4227_project.order.command_pattern.Stock;

public class Memento {
    private final Stock state;

    public Memento(Stock state){
        Log.d("Memento", "Memento state " + state.getSizeQuantity());
        this.state = state;
    }

    public Stock getState(){
        return state;
    }
}
