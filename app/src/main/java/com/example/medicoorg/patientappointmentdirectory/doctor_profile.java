package com.example.medicoorg.patientappointmentdirectory;

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
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicoorg.MainActivity;
import com.example.medicoorg.Profile;
import com.example.medicoorg.R;

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

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

import de.hdodenhof.circleimageview.CircleImageView;

public class doctor_profile extends AppCompatActivity {

    private static final String AUTH_KEY = "AAAAJFlBJdI:APA91bFmqaugVuKG-pbjmvtZ0dMn0-EmFFDOqaXuvnmf9CudBU27Vl_gflvf9E7dmG-GYcL59qhFQkaiXdQHr3D5yaagPiMVwz4p2lE0uvtQQRy8jy__gnjRiLq6HjBG289IcHph4n9Y";
    private String token;
String notificationname,notificationimage,notificationnumber;
    TextView username,hname,specialized,phonenumber;
    CircleImageView imageview;
    String firebasekey;


    StorageReference userprofileimageRef ;
    String Currentuserid;
    FirebaseAuth mAuth;
    int Channelid=123;

    DatabaseReference doctorref,currentref;
    String reciever;
Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_doctor_profile);

        Intent i=getIntent();
        firebasekey=i.getStringExtra("key");
        Currentuserid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        btn=findViewById(R.id.doctorprofilebtn);
        currentref=FirebaseDatabase.getInstance().getReference().child("users").child(Currentuserid);
        doctorref= FirebaseDatabase.getInstance().getReference("Doctors").child(firebasekey);
        reciever=FirebaseDatabase.getInstance().getReference("Doctors").child(firebasekey).toString();
        username=findViewById(R.id.username);
        hname=findViewById(R.id.hospitalname);
        specialized=findViewById(R.id.profilespecialization);
        phonenumber=findViewById(R.id.numberp);
        imageview=findViewById(R.id.my_profile_pic);
        mAuth=FirebaseAuth.getInstance();

currentref.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
   if(dataSnapshot.exists()){
       if(dataSnapshot.hasChild("username")){
          notificationname = dataSnapshot.child("username").getValue().toString();
          notificationimage=dataSnapshot.child("profileimage").getValue().toString();
          notificationnumber=dataSnapshot.child("number").getValue().toString();
       }
   }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});
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


btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    token = task.getException().getMessage();
                    Log.w("FCM TOKEN Failed", task.getException());
                } else {

                    patientnotificationdatabase();
                    token = task.getResult().getToken();
                    Log.i("FCM TOKEN", token);
                }
            }
        });


        sendWithOtherThread("topic");
    }

    private void patientnotificationdatabase() {
        DatabaseReference patientnotificationref=FirebaseDatabase.getInstance().getReference().child("patientnotification")
                .child(firebasekey)
                .child(Currentuserid);
        HashMap patientnotifymap=new HashMap();
        patientnotifymap.put("patientusername",notificationname);
        patientnotifymap.put("patientprofileimage",notificationimage);
        patientnotifymap.put("patientnumber",notificationnumber);
        patientnotificationref.updateChildren(patientnotifymap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
           if(task.isSuccessful()){
               Toast.makeText(doctor_profile.this,"Your appointment proposal has sent ",Toast.LENGTH_SHORT).show();
           }
           else{
               Toast.makeText(doctor_profile.this,"Appointment failed",Toast.LENGTH_SHORT).show();
           }

            }
        });



    }
});
    }
    private void sendWithOtherThread(final String type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pushNotification(type);
            }
        }).start();
    }
    private void pushNotification(String type) {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jData = new JSONObject();
        try {
            jNotification.put("title", notificationname);
            jNotification.put("body", "Have an appointment today");
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", "OPEN_ACTIVITY_1");



            switch(type) {

                case "topic":
                    jPayload.put("to", "/topics/"+firebasekey);
                    break;
            }

            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jData);

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization","key=" +AUTH_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    Log.e("res",resp);
                }
            });
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }


}
