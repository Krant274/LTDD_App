package com.example.myapplication.activity;

import static com.example.myapplication.hashPassword.hashedPassword;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.R;
import com.example.myapplication.SharedPrefManager;
import com.example.myapplication.adapter.PhotoViewPagerAdapter;
import com.example.myapplication.api.APIService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.model.Photo;
import com.example.myapplication.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail;
    EditText editTextpass;
    CheckBox checkBoxShowPassword;
    APIService apiService;
    private ViewPager mViewPager;
    private CircleIndicator mCircleIndicator;
    private List<Photo> mListPhoto;
    private PhotoViewPagerAdapter mAdapter;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private static final long AUTO_SWIPE_INTERVAL = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        isLoggedIn();

        editTextEmail = findViewById(R.id.editTextTextEmailAddress);
        editTextpass = findViewById(R.id.editTextTextPassword);
        ImageButton img1 = findViewById(R.id.imageButton1);
        checkBoxShowPassword = findViewById(R.id.checkBox3);

        // Khởi tạo RetrofitClient và APIService
        apiService = RetrofitClient.getRetrofit().create(APIService.class);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String password = editTextpass.getText().toString();

                // Băm password bằng MD5
                String hashedPassword = hashedPassword(password);

                // Gọi phương thức kiểm tra thông tin đăng nhập
                loginUser(email, hashedPassword);
            }
        });

        checkBoxShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editTextpass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    editTextpass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        mViewPager = findViewById(R.id.view_pager);
        mCircleIndicator = findViewById(R.id.circle_indicator);
        mListPhoto = getListPhoto();
        mAdapter = new PhotoViewPagerAdapter(mListPhoto);
        mViewPager.setAdapter(mAdapter);
        mCircleIndicator.setViewPager(mViewPager);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
    }
    @Override
    protected void onStart() {
        super.onStart();
        startAutoSwipe();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopAutoSwipe();
    }

    private void startAutoSwipe() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = mViewPager.getCurrentItem();
                        int totalItems = mAdapter.getCount();
                        int nextItem = (currentItem + 1) % totalItems;
                        mViewPager.setCurrentItem(nextItem);
                    }
                });
            }
        };
        mTimer.schedule(mTimerTask, 3000, 3000);
    }

    private void stopAutoSwipe() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    private List<Photo> getListPhoto() {
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.img));
        list.add(new Photo(R.drawable.img_1));
        list.add(new Photo(R.drawable.img_2));
        list.add(new Photo(R.drawable.img_3));
        return list;
    }
    // Phương thức để kiểm tra thông tin đăng nhập
    private void loginUser(String email, String hashedPassword) {
        apiService.getUserAll().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    List<User> userList = response.body();
                    boolean isValidUser = false;
                    String userPosition = "";

                    // Kiểm tra xem email và mật khẩu có khớp với thông tin của bất kỳ user nào không
                    for (User user : userList) {
                        if (user.getUsername().equals(email) && user.getHashedPassword().equals(hashedPassword)) {
                            isValidUser = true;
                            userPosition = user.getPosition();
                            break;
                        }
                    }

                    if (isValidUser) {
                        // Đăng nhập thành công
                        Toast.makeText(getApplicationContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        //
                        SharedPrefManager.getInstance(getApplicationContext()).userPosition(userPosition);
                        // Chuyển đến màn hình chính dựa trên vị trí của người dùng
                        if (userPosition.equals("Manager")) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (userPosition.equals("NhanVien")) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        // Đăng nhập thất bại
                        Toast.makeText(getApplicationContext(), "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    int statusCode = response.code();
                    Log.e("Error", "onResponse: " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("Error", "onFailure: " + t.getMessage());
            }
        });
    }

    private void isLoggedIn(){
        if (Objects.equals(SharedPrefManager.getInstance(this).isLoggedIn(), "Manager")) {
            Log.e("TAG", "inLoggedIn Manager");
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else if (Objects.equals(SharedPrefManager.getInstance(this).isLoggedIn(), "NhanVien")) {
            Log.e("TAG", "inLoggedIn NhanVien");
            startActivity(new Intent(this, MainActivity2.class));
            finish();
        }
    }
}
