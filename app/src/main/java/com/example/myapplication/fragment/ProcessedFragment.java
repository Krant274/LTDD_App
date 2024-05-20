package com.example.myapplication.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.activity.ProcessedActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.ProcessedAdapter;
import com.example.myapplication.api.APIService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.model.HoanTat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProcessedFragment extends Fragment {
    RecyclerView rcProcessed;
    APIService apiService;
    ProcessedAdapter processedAdapter;
    List<HoanTat> hoanTatList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_processed, container, false);

        AnhXa(view);
        getProcessed();

        return view;
    }

    private void AnhXa(View view) {
        rcProcessed = view.findViewById(R.id.rc_processed);
    }

    private void getProcessed(){
        apiService = RetrofitClient.getRetrofit().create(APIService.class);

        apiService.getProcessedAll().enqueue(new Callback<List<HoanTat>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<HoanTat>> call, @NonNull Response<List<HoanTat>> response) {
                if (response.isSuccessful()) {
                    hoanTatList = response.body();
                    processedAdapter = new ProcessedAdapter(getActivity(), hoanTatList);

                    RecyclerView.LayoutManager layoutManager =
                            new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    rcProcessed.setHasFixedSize(true);
                    rcProcessed.setLayoutManager(layoutManager);
                    rcProcessed.setAdapter(processedAdapter);

                    processedAdapter.notifyDataSetChanged();
                    processedAdapter.setOnClickListener((position, model) -> {
                        Log.e("onProcessed", "onResponse: " + model.getId());
                        Intent intent = new Intent(getContext(), ProcessedActivity.class);
                        intent.putExtra("id", model.getId());
                        intent.putExtra("hoaDon", model.getDonHang());
                        intent.putExtra("time", model.getLastUpdate());
                        startActivity(intent);

                    });
                } else {
                    int statusCode = response.code();
                    Log.e("Error", "onResponse: " + statusCode);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<HoanTat>> call, @NonNull Throwable t) {
                Log.e("Error", "onFailure: " + t.getMessage());
            }
        });
    }

}