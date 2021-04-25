package com.shopKPR.userRegistration;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import com.shopKPR.R;
import com.shopKPR.configurations.TermsAndConditions;

public class TermsConditionsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);

        TextView termsAndConditions = findViewById(R.id.termsAndConditions);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            termsAndConditions.setText(Html.fromHtml(TermsAndConditions.TERMS_AND_CONDITIONS.getTermsAndConditions(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            termsAndConditions.setText(Html.fromHtml(TermsAndConditions.TERMS_AND_CONDITIONS.getTermsAndConditions()));
        }

    }
}
