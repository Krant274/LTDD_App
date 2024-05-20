package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.myapplication.activity.LoginActivity;

public class SharedPrefManager {
    private static final String SHARE_PREF_NAME = "menuFoodDrink";
    private static final String QUANTITY_KEY_PREFIX_FOOD = "Food_Quantity_  ";
    private static final String QUANTITY_KEY_PREFIX_DRINK = "Drink_Quantity_";
    private static final String KEY_POSITION = "staff_position";

    private static SharedPrefManager mInstance;
    private SharedPreferences sharedPreferences;

    private static Context ctx;

    private SharedPrefManager(Context context) {
        ctx = context;
        sharedPreferences = context.getSharedPreferences(SHARE_PREF_NAME, Context.MODE_PRIVATE);
    }



    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void saveFoodQuantity(int id, int quantity) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(QUANTITY_KEY_PREFIX_FOOD + id, quantity);
        editor.apply(); // Sử dụng apply() thay vì commit() để không chặn UI thread
    }

    public void saveDrinkQuantity(int id, int quantity) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(QUANTITY_KEY_PREFIX_DRINK + id, quantity);
        editor.apply();
    }

    public int loadFoodQuantity(int id) {
        return sharedPreferences.getInt(QUANTITY_KEY_PREFIX_FOOD + id, 0);
    }

    public int loadDrinkQuantity(int id) {
        return sharedPreferences.getInt(QUANTITY_KEY_PREFIX_DRINK + id, 0);
    }

    public void clearAllData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        ctx.startActivity(new Intent(ctx, LoginActivity.class));
    }

    public void clearAllQuantity() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (String key : sharedPreferences.getAll().keySet()) {
            if (key.startsWith(QUANTITY_KEY_PREFIX_FOOD) || key.startsWith(QUANTITY_KEY_PREFIX_DRINK)) {
                editor.remove(key);
            }
        }
        editor.apply();
    }

    public void userPosition (String userPosition) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_POSITION, userPosition);
        editor.apply();
    }

    public String isLoggedIn() {
        return sharedPreferences.getString(KEY_POSITION, null);
    }

    public String getUserPosition() {
        return sharedPreferences.getString(KEY_POSITION, null);
    }



}
