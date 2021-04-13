package com.owoShopKeeperPanel.userRegistration;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.owoShopKeeperPanel.network.RetrofitClient;
import com.owoShopKeeperPanel.Model.UserShopKeeper;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.login.LogInActivity;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyPhoneActivity extends AppCompatActivity {

    private EditText inputOtp;
    private String mobileNumber, name, hashed_pin;
    private String verificationId;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        mAuth = FirebaseAuth.getInstance();

        inputOtp = findViewById(R.id.otp);
        Button continueBtn = findViewById(R.id.nextButton);
        progressBar = findViewById(R.id.progressbar);

        mobileNumber = getIntent().getStringExtra("mobileNumber");
        name = getIntent().getStringExtra("name");
        hashed_pin = getIntent().getStringExtra("hashed_pin");
        verificationId = getIntent().getStringExtra("verificationId");

        continueBtn.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            String code = inputOtp.getText().toString().trim();
            if (code.isEmpty() || code.length() < 6) {
                inputOtp.setError("Enter code...");
                inputOtp.requestFocus();
                return;
            }

            verifyCode(code);
        });
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential)
    {
        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {

                UserShopKeeper userShopKeeper = new UserShopKeeper(name, mobileNumber, hashed_pin, null);

                RetrofitClient.getInstance().getApi()
                        .registerShopKeeper(userShopKeeper)
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                if(response.isSuccessful())
                                {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "Congratulations ! Your account created successfully", Toast.LENGTH_SHORT).show();
                                    FirebaseAuth.getInstance().signOut();
                                    Intent intent = new Intent(VerifyPhoneActivity.this, LogInActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(VerifyPhoneActivity.this, "Can not create account, please try again", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Network error, please try again", Toast.LENGTH_SHORT).show();
                                Log.e("Verify Phone class", t.getMessage());
                            }
                        });
            } else {
                Toast.makeText(VerifyPhoneActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
