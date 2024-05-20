package com.example.myapplication.api;

import com.example.myapplication.model.DangXuLi;
import com.example.myapplication.model.Drink;
import com.example.myapplication.model.Food;
import com.example.myapplication.model.HoanTat;
import com.example.myapplication.model.User;


import java.util.List;

import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService {

    @GET("food")
    Call<List<Food>> getFoodAll();

    @POST("food")
    Call<Food> postFood(@Body Food food);

    @DELETE("food/{id}")
    Call<Void> deleteFood(@Path("id") int id);

    @GET("drink")
    Call<List<Drink>> getDrinkAll();

    @POST("drink")
    Call<Drink> postDrink(@Body Drink drink);

    @DELETE("drink/{id}")
    Call<Void> deleteDrink(@Path("id") int id);

    @GET("processing")
    Call<List<DangXuLi>> getProcessingAll();

    @POST("processing")
    Call<DangXuLi> postDangXuLi(@Body DangXuLi dangXuLi);

    @DELETE("processing/{id}")
    Call<Void> deleteDangXuLi(@Path("id") int id);
    @GET("processed")
    Call<List<HoanTat>> getProcessedAll();

    @POST("processed")
    Call<HoanTat> postDaHoanTat(@Body HoanTat hoanTat);
    @GET("user")
    Call<List<User>> getUserAll();
    @POST("user")
    Call<User> postUser(@Body User newUser);

    @DELETE("user/{id}")
    Call<Void> deleteUser(@Path("id") int id);


}
