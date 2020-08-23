package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.owoshopkeeperpanel.Model.Complains;
import com.owoshopkeeperpanel.R;

public class Contact_us extends AppCompatActivity {

    EditText complain_subject, complain_details;
    private AllianceLoader loader;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String shopKeeperMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        shopKeeperMobile = getIntent().getStringExtra("mobileNumber");

        complain_subject = findViewById(R.id.complain_subject);
        complain_details = findViewById(R.id.complain_details);
        Button submit_complain = findViewById(R.id.submit_complain);
        loader = findViewById(R.id.loader);


        submit_complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.setVisibility(View.VISIBLE);
                String sub = complain_subject.getText().toString();
                String details = complain_details.getText().toString();
                validation(sub, details);
            }
        });
    }

    private void validation(String sub, String details) {
        if(sub.isEmpty())
        {
            complain_subject.setError("Please enter a subject to complain");
            complain_subject.requestFocus();
            loader.setVisibility(View.GONE);
        }
        else if(details.isEmpty())
        {
            complain_details.setError("Please enter complain body");
            complain_details.requestFocus();
            loader.setVisibility(View.GONE);
        }
        else
        {
            Complains complains = new Complains(sub, details);
            database.getReference("ShopKeeperComplains").child(shopKeeperMobile)
                    .setValue(complains)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            loader.setVisibility(View.GONE);

                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.custom_toast,
                                    (ViewGroup) findViewById(R.id.toast_layout_root));

                            ImageView image = (ImageView) layout.findViewById(R.id.image);
                            image.setImageResource(R.drawable.happy);
                            TextView text = (TextView) layout.findViewById(R.id.text);
                            text.setText("Thanks for your patience");

                            Toast toast = new Toast(getApplicationContext());
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {


                    loader.setVisibility(View.GONE);

                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast,
                            (ViewGroup) findViewById(R.id.toast_layout_root));

                    ImageView image = (ImageView) layout.findViewById(R.id.image);
                    image.setImageResource(R.drawable.sad);
                    TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setText("Please try again...");

                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();

                    loader.setVisibility(View.GONE);
                }
            });
        }

    }


}