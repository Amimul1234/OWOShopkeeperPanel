package com.shopKPR.shopKeeperPanel;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.shopKPR.R;
import com.shopKPR.configurations.HostAddress;

public class ZoomProductImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_product_image);

        PhotoView photoView = findViewById(R.id.photo_view);
        ImageView back_arrow = findViewById(R.id.back_from_zoom_image);

        String url = getIntent().getStringExtra("image");

        Glide.with(ZoomProductImage.this).load(HostAddress.HOST_ADDRESS.getHostAddress()+url)
                .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(photoView);

        back_arrow.setOnClickListener(v -> finish());
    }
}