package com.owoShopKeeperPanel.shopKeeperPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;

import com.daasuu.cat.CountAnimationTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.owoShopKeeperPanel.R;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView splash_logo;
    private CountAnimationTextView mCountAnimationTextView;
    private int order_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        splash_logo = findViewById(R.id.splash_logo);

        mCountAnimationTextView = (CountAnimationTextView) findViewById(R.id.count_animation_textView);

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

                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mainIntent);
                            finish();

                        }
                    }, 2000);
                }
                else {
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mainIntent);
                            finish();

                        }
                    }, 2000);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
