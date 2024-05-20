package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.fragment.FoodnDrinkFragment_setting;
import com.example.myapplication.fragment.UserFragment_setting;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Setting extends AppCompatActivity {

    Button btnBack;
    Button btnUser;
    Button btnFoodnDrink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        AnhXa();
        setButton();
        switchFragment(new FoodnDrinkFragment_setting());
    }

    private void AnhXa(){
        btnUser = findViewById(R.id.btnUser);
        btnFoodnDrink = findViewById(R.id.btnFoodnDrink);
        btnBack=findViewById(R.id.btnBack);
    }

    private void setButton(){

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(new UserFragment_setting());
            }
        });

        btnFoodnDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(new FoodnDrinkFragment_setting());
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame_setting, fragment);
        fragmentTransaction.commit();
    }
}