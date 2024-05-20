package com.example.myapplication.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.DrinkAdapter;
import com.example.myapplication.adapter.FoodAdapter;
import com.example.myapplication.api.APIService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.model.Drink;
import com.example.myapplication.model.Food;
import com.example.myapplication.model.HoanTat;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodnDrinkFragment_setting extends Fragment {

    private int ok = -1;
    private String type; // type = {'food', 'drink'}
    private Button btnAdd, btnDelete, btnEdit;
    private EditText editTextNhapMon, editTextNhapGia, editTextUrl;

    private CheckBox checkBoxFood, checkBoxDrink;
    private RecyclerView rcFood, rcDrink;

    DrinkAdapter drinkAdapter;
    APIService apiService;
    List<Drink> drinkList;
    FoodAdapter foodAdapter;
    List<Food> foodList;
    Food food;
    Drink drink;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_foodndrink_setting, container, false);
        AnhXa(view);
        getFood();
        getDrink();
        setCheckBox();
        delete();
        add();
        edit();

        return view;
    }

    private void AnhXa(View view)
    {
        btnAdd = view.findViewById(R.id.btnThem);
        btnDelete = view.findViewById(R.id.btnDelete);
        btnEdit = view.findViewById(R.id.btnEdit);
        editTextNhapMon = view.findViewById(R.id.edtNhapMon);
        editTextNhapGia = view.findViewById(R.id.edtNhapGia);
        editTextUrl = view.findViewById(R.id.edtUrl);
        checkBoxFood = view.findViewById(R.id.checkBoxFood);
        checkBoxDrink = view.findViewById(R.id.checkBoxDrink);
        rcFood = view.findViewById(R.id.rcFood_setting);
        rcDrink = view.findViewById(R.id.rcDrink_setting);
    }

    private void getFood() {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);

        apiService.getFoodAll().enqueue(new Callback<List<Food>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<Food>> call, @NonNull Response<List<Food>> response) {
                if (response.isSuccessful()) {
                    foodList = response.body();
                    foodAdapter = new FoodAdapter(getActivity(), foodList, true);

                    RecyclerView.LayoutManager layoutManager =
                            new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

                    rcFood.setLayoutManager(layoutManager);
                    rcFood.setAdapter(foodAdapter);
                    rcFood.setHasFixedSize(true);

                    foodAdapter.notifyDataSetChanged();
                    foodAdapter.setOnClickListener((position, model) -> {
                        food = model;
                        ok = model.getId();
                        type = "food";
                        Log.e("Setting", "onResponseFood: " + ok);
                        editTextNhapMon.setText(model.getName());
                        editTextNhapGia.setText(String.valueOf(model.getPrice()));
                        editTextUrl.setText(model.getUrlImage());
                        checkBoxFood.setChecked(true);
                    });
                } else {
                    int statusCode = response.code();
                    Log.e("SettingError", "onResponseFood: " + statusCode);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Food>> call, @NonNull Throwable t) {
                Log.e("SettingError", "onFailureFood: " + t.getMessage());
            }
        });
    }

    private void getDrink() {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);

        apiService.getDrinkAll().enqueue(new Callback<List<Drink>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<Drink>> call, @NonNull Response<List<Drink>> response) {
                if (response.isSuccessful()) {
                    drinkList = response.body();
                    drinkAdapter = new DrinkAdapter(getActivity(), drinkList, true);

                    RecyclerView.LayoutManager layoutManager =
                            new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    rcDrink.setHasFixedSize(true);
                    rcDrink.setLayoutManager(layoutManager);
                    rcDrink.setAdapter(drinkAdapter);

                    drinkAdapter.notifyDataSetChanged();
                    drinkAdapter.setOnClickListener((position, model) -> {
                        drink = model;
                        ok = model.getId();
                        type = "drink";
                        Log.e("Setting", "onResponseDrink: " + ok);
                        editTextNhapMon.setText(model.getName());
                        editTextNhapGia.setText(String.valueOf(model.getPrice()));
                        editTextUrl.setText(model.getUrlImage());
                        checkBoxDrink.setChecked(true);
                    });
                } else {
                    int statusCode = response.code();
                    Log.e("SettingError", "onResponseDrink: " + statusCode);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Drink>> call, @NonNull Throwable t) {
                Log.e("SettingError", "onFailureDrink: " + t.getMessage());
            }
        });
    }

    private void setCheckBox() {
        checkBoxFood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBoxFood.isChecked()) {
                    checkBoxDrink.setChecked(false);
                } else {
                    checkBoxDrink.setChecked(true);
                }
            }
        });

        checkBoxDrink.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBoxDrink.isChecked()) {
                    checkBoxFood.setChecked(false);
                } else {
                    checkBoxFood.setChecked(true);
                }
            }
        });
    }

    private void delete(){
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkEditTextEmpty()) {
                    return;
                }
                if(Objects.equals(type, "food")) {
                    apiService.deleteFood(ok).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                getFood();
                                clearFocusAndText();
                                Log.d("FoodController", "Food deleted successfully");
                            } else {
                                Log.e("FoodController", "Error deleting Food: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("FoodController", "Error deleting Food", t);
                        }
                    });
                }
                else if (Objects.equals(type, "drink")) {
                    apiService.deleteDrink(ok).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                getDrink();
                                clearFocusAndText();
                                Log.d("DrinkController", "Drink deleted successfully");
                            } else {
                                Log.e("DrinkController", "Error deleting Drink: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("DrinkController", "Error deleting Drink", t);
                        }
                    });
                }
                else{
                    Toast.makeText(getActivity(), "Hãy chọn 1 sản phẩm để xoá", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void add(){
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkEditTextEmpty()) {
                    return;
                }

                if (checkBoxFood.isChecked()){
                    Food newFood = new Food(editTextNhapMon.getText().toString(), Double.valueOf(editTextNhapGia.getText().toString()), editTextUrl.getText().toString());
                    apiService.postFood(newFood).enqueue(new Callback<Food>() {
                        @Override
                        public void onResponse(Call<Food> call, Response<Food> response) {
                            if (response.isSuccessful()) {
                                getFood();
                                clearFocusAndText();
                                Log.d("Setting", "POST food thanh cong");
                            } else {
                                Log.e("Setting", "Error posting Food: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<Food> call, Throwable t) {
                            Log.e("Setting", "POST food that bai", t);
                        }
                    });
                }
                else if (checkBoxDrink.isChecked()) {
                    Drink newDrink = new Drink(editTextNhapMon.getText().toString(), Double.valueOf(editTextNhapGia.getText().toString()), editTextUrl.getText().toString());
                    apiService.postDrink(newDrink).enqueue(new Callback<Drink>() {
                        @Override
                        public void onResponse(Call<Drink> call, Response<Drink> response) {
                            if (response.isSuccessful()) {
                                getDrink();
                                clearFocusAndText();
                                Log.d("Setting", "POST drink thanh cong");
                            } else {
                                Log.e("Setting", "Error posting drink: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<Drink> call, Throwable t) {
                            Log.e("Setting", "POST drink that bai", t);
                        }
                    });
                }
                else{
                    Toast.makeText(getActivity(), "Hãy chọn loại sản phẩm muốn thêm ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void edit() {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkEditTextEmpty()) {
                    return;
                }

                if (checkBoxFood.isChecked()){

                    Food newFood = new Food(editTextNhapMon.getText().toString(), Double.valueOf(editTextNhapGia.getText().toString()), editTextUrl.getText().toString());
                    apiService.postFood(newFood).enqueue(new Callback<Food>() {
                        @Override
                        public void onResponse(Call<Food> call, Response<Food> response) {
                            if (response.isSuccessful()) {
                                getFood();
                                clearFocusAndText();
                                Log.d("Setting", "Edit food thành công");
                            } else {
                                Log.e("Setting", "Error editting Food: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<Food> call, Throwable t) {
                            Log.e("Setting", "Edit food thất bại", t);
                        }
                    });
                }
                else if (checkBoxDrink.isChecked()) {
                    Drink newDrink = new Drink(editTextNhapMon.getText().toString(), Double.valueOf(editTextNhapGia.getText().toString()),editTextUrl.getText().toString());
                    apiService.postDrink(newDrink).enqueue(new Callback<Drink>() {
                        @Override
                        public void onResponse(Call<Drink> call, Response<Drink> response) {
                            if (response.isSuccessful()) {
                                getDrink();
                                clearFocusAndText();
                                Log.d("Setting", "Edit drink thành công");
                            } else {
                                Log.e("Setting", "Error posting drink: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<Drink> call, Throwable t) {
                            Log.e("Setting", "Edit drink thất bại", t);
                        }
                    });
                }
                else{
                    Toast.makeText(getActivity(), "Hãy chọn loại sản phẩm muốn chỉnh sửa ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void clearFocusAndText(){
        editTextNhapMon.clearFocus();
        editTextNhapGia.clearFocus();
        editTextUrl.clearFocus();
        editTextNhapMon.setText(null);
        editTextNhapGia.setText(null);
        editTextUrl.setText(null);
    }

    private boolean checkEditTextEmpty(){
        if (TextUtils.isEmpty(editTextNhapMon.getText())){
           editTextNhapMon.requestFocus();
            Toast.makeText(getActivity(), "Hãy chọn món", Toast.LENGTH_SHORT).show();
           return true;
        }

        if (TextUtils.isEmpty(editTextNhapGia.getText())){
            editTextNhapGia.requestFocus();
            Toast.makeText(getActivity(), "Hãy chọn giá", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (TextUtils.isEmpty(editTextUrl.getText())){
            editTextUrl.requestFocus();
            Toast.makeText(getActivity(), "Hãy chọn hình ảnh", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

}