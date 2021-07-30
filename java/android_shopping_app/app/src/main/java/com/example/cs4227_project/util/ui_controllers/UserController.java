package com.example.cs4227_project.util.ui_controllers;

import com.example.cs4227_project.user.User;

public class UserController {
    private static User user;

    private UserController() {
        throw new IllegalStateException("Utility class");
    }

    public static void setUser(User u){ user = u;}

    public static User getUser(){ return user; }
}
