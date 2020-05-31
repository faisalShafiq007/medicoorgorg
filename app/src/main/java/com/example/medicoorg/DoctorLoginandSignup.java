package com.example.medicoorg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorLoginandSignup extends AppCompatActivity {
    private TextView mTextMessage;
    CircleImageView circleImageView;
    TextInputEditText name,email,password;
    String mail,pass;
    Button registerbtn;
    String user_name;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int i=0;
    private FirebaseAuth mAuth;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_signin:
                   signin();
                    return true;

                case R.id.navigation_signup:
                    signup();

                    return true;
            }
            return false;
        }
    };

    private void signin() {
      Log.e("username",user_name);
        getSupportActionBar().setTitle(user_name+" Sign In");
        registerbtn.setText("Sign In");
i=1;
    }

    private void signup() {
        i=0;
        Log.e("username",user_name);
        getSupportActionBar().setTitle(user_name+ "Register");

        registerbtn.setText("Register");



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        user_name = intent.getStringExtra("user");
        getSupportActionBar().setTitle(user_name+" Register");

        setContentView(R.layout.activity_doctor_loginand_signup);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        circleImageView=findViewById(R.id.circularrrrimageview);
        email=findViewById(R.id.emailedittext);
        password=findViewById(R.id.passwordedittext);
        registerbtn=findViewById(R.id.buttonRegisteration);
        mAuth = FirebaseAuth.getInstance();
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {
                    if(email.getText().toString().isEmpty()){
                        email.setError("Please type email");

                    }
                    else if(password.getText().toString().isEmpty()){
                        password.setError("Please type password");
                    }
                    else if(!email.getText().toString().contains("@")){
                        email.setError("Please tpye complete email with @");
                    }
                    else if(!email.getText().toString().contains(".com")){
                        email.setError("Please tpye complete email with .com");
                    }
                    else if(password.getText().toString().length()<9){
                        password.setError("Please tpye complete password with length 9");
                    }
                    else {
                        final ProgressDialog progress = new ProgressDialog(DoctorLoginandSignup.this);
                        final Timer t = new Timer();
                        progress.setTitle("Loading");
                        progress.setMessage("Wait while Signing up...");
                        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                        progress.show();
                        t.schedule(new TimerTask() {
                            public void run() {

                                progress.dismiss();

                                // when the task active then close the dialog
                                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                            }
                        }, 2000);
                    mail=email.getText().toString();
                    pass=password.getText().toString();

                    Log.e("mail",mail);
                    mAuth.createUserWithEmailAndPassword(mail,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                            editor = pref.edit();
                            editor.putString("email", mail);
                            editor.putString("password", pass);
                            editor.commit();
                            Log.e("success","success");
                            Toast.makeText(DoctorLoginandSignup.this,"successfully register",Toast.LENGTH_SHORT).show();

                                Intent profile=new Intent(DoctorLoginandSignup.this,Profile.class);
                                profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                profile.putExtra("user",user_name);
                                startActivity(profile);


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(DoctorLoginandSignup.this,"failed to register",Toast.LENGTH_SHORT).show();
                        }
                    });

                }}
                else{

                    mail=email.getText().toString();
                    pass=password.getText().toString();
                    if(email.getText().toString().isEmpty()){
                        email.setError("Please type email");

                    }
                    else if(password.getText().toString().isEmpty()){
                        password.setError("Please type password");
                    }
                    else if(!email.getText().toString().contains("@")){
                        email.setError("Please tpye complete email with @");
                    }
                    else if(password.getText().toString().length()<9){
                        password.setError("Please tpye complete password with length 9");
                    }
                   else{
                        final ProgressDialog progress = new ProgressDialog(DoctorLoginandSignup.this);
                        final Timer t = new Timer();
                        progress.setTitle("Loading");
                        progress.setMessage("Wait while Signing in...");
                        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                        progress.show();
                        t.schedule(new TimerTask() {
                            public void run() {

                                progress.dismiss();

                                // when the task active then close the dialog
                                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                            }
                        }, 2000);

                        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
                       mAuth.signInWithEmailAndPassword(mail,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                           @Override
                           public void onSuccess(AuthResult authResult) {
                               userRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                      // after 2 second (or 2000 miliseconds), the task will be active.

                                       if (dataSnapshot.exists()) {
                                           if(dataSnapshot.hasChild("username")){
                                               String name = dataSnapshot.child("username").getValue().toString();
                                               if(!name.contains(user_name)){
                                                   if(user_name.equals("Doctor")){
                                                       Toast.makeText(DoctorLoginandSignup.this,"You are  not a Doctor",Toast.LENGTH_SHORT).show();
                                                   }
                                                   if(user_name.equals("Patient")){
                                                       Toast.makeText(DoctorLoginandSignup.this,"You are not a Patient",Toast.LENGTH_SHORT).show();

                                                   }

                                               }
                                               else{
                                                pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                                                   editor = pref.edit();
                                                   editor.putString("email", mail);
                                                   editor.putString("password", pass);
                                                   editor.commit();

                                                   Toast.makeText(DoctorLoginandSignup.this,"Welcome:"+name ,Toast.LENGTH_SHORT).show();
                                                   Intent mainintent=new Intent(DoctorLoginandSignup.this,MainActivity.class);
                                                   mainintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                   startActivity(mainintent);

                                               }

                                           }}

                                   }

                                   @Override
                                   public void onCancelled(@NonNull DatabaseError databaseError) {

                                   }
                               });
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Toast.makeText(DoctorLoginandSignup.this,"failed to login",Toast.LENGTH_SHORT).show();

                           }
                       });
                    }
                }
            }
        });

    }



}
