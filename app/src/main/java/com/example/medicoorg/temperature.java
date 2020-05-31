package com.example.medicoorg;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.widget.Toast.LENGTH_LONG;

public class temperature extends AppCompatActivity {
    LocationManager locationManager;
    String stringcity;
    boolean GpsStatus;
    TextView cityin;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    String provider="";
    ImageView tdayimg,tmrwimg,thirdayimg,fdimg,sdimg;
    TextView presentday, tomorw, dat, sdat, tfat, fourthdat;
    TextView a, b, c, d;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    String jagah="";
    TextView today;
    class Weatherb extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... addressa) {
            try {

                URL url = new URL(addressa[0]);
                HttpURLConnection connectionc = (HttpURLConnection) url.openConnection();
                //Establish connection with adress
                connectionc.connect();
                //retrieve data from url
                InputStream is = connectionc.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                //retrieve data and return as a string
                int data = isr.read();
                String contenta="";
                char ch;
                while (data != -1) {
                    ch = (char) data;
                    contenta = contenta + ch;
                    data = isr.read();


                }
                return contenta;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void search() {

        try {


            String contenta="";
            Weatherb weather = new Weatherb();
            contenta = weather.execute("https://api.openweathermap.org/data/2.5/forecast?q="+stringcity+"&appid=b89b3ca8d366343a3ac1ce2776c1ea03").get();
            /*   Log.e("Content", contenta);*/
            JSONObject jsonObject = new JSONObject(contenta);

            String list = jsonObject.getString("list");

            /* Log.i("listnew", list);*/

            JSONArray array = new JSONArray(list);


            String main = "";

            /*JSONObject data = json.getJSONObject("data");*/

            for (int i = 0; i <= 0; i++) {

                JSONObject listarr = array.getJSONObject(i);
                main = listarr.getString("main");
                String weathernn=listarr.getString("weather");
                Log.e("weather",weathernn);
                JSONArray warray=new JSONArray(weathernn);

                JSONObject w=warray.getJSONObject(0);
                String mainw=w.getString("main");


                /*  Log.e("main",main);*/
                JSONObject jsonObjecttemp = new JSONObject(main);

                String temp = jsonObjecttemp.getString("temp");
                Log.e("temp", temp);
                double cat = ((Double.parseDouble(temp)) - (273.15));
                Log.e("value", String.valueOf(cat));
                String day = String.format("%.0f", cat);
                int  tempor=Integer.valueOf(day.trim());
                Log.e("tempor",day);
                 a(day);
                presentday.setText(day + "°C");

                Log.e("value", String.valueOf(tempor));
                if(mainw.equals("Clear")){
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            // Stuff that updates the UI
                            ConstraintLayout layout=findViewById(R.id.aaa);
                            layout.setBackgroundResource(R.drawable.sunnyback);
                            today.setText("Clear");
                        }
                    });
                }
                else if(mainw.equals("Haze")){
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            // Stuff that updates the UI
                            today.setText("Haze");
                            ConstraintLayout layout=findViewById(R.id.aaa);
                            layout.setBackgroundResource(R.drawable.hazeback);
                        }
                    });
                }
                else if(mainw.equals("Snow")){
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            today.setText("Snow");
                            // Stuff that updates the UI
                            ConstraintLayout layout=findViewById(R.id.aaa);
                            layout.setBackgroundResource(R.drawable.snowback);
                        }
                    });
                }
                else if(mainw.equals("Clouds")){

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            today.setText("Cloud");
                            ConstraintLayout layout=findViewById(R.id.aaa);
                            layout.setBackgroundResource(R.drawable.cloudyback);
                        }
                    });
                }
                else if(mainw.equals("Smoke")){
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            today.setText("Smoke");
                            ConstraintLayout layout=findViewById(R.id.aaa);
                            layout.setBackgroundResource(R.drawable.smokeback);
                            // Stuff that updates the UI

                        }
                    });
                }
                else if(mainw.equals("Rain")) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            today.setText("Rain");
                            // Stuff that updates the UI
                            ConstraintLayout layout=findViewById(R.id.aaa);
                            layout.setBackgroundResource(R.drawable.rainingback);

                        }
                    });
                }
                else if(mainw.equals("Mist")) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            today.setText("Mist");
                            // Stuff that updates the UI
                            ConstraintLayout layout=findViewById(R.id.aaa);
                            layout.setBackgroundResource(R.drawable.mistback);

                        }
                    });
                }   else if(mainw.equals("Drizzle")) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            today.setText("Drizzle");
                            // Stuff that updates the UI
                            ConstraintLayout layout=findViewById(R.id.aaa);
                            layout.setBackgroundResource(R.drawable.drizzleback);

                        }
                    });
                }

            }
            for (int i = 1; i <= 1; i++) {

                JSONObject listarr = array.getJSONObject(i);
                main = listarr.getString("main");
                String weathernn=listarr.getString("weather");
                Log.e("weather",weathernn);
                JSONArray warray=new JSONArray(weathernn);

                JSONObject w=warray.getJSONObject(0);
                String mainw=w.getString("main");
                Log.e("main",mainw);



                JSONObject jsonObjecttemp = new JSONObject(main);

                String temp = jsonObjecttemp.getString("temp");

                double c = ((Double.parseDouble(temp)) - (273.15));
                String d = String.format("%.0f", c);

                tomorw.setText(d + "°C");
                int   tempor=Integer.parseInt(d.trim());

                if(mainw.equals("Clear")){
                    tmrwimg.setImageResource(R.drawable.desert); }
                else if(mainw.equals("Haze")){
                    tmrwimg.setImageResource(R.drawable.haze); }
                else if(mainw.equals("Snow")){
                    tmrwimg.setImageResource(R.drawable.snow); }
                else if(mainw.equals("Clouds")){
                    tmrwimg.setImageResource(R.drawable.cloudy); }
                else if(mainw.equals("Smoke")){
                    tmrwimg.setImageResource(R.drawable.smoke); }
                else if(mainw.equals("Rain")) {
                    tmrwimg.setImageResource(R.drawable.raining);
                }
                else if(mainw.equals("Mist")){
                    tmrwimg.setImageResource(R.drawable.mist);
                }
                else if(mainw.equals("Drizzle")){
                    tmrwimg.setImageResource(R.drawable.drizzle);
                }

            }
            for (int i = 2; i <= 2; i++) {
                JSONObject listarr = array.getJSONObject(i);
                main = listarr.getString("main");
                String weathernn=listarr.getString("weather");
                Log.e("weather",weathernn);
                JSONArray warray=new JSONArray(weathernn);

                JSONObject w=warray.getJSONObject(0);
                String mainw=w.getString("main");
                Log.e("main",mainw);



                JSONObject jsonObjecttemp = new JSONObject(main);

                String temp = jsonObjecttemp.getString("temp");
                double c = ((Double.parseDouble(temp)) - (273.15));
                String d = String.format("%.0f", c);
                dat.setText(d + "°C");
                int   tempor=Integer.parseInt(d);

                if(mainw.equals("Clear")){
                    thirdayimg.setImageResource(R.drawable.desert); }
                else if(mainw.equals("Haze")){
                    thirdayimg.setImageResource(R.drawable.haze); }
                else if(mainw.equals("Snow")){
                    thirdayimg.setImageResource(R.drawable.snow); }
                else if(mainw.equals("Clouds")){
                    thirdayimg.setImageResource(R.drawable.cloudy); }
                else if(mainw.equals("Smoke")){
                    thirdayimg.setImageResource(R.drawable.smoke); }
                else if(mainw.equals("Rain")) {
                    thirdayimg.setImageResource(R.drawable.raining);
                }
                else if(mainw.equals("Mist")){
                    thirdayimg.setImageResource(R.drawable.mist);
                }
                else if(mainw.equals("Drizzle")){
                    thirdayimg.setImageResource(R.drawable.drizzle);
                }

            }
            for (int i = 3; i <= 3; i++) {
                JSONObject listarr = array.getJSONObject(i);
                main = listarr.getString("main");

                JSONObject jsonObjecttemp = new JSONObject(main);
                String weathernn=listarr.getString("weather");
                Log.e("weather",weathernn);
                JSONArray warray=new JSONArray(weathernn);

                JSONObject w=warray.getJSONObject(0);
                String mainw=w.getString("main");
                Log.e("main",mainw);


                String temp = jsonObjecttemp.getString("temp");

                double c = ((Double.parseDouble(temp)) - (273.15));
                String d = String.format("%.0f", c);
                sdat.setText(d + "°C");
                int   tempor=Integer.parseInt(d);

                if(mainw.equals("Clear")){
                    fdimg.setImageResource(R.drawable.desert); }
                else if(mainw.equals("Haze")){
                    fdimg.setImageResource(R.drawable.haze); }
                else if(mainw.equals("Snow")){
                    fdimg.setImageResource(R.drawable.snow); }
                else if(mainw.equals("Clouds")){
                    fdimg.setImageResource(R.drawable.cloudy); }
                else if(mainw.equals("Smoke")){
                    fdimg.setImageResource(R.drawable.smoke); }
                else if(mainw.equals("Rain")) {
                    fdimg.setImageResource(R.drawable.raining);
                }
                else if(mainw.equals("Mist")){
                    fdimg.setImageResource(R.drawable.mist);
                }
                else if(mainw.equals("Drizzle")){
                    fdimg.setImageResource(R.drawable.drizzle);
                }

            }

            for (int i = 4; i <= 4; i++) {
                JSONObject listarr = array.getJSONObject(i);
                main = listarr.getString("main");
                String weathernn=listarr.getString("weather");
                Log.e("weather",weathernn);
                JSONArray warray=new JSONArray(weathernn);

                JSONObject w=warray.getJSONObject(0);
                String mainw=w.getString("main");
                Log.e("main",mainw);


                JSONObject jsonObjecttemp = new JSONObject(main);

                String temp = jsonObjecttemp.getString("temp");
                double c = ((Double.parseDouble(temp)) - (273.15));
                String d = String.format("%.0f", c);
                tfat.setText(d + "°C");
                int   tempor=Integer.parseInt(d);


                if(mainw.equals("Clear")){
                    sdimg.setImageResource(R.drawable.desert); }
                else if(mainw.equals("Haze")){
                    sdimg.setImageResource(R.drawable.haze); }
                else if(mainw.equals("Snow")){
                    sdimg.setImageResource(R.drawable.snow); }
                else if(mainw.equals("Clouds")){
                    sdimg.setImageResource(R.drawable.cloudy); }
                else if(mainw.equals("Smoke")){
                    sdimg.setImageResource(R.drawable.smoke); }
                else if(mainw.equals("Rain")) {
                    sdimg.setImageResource(R.drawable.raining);
                }
                else if(mainw.equals("Mist")){
                    sdimg.setImageResource(R.drawable.mist);
                }
                else if(mainw.equals("Drizzle")){
                    sdimg.setImageResource(R.drawable.drizzle);
                }

            }
            for (int i = 5; i <= 5; i++) {
                JSONObject listarr = array.getJSONObject(i);
                main = listarr.getString("main");
                String weathernn=listarr.getString("weather");
                Log.e("weather",weathernn);
                JSONArray warray=new JSONArray(weathernn);

                JSONObject w=warray.getJSONObject(0);
                String mainw=w.getString("main");
                Log.e("main",mainw);


                /*  Log.e("main",main);*/
                JSONObject jsonObjecttemp = new JSONObject(main);

                String temp = jsonObjecttemp.getString("temp");
                double c = ((Double.parseDouble(temp)) - (273.15));
                String d = String.format("%.0f", c);
                fourthdat.setText(d + " C");

            }






        } catch (Exception e) {
            e.printStackTrace();
        }
        // also just top the timer thread, otherwise, you may receive a crash report
    }

    private void a(String day) {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
        cityin = findViewById(R.id.citynotintent);
        getSupportActionBar().hide();
        checkLocationPermission();
        presentday=findViewById(R.id.orinaltoday);
        tomorw=findViewById(R.id.tomorrowtemp);
        dat=findViewById(R.id.daytomorrowtemp);
        sdat=findViewById(R.id.sectomorrowtemp);
        today=findViewById(R.id.tday);
        tfat=findViewById(R.id.thirdtomorrowtemp);
        fourthdat=findViewById(R.id.fourtomorrowtemp);

        a=findViewById(R.id.c);
        b=findViewById(R.id.d);
        c=findViewById(R.id.e);
        d=findViewById(R.id.f);

        tmrwimg=findViewById(R.id.tomorrowimage);
        fdimg=findViewById(R.id.firstdayimage);
        sdimg=findViewById(R.id.seconddayimage);
        thirdayimg=findViewById(R.id.thirddayimage);
        getSupportActionBar().hide();
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date = dateFormat.format(calendar.getTime());
        String concat=date.substring(0,3);
        Integer conert=Integer.parseInt(date.substring(3,5));
        Integer fa=(conert)+2;
        Integer fb=(fa)+1;
        Integer fc=(fb)+1;
        Integer fd=(fc)+1;
        a.setText(fa+"/"+concat+"20");
        b.setText(fb+"/"+concat+"20");
        c.setText(fc+"/"+concat+"20");
        d.setText(fd+"/"+concat+"20");
        final ProgressDialog progress = new ProgressDialog(this);
        final Timer t = new Timer();

        progress.setTitle("Loading");
        progress.setMessage("Wait we are getting current weather...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        t.schedule(new TimerTask() {
            public void run() {
                search();
                progress.dismiss();
                // when the task active then close the dialog
                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
            }
        }, 5000);


    }

    @Override
    protected void onStart() {
        super.onStart();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        CheckGpsStatus();

        if (GpsStatus == true) {
            Toast.makeText(temperature.this, "Location Services Is Enabled", Toast.LENGTH_SHORT).show();
            startho();

        } else {
            Toast.makeText(temperature.this, "Location Services Is Disabled", Toast.LENGTH_SHORT).show();
        }
    }


    private void CheckGpsStatus() {
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (GpsStatus == true) {
            Toast.makeText(temperature.this, "Location Services Is Enabled", Toast.LENGTH_SHORT).show();
            startho();

        } else {
            Toast.makeText(temperature.this, "Location Services Is Disabled", Toast.LENGTH_SHORT).show();
        }



    }

    private void startho() {

        LocationRequest mLocationRequest = LocationRequest.create();
        Log.e("check","1");
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.e("check","0");
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        Log.e("check","1");
                        //TODO: UI updates.
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    Activity#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for Activity#requestPermissions for more details.
                                return;
                            }
                        }
                        LocationServices.getFusedLocationProviderClient(temperature.this).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                Geocoder geocoder;
                                List<Address> addresses;
                                geocoder = new Geocoder(temperature.this, Locale.getDefault());
                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                    stringcity= addresses.get(0).getLocality();
                                    String state = addresses.get(0).getAdminArea();
                                    String country = addresses.get(0).getCountryName();
                                    String postalCode = addresses.get(0).getPostalCode();
                                    String knownName = addresses.get(0).getFeatureName();
                                    // Logic to handle location object
                                    Log.e("lat:", String.valueOf(location.getLatitude()));
                                    Log.e("lon:", String.valueOf(location.getLongitude()));
                                    cityin.setText(stringcity);
                                    /*Toast.makeText(MainActivity.this,String.valueOf(location.getLatitude()),LENGTH_LONG).show();*/
                                    Toast.makeText(temperature.this, address, Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                            }
                        });
                    }
                }
            }
        };
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, mLocationCallback, null);

    }
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Permision for Location")
                        .setMessage("Please click Ok for permision")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(temperature.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        Toast.makeText(temperature.this,"permission given",Toast.LENGTH_SHORT).show();
                        CheckGpsStatus();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }


}
