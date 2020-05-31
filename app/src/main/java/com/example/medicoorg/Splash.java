package com.example.medicoorg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Splash extends AppCompatActivity {
    String p,e;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        Thread thread=new Thread(){
            @Override
            public void run() {
                try{
                    sleep(2000);

                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                     e=pref.getString("email",null);
                    p=pref.getString("password",null);
                    if(TextUtils.isEmpty(e)){
                        Intent intent=new Intent(Splash.this, PlatformActivity.class);
                       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        Splash.this.finish();}
                    else{
                        FirebaseAuth mAuth=FirebaseAuth.getInstance();
                        mAuth.signInWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Intent intent_1=new Intent(Splash.this, MainActivity.class);
                                intent_1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent_1);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Intent intent=new Intent(Splash.this, PlatformActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                Splash.this.finish();
                            }
                        });

                    }
                }
            }
        };
        thread.start();

    }
    }

