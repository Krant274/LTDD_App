package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;
import com.example.myapplication.fragment.ProcessedFragment;
import com.example.myapplication.fragment.ProcessingFragment;

public class DonActivity extends AppCompatActivity {
    private ImageButton btnProcessing;
    private ImageButton btnProcessed;
    private Button btn_backtomainp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_don);

        AnhXa();

        switchFragment(new ProcessingFragment());

        setButton();

    }

    private void setButton() {
        btnProcessing.setOnClickListener(v -> switchFragment(new ProcessingFragment()));
        btnProcessed.setOnClickListener(v -> switchFragment(new ProcessedFragment()));
        btn_backtomainp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DonActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void AnhXa() {
        btnProcessed = findViewById(R.id.iBtnProcessed);
        btnProcessing = findViewById(R.id.iBtnProcessing);
        btn_backtomainp=findViewById(R.id.btn_backtomainpage);
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.don_container, fragment);
        fragmentTransaction.commit();
    }
}