package com.example.cs4227_project.order.command_pattern;

import android.util.Log;

import java.util.ArrayList;

public class CommandControl {
    private ArrayList<Command> commandList = new ArrayList<>();

    public void addCommand(Command command){
        Log.d("STOCKS", "Adding command");
        commandList.add(command);
    }

    public void executeCommands(){
        Log.d("STOCKS", "Executing command");
        for(Command c : commandList){
            c.execute();
        }
        commandList.clear();
    }
}
