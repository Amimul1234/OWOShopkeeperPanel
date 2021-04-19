package com.shopKPR.userRegistration;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.shopKPR.R;
import com.shopKPR.configurations.TermsAndConditions;

public class TermsConditionsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);

        TextView termsAndConditions = findViewById(R.id.termsAndConditions);
        termsAndConditions.setText(TermsAndConditions.TERMS_AND_CONDITIONS.getTermsAndConditions());
    }
}
