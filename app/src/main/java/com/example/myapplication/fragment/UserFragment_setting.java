package com.example.myapplication.fragment;

import static com.example.myapplication.hashPassword.hashedPassword;

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
import com.example.myapplication.adapter.FoodAdapter;
import com.example.myapplication.adapter.UserAdapter;
import com.example.myapplication.api.APIService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.hashPassword;
import com.example.myapplication.model.Food;
import com.example.myapplication.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserFragment_setting extends Fragment {

    int ok = -1;
    private EditText username, password;
    private Button btnAdd, btnDelete;

    private CheckBox checkBoxManager, checkBoxNhanVien;
    RecyclerView rcUser;
    APIService apiService;
    List<User> userList;

    UserAdapter userAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_setting, container, false);
        AnhXa(view);
        getUser();
        setCheckBox();
        setAddUser();
        setDeleteUser();


        return view;
    }

    private void AnhXa(View view) {
        username = view.findViewById(R.id.edtUser);
        password = view.findViewById(R.id.edtPass);
        btnAdd = view.findViewById(R.id.btnAddUser);
        btnDelete = view.findViewById(R.id.btnDeleteUser);
        checkBoxManager = view.findViewById(R.id.checkBoxManager);
        checkBoxNhanVien = view.findViewById(R.id.checkBoxNhanVien);
        rcUser = view.findViewById(R.id.rcUser);
    }

    private void getUser(){
        apiService = RetrofitClient.getRetrofit().create(APIService.class);

        apiService.getUserAll().enqueue(new Callback<List<User>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.isSuccessful()) {
                    userList = response.body();
                    userAdapter = new UserAdapter(getActivity(), userList);

                    RecyclerView.LayoutManager layoutManager =
                            new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

                    rcUser.setLayoutManager(layoutManager);
                    rcUser.setAdapter(userAdapter);
                    rcUser.setHasFixedSize(true);

                    userAdapter.notifyDataSetChanged();
                    userAdapter.setOnClickListener((position, model) -> {

                        checkBox(model.getPosition());
                        ok = model.getId();
                        username.setText(model.getUsername());
                        Log.e("Setting", "onResponseUser: " + model.getUsername());

                    });
                } else {
                    int statusCode = response.code();
                    Log.e("SettingError", "onResponseUser: " + statusCode);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                Log.e("SettingError", "onFailureUser: " + t.getMessage());
            }
        });
    }

    private void setCheckBox() {
        checkBoxManager.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBoxNhanVien.setChecked(false);
                } else {
                    checkBoxNhanVien.setChecked(true);
                }
            }
        });

        checkBoxNhanVien.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBoxManager.setChecked(false);
                } else {
                    checkBoxManager.setChecked(true);
                }
            }
        });
    }

    private void setAddUser() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(username.getText().toString())){
                    Toast.makeText(getActivity(), "Nhập username", Toast.LENGTH_SHORT).show();
                    username.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(password.getText().toString())){
                    Toast.makeText(getActivity(), "Nhập password", Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                    return;
                }

                boolean checkToCreateNewUser = checkUserExist(username.getText().toString());

                if(checkToCreateNewUser) {
                    Toast.makeText(getActivity(), "Username này đã tồn tại", Toast.LENGTH_SHORT).show();
                    return;
                }

                apiService = RetrofitClient.getRetrofit().create(APIService.class);
                User user = new User(username.getText().toString(), hashedPassword(password.getText().toString()), checkPosition());
                apiService.postUser(user).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            getUser();
                            clearFocusAndText();
                            Log.d("Setting", "POST user thành công");
                        } else {
                            Log.e("Setting", "Error posting User: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e("Setting", "POST user thất bại", t);
                    }
                });

            }
        });
    }

    private boolean checkUserExist(String username) {
        for(User user : userList) {
            if(Objects.equals(user.getUsername(), username))
                return true;
        }
        return false;
    }

    private String checkPosition(){
        if (!checkBoxManager.isChecked() && !checkBoxNhanVien.isChecked()){
            Toast.makeText(getActivity(), "Vui lòng chọn chức vụ", Toast.LENGTH_SHORT).show();
        }
        else if (checkBoxManager.isChecked()){
            return "Manager";
        }
        else return "NhanVien";
        return "NhanVien";
    }


    private void setDeleteUser() {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(username.getText())){
                    Toast.makeText(getActivity(), "Chọn 1 user để xoá", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(username.getText().toString().equals("admin")){
                    Toast.makeText(getActivity(), "Không thể xoá user này", Toast.LENGTH_SHORT).show();
                    return;
                }

                apiService.deleteUser(ok).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            getUser();
                            clearFocusAndText();
                            Log.d("UserController", "User deleted successfully");
                        } else {
                            Log.e("UserController", "Error deleting User: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("FoodController", "Error deleting User", t);
                    }
                });

            }
        });


    }

    private void clearFocusAndText(){
        username.clearFocus();
        password.clearFocus();
        username.setText(null);
        password.setText(null);
    }

    private void checkBox(String s) {
        if (s.equals("Manager")) {
            checkBoxManager.setChecked(true);
        }
        else checkBoxNhanVien.setChecked(true);
    }


}