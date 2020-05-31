package com.example.medicoorg.doctorappointmentdirectory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicoorg.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
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

public class patientproposal extends AppCompatActivity {
    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter adapter;
    String visituserid;
    RecyclerView reccyclerView;
    String image,name,number,doctorname,doctornumber,doctorimage,doctorhospital;
    private String token;
    private static final String AUTH_KEY = "AAAAJFlBJdI:APA91bFmqaugVuKG-pbjmvtZ0dMn0-EmFFDOqaXuvnmf9CudBU27Vl_gflvf9E7dmG-GYcL59qhFQkaiXdQHr3D5yaagPiMVwz4p2lE0uvtQQRy8jy__gnjRiLq6HjBG289IcHph4n9Y";
    String Currentuserid;
    String notificationname,notificationimage;
    FirebaseAuth mAuth;
    DatabaseReference patientref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setTitle("Doctors Appointment");
        Currentuserid = mAuth.getCurrentUser().getUid();
        setContentView(R.layout.activity_patientproposal);
        reccyclerView = findViewById(R.id.patientrecyclerView);
        patientref = FirebaseDatabase.getInstance().getReference().child("users").child(Currentuserid);
        patientref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
           if(snapshot.exists()){
               notificationname=snapshot.child("username").getValue().toString();
               notificationimage=snapshot.child("profileimage").getValue().toString();
               doctorimage=snapshot.child("profileimage").getValue().toString();

                       doctorname=snapshot.child("username").getValue().toString();
                       doctornumber=snapshot.child("number").getValue().toString();
               doctorhospital=snapshot.child("hospital").getValue().toString();

           }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        linearLayoutManager = new LinearLayoutManager(this);

        reccyclerView.setLayoutManager(new LinearLayoutManager(patientproposal.this));
        fetch();

    }

    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("patientnotification").child(Currentuserid);
        FirebaseRecyclerOptions<patientmodel> patientoptions =
                new FirebaseRecyclerOptions.Builder<patientmodel>()
                        .setQuery(query, new SnapshotParser<patientmodel>() {
                            @NonNull
                            @Override
                            public patientmodel parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new patientmodel(

                                        image=snapshot.child("patientprofileimage").getValue().toString(),
                                        name=snapshot.child("patientusername").getValue().toString(),
                                        number=snapshot.child("patientnumber").getValue().toString());
                            }
                        })
                        .build();
        adapter=new FirebaseRecyclerAdapter<patientmodel,ViewHolder>(patientoptions) {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.proposallist, parent, false);

                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, final int position,@NonNull patientmodel model) {
                holder.setTxtimageview(model.getPatientprofileimage());
                holder.setTxthospital(model.getPatientusername());
                holder.setTxtspeical(model.getPatientnumber());
                visituserid =getRef(position).getKey();


holder.accept.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    token = task.getException().getMessage();
                    Log.w("FCM TOKEN Failed", task.getException());
                } else {


                    token = task.getResult().getToken();
                    Log.i("FCM TOKEN", token);
                }
            }
        });


        sendWithOtherThread("topic");
        Toast.makeText(patientproposal.this, "accepted ", Toast.LENGTH_SHORT).show();
        accept(visituserid);

    }
});
holder.reject.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        reject(visituserid);

    }
});
                holder.patinetroot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }


        };
        reccyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void reject(String id) {
        DatabaseReference rejectreference=FirebaseDatabase.getInstance().getReference().child("patientnotification")
                .child(Currentuserid).child(id);
        rejectreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                    Toast.makeText(patientproposal.this,"Rejected",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void accept(String id) {

        DatabaseReference doctorhistoryref=FirebaseDatabase.getInstance().getReference().child("DoctorHistory").child(Currentuserid).child(id);
        HashMap historymap=new HashMap();
        historymap.put("patientname",name);
        historymap.put("patientimage",image);
        historymap.put("patientnumber",number);

        doctorhistoryref.updateChildren(historymap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){

                }
            }
        });
        DatabaseReference patienthistoryref=FirebaseDatabase.getInstance().getReference().child("PatientHistory").child(id).child(Currentuserid);
        HashMap patienthistorymap=new HashMap();
        patienthistorymap.put("doctorname",doctorname);
        patienthistorymap.put("doctorimage",doctorimage);
        patienthistorymap.put("doctornumber",doctornumber);
        patienthistoryref.updateChildren(patienthistorymap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {


            }
        });
        DatabaseReference patientreviewref=FirebaseDatabase.getInstance().getReference().child("patientreview").child(id).child(Currentuserid);
        HashMap patientreviewmap=new HashMap();
        patientreviewmap.put("doctorname",doctorname);
        patientreviewmap.put("doctorimage",doctorimage);
        patientreviewmap.put("doctornumber",doctornumber);
        patientreviewmap.put("doctorhospital",doctorhospital);

        patientreviewref.updateChildren(patientreviewmap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

            }
        });

        DatabaseReference rejectreference=FirebaseDatabase.getInstance().getReference().child("patientnotification")
                .child(Currentuserid).child(id);
        rejectreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                    Toast.makeText(patientproposal.this,"Remove successfuly from firebase",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout patinetroot;
        public CircleImageView IV;
        public Button accept,reject;
        public TextView tvname,tvnumber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            patinetroot=itemView.findViewById(R.id.proposallistroot);
            IV=itemView.findViewById(R.id.proposallistcircleimageview);
            accept=itemView.findViewById(R.id.proposallistaccept);
            reject=itemView.findViewById(R.id.proposallistreject);
            tvname=itemView.findViewById(R.id.proposallistname);
            tvnumber=itemView.findViewById(R.id.proposallistnumber);

        }
        public void setTxtimageview(String stringd) {
            Picasso.get().load(stringd).into(IV);
        }
        public void setTxthospital(String stringa) {
            tvname.setText(stringa);
        }
        public void setTxtspeical(String stringb) {
            tvnumber.setText(stringb);
        }



    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

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
            jNotification.put("body", "Appointment done Please add review");
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", "OPEN_ACTIVITY_1");


            switch(type) {

                case "topic":
                    jPayload.put("to", "/topics/"+visituserid);
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