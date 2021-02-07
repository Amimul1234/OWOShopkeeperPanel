package com.owoShopKeeperPanel.shopKeeperPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.owoShopKeeperPanel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ForgetVerifyPhoneActivity extends AppCompatActivity {

    private EditText otp;
    private Button continueBtn;
    private String mobilenumber;
    private String verificationId;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_verify_phone);

        mAuth = FirebaseAuth.getInstance();

        otp=(EditText)findViewById(R.id.forget_otp);
        continueBtn=(Button)findViewById(R.id.forget_continue_btn);
        progressBar = findViewById(R.id.progressbar);

        mobilenumber = getIntent().getStringExtra("mobilenumber");
        String phonenumber = getIntent().getStringExtra("phonenumber");

        sendVerificationCode(phonenumber);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = otp.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    otp.setError("Enter code...");
                    otp.requestFocus();
                    return;
                }
                verifyCode(code);

            }
        });
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(ForgetVerifyPhoneActivity.this, SetPinActivity.class);
                            intent.putExtra("mobilenumber", mobilenumber);
                            startActivity(intent);

                        } else {
                            Toast.makeText(ForgetVerifyPhoneActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                (Activity) TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                otp.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(ForgetVerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
}
