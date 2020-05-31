package com.example.medicoorg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlatformActivity extends AppCompatActivity {
ImageButton doctor,patient;
TextView admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platform);
        getSupportActionBar().hide();
        admin=findViewById(R.id.admintextview);
        doctor=findViewById(R.id.doctorButton);
        patient=findViewById(R.id.patientbutton);
        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent d=new Intent(PlatformActivity.this,DoctorLoginandSignup.class);
                d.putExtra("user", "Doctor");
                startActivity(d);
            }
        });
        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent p=new Intent(PlatformActivity.this,DoctorLoginandSignup.class);
                p.putExtra("user", "Patient");
                startActivity(p);

            }
        });


    }
}
