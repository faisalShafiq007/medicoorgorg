package com.example.medicoorg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicoorg.MainActivity;
import com.example.medicoorg.Profile;
import com.example.medicoorg.R;
import com.example.medicoorg.patientappointmentdirectory.doctor_profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_show extends AppCompatActivity {
 TextView username,hname,specialized,phonenumber;
 CircleImageView imageview;
 StorageReference userprofileimageRef ;
 String Currentuserid;
 FirebaseAuth mAuth;
 int Channelid=123;
 DatabaseReference doctorref;
 Button btn;
 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  FirebaseAuth mAuth=FirebaseAuth.getInstance();
  String Currrentuserid=mAuth.getCurrentUser().getUid();
  doctorref= FirebaseDatabase.getInstance().getReference("users").child(Currrentuserid);
  setContentView(R.layout.activity_profile_show);
  username=findViewById(R.id.username);
  hname=findViewById(R.id.hospitalname);
  specialized=findViewById(R.id.profilespecialization);
  phonenumber=findViewById(R.id.numberp);
  imageview=findViewById(R.id.my_profile_pic);
  mAuth=FirebaseAuth.getInstance();
  Currentuserid=FirebaseAuth.getInstance().getCurrentUser().getUid();
  doctorref.addValueEventListener(new ValueEventListener() {
   @Override
   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
    if(dataSnapshot.exists()){
     String image=dataSnapshot.child("profileimage").getValue().toString();
     Picasso.get().load(image).placeholder(R.drawable.man).into(imageview);

    }
    if (dataSnapshot.hasChild("username")) {
     String fullname = dataSnapshot.child("username").getValue().toString();
     getSupportActionBar().setTitle(fullname+" Profile");
     username.setText(fullname);
    }
    if (dataSnapshot.hasChild("hospital")) {
     String hospitalname = dataSnapshot.child("hospital").getValue().toString();
     hname.setText("Hospital: "+hospitalname);
    }
    if (dataSnapshot.hasChild("number")) {
     String phonenumber1 = dataSnapshot.child("number").getValue().toString();
     phonenumber.setText("No: "+phonenumber1);
    }
    if (dataSnapshot.hasChild("specialized")) {
     String specializationstring= dataSnapshot.child("specialized").getValue().toString();
     specialized.setText("Major: "+specializationstring);
    }
   }

   @Override
   public void onCancelled(@NonNull DatabaseError databaseError) {

   }
  });




 }}