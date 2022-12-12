package com.example.firebase_auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Admin_panel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        getSupportActionBar().hide();
    }
}