package com.owoShopKeeperPanel.shopKeeperPanel;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.owoShopKeeperPanel.R;

public class Contact_us extends AppCompatActivity {

    private WebView chat_with_us;
    private AllianceLoader loader;
    private ImageView back_to_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        back_to_home = findViewById(R.id.back_to_home);
        chat_with_us = findViewById(R.id.chat_with_us);
        loader = findViewById(R.id.loader);

        chat_with_us.getSettings().setJavaScriptEnabled(true);

        chat_with_us.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if(progress < 100)
                {
                    loader.setVisibility(View.VISIBLE);
                }
                else
                    loader.setVisibility(View.INVISIBLE);
            }
        });

        chat_with_us.loadUrl("https://tawk.to/chat/5f85a1484704467e89f70ca9/default");

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }
}