package com.owoShopKeeperPanel.shopKeeperPanel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.owoShopKeeperPanel.R;

public class ZoomProductImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_product_image);

        PhotoView photoView = findViewById(R.id.photo_view);
        ImageView back_arrow = findViewById(R.id.back_from_zoom_image);

        String url = getIntent().getStringExtra("image");

        Glide.with(ZoomProductImage.this).load(url).into(photoView);

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}