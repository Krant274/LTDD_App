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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.SharedPrefManager;
import com.example.myapplication.api.APIService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.model.HoanTat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcessingActivity extends AppCompatActivity {
    private Button BacktoMenu;
    private Button btnConfirmProcess;
    private Button btnDeleteBill;
    private int id;
    private String hoaDon;
    private TableLayout tableLayout;
    private double totalAmount;
    APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_processing);

        AnhXa();
        setButton();

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        hoaDon = intent.getStringExtra("hoaDon");
        Log.d("ProcessingActivity", "Received hoaDon: " + hoaDon);

        String patternString = "Tên: (.*?)\\s*\\r?\\nGiá: ([\\d.]+)\\s*\\S*\\s*Số lượng: (\\d+)(?:\\s*\\r?\\n)?";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(hoaDon);

        while (matcher.find()) {
            Log.d("ProcessingActivity", "Pattern matched successfully.");
            try {
                String ten = matcher.group(1); // Group 1
                double gia = Double.parseDouble(matcher.group(2)); // Group 2
                int soLuong = Integer.parseInt(matcher.group(3)); // Group 3
                double thanhTien = gia * soLuong;

                addTableRow(ten, String.valueOf(soLuong), String.valueOf(gia), String.valueOf(thanhTien));
                totalAmount += gia * soLuong;
            } catch (NumberFormatException e) {
                Log.e("ProcessingActivity", "Error parsing hoaDon string", e);
            }
        }

        // Extract total amount if present in the string
        Pattern totalPattern = Pattern.compile("Tổng tiền: ([\\d.]+)");
        Matcher totalMatcher = totalPattern.matcher(hoaDon);
        if (totalMatcher.find()) {
            try {
                totalAmount = Double.parseDouble(totalMatcher.group(1));
            } catch (NumberFormatException e) {
                Log.e("ProcessingActivity", "Error parsing total amount", e);
            }
        }

        // Add a row for total amount with empty quantity column
        addTableRowBold("Tổng tiền", "","", String.valueOf(totalAmount));
    }
    private void AnhXa(){
        tableLayout = findViewById(R.id.tableLayout);
        BacktoMenu=findViewById(R.id.btn_btMenu);
        btnConfirmProcess=findViewById(R.id.btn_ConfirmProcess);
        btnDeleteBill=findViewById(R.id.btn_delete);
    }
    private void addTableRow(String ten, String soLuong, String gia, String subtotal) {
        TableRow tableRow = new TableRow(this);

        TextView tenView = new TextView(this);
        tenView.setText(ten);
        tenView.setPadding(8, 8, 8, 8);

        TextView soLuongView = new TextView(this);
        soLuongView.setText(soLuong);
        soLuongView.setPadding(8, 8, 8, 8);

        TextView giaView = new TextView(this);
        giaView.setText(gia);
        giaView.setPadding(8, 8, 8, 8);

        TextView subtotalView = new TextView(this);
        subtotalView.setText(subtotal);
        subtotalView.setPadding(8, 8, 8, 8);

        tableRow.addView(tenView);
        tableRow.addView(soLuongView);
        tableRow.addView(giaView);
        tableRow.addView(subtotalView);

        tableLayout.addView(tableRow);
    }
    //IN hoa Tong
    private void addTableRowBold(String ten, String soLuong, String gia, String subtotal) {
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

        TextView subtotalView = new TextView(this);
        subtotalView.setText(subtotal);
        subtotalView.setPadding(8, 8, 8, 8);

        tableRow.addView(tenView);
        tableRow.addView(soLuongView);
        tableRow.addView(giaView);
        tableRow.addView(subtotalView);

        tableLayout.addView(tableRow);
    }

    private void setButton() {

        BacktoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPositionAndReturn();
            }
        });

        btnDeleteBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delBill(id);
                checkPositionAndReturn();
            }
        });

        btnConfirmProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delBill(id);
                PostToProcessed();
                checkPositionAndReturn();
            }
        });
    }

    private void delBill(int id){
        Log.d("DangXuLiController", "Deleting DangXuLi with ID: " + id);
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.deleteDangXuLi(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("DangXuLiController", "DangXuLi deleted successfully");
                } else {
                    Log.e("DangXuLiController", "Error deleting DangXuLi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("DangXuLiController", "Error deleting DangXuLi", t);
            }
        });
    }

    private void PostToProcessed() {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = formatter.format(now);

        HoanTat donHoanTat = new HoanTat(id, formattedDateTime, hoaDon);

        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.postDaHoanTat(donHoanTat).enqueue(new Callback<HoanTat>() {
            @Override
            public void onResponse(Call<HoanTat> call, Response<HoanTat> response) {
                if (response.isSuccessful()) {
                    Log.d("DaHoanTat", "POST thanh cong");
                } else {
                    // Handle unsuccessful deletion (e.g., show an error message)
                    Log.e("DaHoanTat", "Error posting DaHoanTat: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<HoanTat> call, Throwable t) {
                Log.e("DaHoanTat", "POST that bai", t);
            }
        });
    }

    private void checkPositionAndReturn() {
        String userPosition =  SharedPrefManager.getInstance(ProcessingActivity.this).getUserPosition();
        if(Objects.equals(userPosition, "Manager")) {
            Intent intent = new Intent(ProcessingActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(ProcessingActivity.this, MainActivity2.class);
            startActivity(intent);
            finish();
        }
    }

}