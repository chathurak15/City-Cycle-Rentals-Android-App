package com.example.citycyclerentals;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.citycyclerentals.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private DBHelper dbHelper ;
    private ImageView profile , notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(20, systemBars.top, 20, systemBars.bottom);
                    return insets;
        });
        dbHelper = new DBHelper(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        profile = findViewById(R.id.profile_image);
        notification = findViewById(R.id.btn_notifications);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);  // Retrieve email from shared preferences
        UserModel userModel = dbHelper.getUserByEmail(email);

        if (userModel.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(userModel.getImage(), 0, userModel.getImage().length);
            profile.setImageBitmap(bitmap);
        } else {
            profile.setImageResource(R.drawable.profile_icon); // Default profile image
        }

        notification.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout,new PromotionFragment())
                    .commit();
        });

        profile.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, new ProfileFragment())
                    .commit();
        });

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout,new HomeFragment())
                    .commit();
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            System.out.println(itemId);
            if (itemId == R.id.home) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new HomeFragment())
                        .commit();
            } else if (itemId == R.id.search) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new SearchFragment())
                        .commit();
            } else if (itemId == R.id.rent) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new AllRentFragment())
                        .commit();
            }else if (itemId == R.id.profile) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new ProfileFragment())
                        .commit();
            }
            return true;
        });
    }
}