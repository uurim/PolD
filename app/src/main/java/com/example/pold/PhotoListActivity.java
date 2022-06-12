package com.example.pold;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class PhotoListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("사진보기");
    }
}