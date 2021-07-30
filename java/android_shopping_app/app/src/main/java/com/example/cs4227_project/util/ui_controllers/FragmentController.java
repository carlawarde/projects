package com.example.cs4227_project.util.ui_controllers;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cs4227_project.util.LogTags;

public class FragmentController {
    private static FragmentController instance;
    private FragmentManager currentFragmentManager;

    public static FragmentController getInstance() {
        if(instance == null) {
            instance = new FragmentController();
        }
        return instance;
    }

    public void startFragment(Fragment fragment, int replacedContentID,  String fragmentName) {
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        Log.d(LogTags.FRAGMENT_CONTROLLER, "Starting fragment transaction");
        FragmentTransaction transaction = currentFragmentManager.beginTransaction();
        transaction.replace(replacedContentID, fragment);
        transaction.addToBackStack(fragmentName);
        transaction.commit();
    }

    public void startFragment(Fragment fragment, int replacedContentID, String fragmentName, Bundle bundle) {
        fragment.setArguments(bundle);
        Log.d(LogTags.FRAGMENT_CONTROLLER, "Starting "+fragmentName+" fragment transaction");
        FragmentTransaction transaction = currentFragmentManager.beginTransaction();
        transaction.replace(replacedContentID, fragment);
        transaction.addToBackStack(fragmentName);
        transaction.commit();
    }

    public FragmentManager getCurrentFragmentManager() {
        return currentFragmentManager;
    }

    public void setCurrentFragmentManager(FragmentManager currentFragmentManager) {
        this.currentFragmentManager = currentFragmentManager;
    }
}
