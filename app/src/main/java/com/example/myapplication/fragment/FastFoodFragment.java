package com.example.myapplication.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.FoodAdapter;
import com.example.myapplication.api.APIService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.model.Food;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FastFoodFragment extends Fragment {
    RecyclerView rcMenu;
    SearchView searchView;
    FoodAdapter foodAdapter;
    APIService apiService;
    List<Food> foodList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fastfoodfragment, container, false);
        AnhXa(view);
        setUpSearchView();
        setUpRecyclerView();
        getFood();
        return view;
    }

    private void setUpSearchView() {
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
    }

    private void setUpRecyclerView() {
        rcMenu.setHasFixedSize(true);
        rcMenu.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void filterList(String text) {
        List<Food> filteredList = new ArrayList<>();
        for (Food item : foodList) {
            if (item.getItemName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        foodAdapter.setFilteredList(filteredList);

    }

    private void AnhXa(View view) {
        rcMenu = view.findViewById(R.id.rc_food);
        searchView = view.findViewById(R.id.searchView);
    }

    private void getFood() {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);

        apiService.getFoodAll().enqueue(new Callback<List<Food>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<Food>> call, @NonNull Response<List<Food>> response) {
                if (response.isSuccessful()) {
                    foodList = response.body();
                    foodAdapter = new FoodAdapter(getActivity(), foodList);

                    RecyclerView.LayoutManager layoutManager =
                            new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

                    rcMenu.setLayoutManager(layoutManager);
                    rcMenu.setAdapter(foodAdapter);
                    rcMenu.setHasFixedSize(true);

                    foodAdapter.notifyDataSetChanged();
                    foodAdapter.setOnClickListener((position, model) -> {
                        Log.e("TAG", "onResponse: " + model.getId());
                    });
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
}
