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
import com.example.myapplication.adapter.DrinkAdapter;
import com.example.myapplication.api.APIService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.model.Drink;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrinkFragment extends Fragment {
    SearchView searchView;
    RecyclerView rcMenu;

    DrinkAdapter drinkAdapter;
    APIService apiService;
    List<Drink> drinkList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_drinkfragment, container, false);
        AnhXa(view);
        setUpSearchView();
        setUpRecyclerView();
        getDrink();
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
                filterList2(newText);
                return true;
            }
        });
    }

    private void setUpRecyclerView() {

        rcMenu.setHasFixedSize(true);
        rcMenu.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void filterList2(String text) {

        List<Drink> filteredList2 = new ArrayList<>();
        for (Drink item : drinkList) {
            if (item.getItemName().toLowerCase().contains(text.toLowerCase())) {
                filteredList2.add(item);
            }
        }
        drinkAdapter.setFilteredList2(filteredList2);

    }

    private void AnhXa(View view) {

        rcMenu = view.findViewById(R.id.rc_drink);
        searchView = view.findViewById(R.id.searchView);
    }

    private void getDrink() {

        apiService = RetrofitClient.getRetrofit().create(APIService.class);

        apiService.getDrinkAll().enqueue(new Callback<List<Drink>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<Drink>> call, @NonNull Response<List<Drink>> response) {
                if (response.isSuccessful()) {
                    drinkList = response.body();
                    drinkAdapter = new DrinkAdapter(getActivity(), drinkList); // Sử dụng DrinkAdapter

                    RecyclerView.LayoutManager layoutManager =
                            new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

                    rcMenu.setLayoutManager(layoutManager);
                    rcMenu.setAdapter(drinkAdapter);
                    rcMenu.setHasFixedSize(true);

                    drinkAdapter.notifyDataSetChanged();
                    drinkAdapter.setOnClickListener((position, model) -> {
                        Log.e("TAG", "onResponse: " + model.getId());
                    });
                } else {
                    int statusCode = response.code();
                    Log.e("Error", "onResponse: " + statusCode);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Drink>> call, @NonNull Throwable t) {
                Log.e("Error", "onFailure: " + t.getMessage());
            }
        });
    }
}
