package com.owoShopKeeperPanel.welcomeScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import com.daasuu.cat.CountAnimationTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.login.LogInActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private CountAnimationTextView mCountAnimationTextView;
    private int order_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            final WindowInsetsController insetsController = getWindow().getInsetsController();

            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars());
            }
        } else {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }

        mCountAnimationTextView = findViewById(R.id.count_animation_textView);

        FirebaseMessaging.getInstance().subscribeToTopic("e-commerce")
                .addOnCompleteListener(task -> {

                    String msg = "Subscribed";

                    if (!task.isSuccessful()) {
                        msg = "Subscription failed";
                    }
                    Log.d("FCM", msg);
                });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Shop Order Number").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    order_count = snapshot.getValue(Integer.class);

                    mCountAnimationTextView
                            .setAnimationDuration(1500)
                            .countAnimation(0, order_count);

                    new Handler(Looper.getMainLooper()).postDelayed(() ->
                    {
                        Intent mainIntent = new Intent(SplashScreenActivity.this, LogInActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                        finish();
                    }, 2000);
                }
                else {

                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        Intent mainIntent = new Intent(SplashScreenActivity.this, LogInActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                        finish();
                    }, 2000);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.e("Splash Screen", "Error is: "+error.getDetails());

                new Handler(Looper.getMainLooper()).postDelayed(() ->
                {
                    Intent mainIntent = new Intent(SplashScreenActivity.this, LogInActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                    finish();
                }, 2000);

            }
        });
    }
}
