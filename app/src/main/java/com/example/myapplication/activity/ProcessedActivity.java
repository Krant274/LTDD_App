package com.example.myapplication.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.SharedPrefManager;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessedActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private Button BacktoMenu;

    private String hoaDon, time;
    private double totalAmount = 0.0; // Initialize totalAmount

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processed);


        // Mapping views
        AnhXa();

        // Get data from Intent
        Intent intent = getIntent();
        hoaDon = intent.getStringExtra("hoaDon");
        time = intent.getStringExtra("time");

        // Set up regex pattern to match items in the receipt
        String patternString = "Tên: (.*?)\\s*\\r?\\nGiá: ([\\d.]+)\\s*\\S*\\s*Số lượng: (\\d+)(?:\\s*\\r?\\n)?";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(hoaDon);

        // Process each matched item in the receipt
        while (matcher.find()) {
            try {
                String ten = matcher.group(1); // Group 1: Tên sản phẩm
                double gia = Double.parseDouble(matcher.group(2)); // Group 2: Giá
                int soLuong = Integer.parseInt(matcher.group(3)); // Group 3: Số lượng
                double subtotal = gia * soLuong; // Tính tổng tiền cho từng mặt hàng
                addTableRow(ten, soLuong, gia, subtotal); // Thêm dòng vào bảng
                totalAmount += subtotal; // Cộng dồn tổng tiền
            } catch (NumberFormatException e) {
                Log.e("ProcessedActivity", "Error parsing hoaDon string", e);
            }
        }

        // Extract total amount if present in the string
        Pattern totalPattern = Pattern.compile("Tổng tiền: ([\\d.]+)");
        Matcher totalMatcher = totalPattern.matcher(hoaDon);
        if (totalMatcher.find()) {
            try {
                totalAmount = Double.parseDouble(totalMatcher.group(1)); // Lấy tổng tiền
            } catch (NumberFormatException e) {
                Log.e("ProcessedActivity", "Error parsing total amount", e);
            }
        }

        // Add a row for total amount with empty quantity and subtotal columns
        addTableRowBold("Tổng tiền", "", "", totalAmount);

        // Set click listener for BacktoMenu button
        BacktoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPositionAndReturn();
            }
        });
    }

    private void AnhXa() {
        tableLayout = findViewById(R.id.tableLayout_processed);
        BacktoMenu = findViewById(R.id.btn_btMenu);
    }

    private void addTableRow(String ten, int soLuong, double gia, double subtotal) {
        TableRow tableRow = new TableRow(this);

        TextView tenView = new TextView(this);
        tenView.setText(ten);
        tenView.setPadding(8, 8, 8, 8);

        TextView soLuongView = new TextView(this);
        soLuongView.setText(String.valueOf(soLuong));
        soLuongView.setPadding(8, 8, 8, 8);

        TextView giaView = new TextView(this);
        giaView.setText(String.valueOf(gia));
        giaView.setPadding(8, 8, 8, 8);

        TextView subtotalView = new TextView(this);
        subtotalView.setText(String.valueOf(subtotal));
        subtotalView.setPadding(8, 8, 8, 8);

        tableRow.addView(tenView);
        tableRow.addView(soLuongView);
        tableRow.addView(giaView);
        tableRow.addView(subtotalView);

        tableLayout.addView(tableRow);
    }

    private void addTableRowBold(String ten, String soLuong, String gia, double totalAmount) {
        TableRow tableRow = new TableRow(this);

        TextView tenView = new TextView(this);
        tenView.setText(ten);
        tenView.setPadding(8, 8, 8, 8);
        tenView.setTypeface(null, Typeface.BOLD);

        TextView soLuongView = new TextView(this);
        soLuongView.setText(soLuong);
        soLuongView.setPadding(8, 8, 8, 8);

        TextView giaView = new TextView(this);
        giaView.setText(gia);
        giaView.setPadding(8, 8, 8, 8);

        TextView totalAmountView = new TextView(this);
        totalAmountView.setText(String.valueOf(totalAmount));
        totalAmountView.setPadding(8, 8, 8, 8);

        tableRow.addView(tenView);
        tableRow.addView(soLuongView);
        tableRow.addView(giaView);
        tableRow.addView(totalAmountView);

        tableLayout.addView(tableRow);
    }

    private void checkPositionAndReturn() {
        String userPosition =  SharedPrefManager.getInstance(ProcessedActivity.this).getUserPosition();
        if(Objects.equals(userPosition, "Manager")) {
            Intent intent = new Intent(ProcessedActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(ProcessedActivity.this, MainActivity2.class);
            startActivity(intent);
            finish();
        }
    }
}
