package com.example.medicoorg;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.util.Log;
import android.view.MenuItem;

import com.example.medicoorg.Doctorreview.Doctorreview;
import com.example.medicoorg.PatientHistoryDirectory.Patienthistory;
import com.example.medicoorg.PatientReviewDirectory.patientreviewmain;
import com.example.medicoorg.doctorappointmentdirectory.patientproposal;
import com.example.medicoorg.doctorhistorydirectory.Doctorhistory;
import com.example.medicoorg.patientappointmentdirectory.doctoramain;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
TextView headerdname;
CircleImageView headerimgview;
FirebaseAuth mAuth;
String Currentuserid;
    String dbname;
    String dbimage;


    TextView spname,namedoc;
DatabaseReference userRef;
ImageView appointment,review,temperatur,history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ProgressDialog progress = new ProgressDialog(this);
        final Timer t = new Timer();
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        t.schedule(new TimerTask() {
            public void run() {

                progress.dismiss();

                // when the task active then close the dialog
                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
            }
        }, 3000); // after 2 second (or 2000 miliseconds), the task will be active.

mAuth=FirebaseAuth.getInstance();
Currentuserid=mAuth.getCurrentUser().getUid();
        Toolbar toolbar = findViewById(R.id.toolbar);
        spname=findViewById(R.id.specialistname);
        appointment=findViewById(R.id.appointmentimageView);
        review=findViewById(R.id.reviewimageview);
        temperatur=findViewById(R.id.temperatureimageview);
        history=findViewById(R.id.historyimageview);
        namedoc=findViewById(R.id.namedoctor);


history.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(namedoc.getText().toString().contains("Patient")){
            Intent patienthistoryintent=new Intent(MainActivity.this, Patienthistory.class);
            startActivity(patienthistoryintent);

        }
        else{
            Intent doctorhistoryintent=new Intent(MainActivity.this, Doctorhistory.class);
            startActivity(doctorhistoryintent);

        }

    }
});

        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(namedoc.getText().toString().contains("Patient")){
                   Intent patientintent=new Intent(MainActivity.this, doctoramain.class);
                   startActivity(patientintent);

               }
               else{
                   Intent doctorintent=new Intent(MainActivity.this, patientproposal.class);
                   startActivity(doctorintent);

               }

            }
        });
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(namedoc.getText().toString().contains("Patient")){
                    Intent patientintent=new Intent(MainActivity.this, patientreviewmain.class);
                    startActivity(patientintent);

                }
                else{
                    Intent doctorintent=new Intent(MainActivity.this, Doctorreview.class);
                    startActivity(doctorintent);

                }

            }
        });
        temperatur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tmprature=new Intent(MainActivity.this,temperature.class);
                startActivity(tmprature);

            }
        });

        setSupportActionBar(toolbar);
        NavigationView navigationaView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationaView.getHeaderView(0);
        headerdname=headerView.findViewById(R.id.headerviewdoctorname);
        headerimgview=headerView.findViewById(R.id.headerimageview);
        if(mAuth.getCurrentUser()!=null) {
            String Current_user_id = mAuth.getCurrentUser().getUid();
            userRef = FirebaseDatabase.getInstance().getReference().child("users");
            userRef.child(Current_user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if(dataSnapshot.hasChild("username")){
                            dbname= dataSnapshot.child("username").getValue().toString();
                            headerdname.setText(dbname);
                            namedoc.setText(dbname);
                        }
                        if(dataSnapshot.hasChild("specialized")){
                           String  specialname= dataSnapshot.child("specialized").getValue().toString();
                            spname.setText(specialname);
                        }
                        if(dataSnapshot.hasChild("profileimage")) {

                           dbimage = dataSnapshot.child("profileimage").getValue().toString();
                            Picasso.get().load(dbimage).placeholder(R.drawable.man).into(headerimgview);
                        }}
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
getSupportActionBar().setTitle("");
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsintent=new Intent(MainActivity.this, settings.class);
            startActivity(settingsintent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        }
        else if (id == R.id.nav_profile_show) {
            Intent profileshowintent=new Intent(MainActivity.this, Profile_show.class);
            startActivity(profileshowintent);
        }else if (id == R.id.nav_logout) {
            String Current_user_id=mAuth.getCurrentUser().getUid();
            FirebaseMessaging.getInstance().unsubscribeFromTopic(Current_user_id);
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            //logout
            editor.remove("email"); // will delete key name
            editor.remove("password"); // will delete key email
            editor.commit();
            Intent logoutintent=new Intent(MainActivity.this, PlatformActivity.class);
            logoutintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logoutintent);
            mAuth.signOut();

        }  else if (id == R.id.nav_settings) {
            Intent settingsintent=new Intent(MainActivity.this, settings.class);
            startActivity(settingsintent);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void collectPhoneNumbers(Map<String,Object> users)
    {
        ArrayList<Object> name;
        ArrayList<Object> image;
        ArrayList<Object> phoneNumbers;
      phoneNumbers = new ArrayList<>();
        image= new ArrayList<>();
         name= new ArrayList<>();
Log.e("hereforlop","");

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){
            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            phoneNumbers.add(singleUser.get("patientnumber"));
            image.add(singleUser.get("patientimage"));
        name.add(singleUser.get("patientfullname"));
            List<String> notificationpatientphonenumber = Arrays.asList(phoneNumbers.toArray(new String[name.size()]));
            List<String> notificationpatientimage = Arrays.asList(image.toArray(new String[name.size()]));
            List<String> notificationpatientname = Arrays.asList(name.toArray(new String[name.size()]));
            Log.e("sendingphonenumber", String.valueOf(notificationpatientphonenumber));
            Log.e("sendingphonenumber", String.valueOf(notificationpatientname));
            Log.e("sendingphonenumber", String.valueOf(notificationpatientphonenumber));
            for(int i=0;i<1;i++){
                String a=String.valueOf(i);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this,"123")
                        .setSmallIcon(R.drawable.man)
                        .setContentTitle("My notification")
                        .setContentText("Much longer text that cannot fit one line...")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("Much longer text that cannot fit one line..."))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                createNotificationChannel(builder,notificationpatientimage,notificationpatientphonenumber,notificationpatientname);

            }
        }

}

    private void createNotificationChannel(NotificationCompat.Builder builder, List<String> notificationpatientimage, List<String> notificationpatientphonenumber, List<String> notificationpatientname) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "name";
            String description ="description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("123", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
            notificationManagerCompat.notify(123, builder.build());

            //Click notification
            Intent intent = new Intent(this, Splash.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            NotificationCompat.Builder buildernew = new NotificationCompat.Builder(this, "123")
                    .setSmallIcon(R.drawable.man)
                    .setContentTitle(String.valueOf(notificationpatientname).substring(1, String.valueOf(notificationpatientname).length()-1))
                    .setContentText("Has an appointment")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            notificationManager.notify(123, buildernew.build());

        }

    }



    @Override
    protected void onStart() {
        super.onStart();
        String Current_user_id=mAuth.getCurrentUser().getUid();
        FirebaseMessaging.getInstance().subscribeToTopic(Current_user_id);
    }

}
