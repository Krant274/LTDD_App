package com.example.myapplication.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.activity.ProcessingActivity;
import com.example.myapplication.R;
import com.example.myapplication.SharedPrefManager;
import com.example.myapplication.api.APIService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.model.DangXuLi;
import com.example.myapplication.model.Drink;
import com.example.myapplication.model.Food;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuFragment extends Fragment {
    private ImageButton btnFastFood;
    private ImageButton btnDrink;
    private Button btnConfirm;
    private List<Drink> drinkList;
    private List<Food> foodList;
    APIService apiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_menu_fragment, container, false);

        get_food_drink_list();
        AnhXa(view);
        setButton();

        return view;
    }

    private void AnhXa(View view) {
        btnFastFood= view.findViewById(R.id.iBtnFastFood);
        btnDrink = view.findViewById(R.id.iBtnDrink);
        btnConfirm=view.findViewById(R.id.btn_confirm);
        switchFragment(new FastFoodFragment());
    }

    private void setButton(){

        btnFastFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(new FastFoodFragment());
            }
        });

        btnDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(new DrinkFragment());
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() == null) {
                    return;
                }

                setQuantity();

                if (drinkList == null) {
                    Log.e("drink", "null" );
                }
                else{
                    String hoaDon = bill(foodList, drinkList);

                    if(hoaDon.equals("false")) {
                        Toast.makeText(getActivity(), "Vui lòng chọn sản phẩm trước khi bấm xác nhận", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SharedPrefManager.getInstance(getContext()).clearAllQuantity();
                    PostDonHang(hoaDon);
                }
            }
        });
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.menu_container, fragment); // Use replace() instead of add()
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void get_food_drink_list() {
        getFood();
        getDrink();
    }

    private void getFood() {
        apiService = RetrofitClient.getRetrofit()
                .create(APIService.class);

        apiService.getFoodAll().enqueue(new Callback<List<Food>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<Food>> call, @NonNull Response<List<Food>> response) {
                if (response.isSuccessful()) {
                    foodList = response.body();
                } else {
                    int statusCode = response.code();
                    Log.e("Error", "onResponse: " + statusCode);
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Food>> call, @NonNull Throwable t) {
                Log.e("Error", "onFailure: " + t.getMessage());
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

                } else {
                    int statusCode = response.code();
                    Log.e("Error", "onResponse: " + statusCode);
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Drink>> call, @NonNull Throwable t
            ) {
                Log.e("Error", "onFailure: " + t.getMessage());
            }
        });
    }

    private String bill(List<Food> foodList1, List<Drink> drinkList1) {
        String hoaDon = "";
        float total = 0;
        for (Food item : foodList1) {

            if (item.getQuantity() != 0) {
                hoaDon += "Tên: " + item.getName() + "\nGiá: " + item.getPrice().toString() + " \tSố lượng: " + item.getQuantity() + "\n\n";
                total += (float) (item.getPrice()* item.getQuantity());
            }
        }

        for (Drink item : drinkList1) {
            if (item.getQuantity() != 0) {
                hoaDon += "Tên: " + item.getName() + "\nGiá: " + item.getPrice().toString() + " \tSố lượng: " + item.getQuantity() + "\n\n";
                total += (float) (item.getPrice()* item.getQuantity());
            }
        }

        if(total == 0){
            return "false";
        }
        hoaDon += "Tổng tiền: " + total;
        return hoaDon;
    }

    private void PostDonHang(String hoaDon) {

        DangXuLi dangXuLi = new DangXuLi(hoaDon);

        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.postDangXuLi(dangXuLi).enqueue(new Callback<DangXuLi>() {
            @Override
            public void onResponse(Call<DangXuLi> call, Response<DangXuLi> response) {
                int id = response.body().getId();
                Log.e("POST", "POST hoá đơn thành công" + response.body().getId());

                Intent intent = new Intent(getContext(), ProcessingActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("hoaDon", hoaDon);
                startActivity(intent);
                getActivity().finish();

            }

            @Override
            public void onFailure(Call<DangXuLi> call, Throwable t) {
                Log.e("POST", "POST hoá đơn thất bại");
            }
        });
    }

    private void setQuantity() {
        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(getContext());
        for (int i = 0; i < drinkList.size(); i++) {
            int quantity = sharedPrefManager.loadDrinkQuantity(i+1);
            if(quantity != 0) {
                drinkList.get(i).setQuantity(quantity);
            }
        }

        for (int i = 0; i < foodList.size(); i++) {
            int quantity = sharedPrefManager.loadFoodQuantity(i+1);
            if(quantity != 0) {
                foodList.get(i).setQuantity(quantity);
            }
        }

    }


}
