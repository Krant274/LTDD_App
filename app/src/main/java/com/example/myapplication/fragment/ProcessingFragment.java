package com.example.myapplication.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.activity.ProcessingActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.ProcessingAdapter;
import com.example.myapplication.api.APIService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.model.DangXuLi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcessingFragment extends Fragment {
    RecyclerView rcProcessing;
    APIService apiService;
    ProcessingAdapter processingAdapter;
    List<DangXuLi> dangXuLiList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_processing_fragment, container, false);
        AnhXa(view);
        getProcessing();

        return view;
    }

    private void AnhXa(View view) {
        rcProcessing = view.findViewById(R.id.rc_processing);
    }

    private void getProcessing(){
        apiService = RetrofitClient.getRetrofit().create(APIService.class);

        apiService.getProcessingAll().enqueue(new Callback<List<DangXuLi>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<DangXuLi>> call, @NonNull Response<List<DangXuLi>> response) {
                if (response.isSuccessful()) {
                    dangXuLiList = response.body();
                    processingAdapter = new ProcessingAdapter(getActivity(), dangXuLiList);

                    RecyclerView.LayoutManager layoutManager =
                            new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    rcProcessing.setHasFixedSize(true);
                    rcProcessing.setLayoutManager(layoutManager);
                    rcProcessing.setAdapter(processingAdapter);

                    processingAdapter.notifyDataSetChanged();
                    processingAdapter.setOnClickListener((position, model) -> {
                        Log.e("onProcessing", "onResponse: " + model.getId());
                        Intent intent = new Intent(getContext(), ProcessingActivity.class);
                        intent.putExtra("id", model.getId());
                        intent.putExtra("hoaDon", model.getDonHang());
                        startActivity(intent);
                    });
                } else {
                    int statusCode = response.code();
                    Log.e("Error", "onResponse: " + statusCode);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<DangXuLi>> call, @NonNull Throwable t) {
                Log.e("Error", "onFailure: " + t.getMessage());
            }
        });
    }
}