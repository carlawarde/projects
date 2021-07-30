package com.example.cs4227_project.order.memento_pattern;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CareTaker {
    private final List<Memento> mementoList = new ArrayList<>();

    public void add(Memento state){
        Log.d("CareTaker", "Saving state to list  " + state.getState().getSizeQuantity());
        mementoList.add(state);
    }

    public Memento get(int index){
        return mementoList.get(index);
    }

    public List<Memento> getMementoList(){
        for(Memento m : mementoList){
            Log.d("CareTaker", "memento array list" + m.getState().getSizeQuantity());
        }
        return mementoList;
    }
}
