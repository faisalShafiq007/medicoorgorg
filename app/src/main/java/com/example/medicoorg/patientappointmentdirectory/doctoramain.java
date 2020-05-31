package com.example.medicoorg.patientappointmentdirectory;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicoorg.MainActivity;
import com.example.medicoorg.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class doctoramain extends AppCompatActivity {
     LinearLayoutManager linearLayoutManager;
     FirebaseRecyclerAdapter adapter;
RecyclerView reccyclerView;
DatabaseReference doctorref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorappointment);
        getSupportActionBar().setTitle("Doctor List");
        doctorref= FirebaseDatabase.getInstance().getReference().child("Doctors");
        reccyclerView=findViewById(R.id.recyclerView);


        linearLayoutManager = new LinearLayoutManager(this);

        reccyclerView.setLayoutManager(new LinearLayoutManager(doctoramain.this));
        fetch();
    }

    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Doctors");
        FirebaseRecyclerOptions<doctormodel> options =
                new FirebaseRecyclerOptions.Builder<doctormodel>()
                        .setQuery(query, new SnapshotParser<doctormodel>() {
                            @NonNull
                            @Override
                            public doctormodel parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new doctormodel(snapshot.child("profileimage").getValue().toString(),
                                        snapshot.child("hospital").getValue().toString(),
                                        snapshot.child("specialized").getValue().toString(),
                                snapshot.child("username").getValue().toString());
                            }
                        })
                        .build();
        adapter=new FirebaseRecyclerAdapter<doctormodel,ViewHolder>(options) {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.doctorlist, parent, false);

                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, final int position,@NonNull doctormodel doctormodel) {

                holder.setTxtimageview(doctormodel.getProfileimage());
                holder.setTxthospital(doctormodel.getHospital());
                holder.setTxtusername(doctormodel.getUsername());
                holder.setTxtspeical(doctormodel.getSpecialized());

holder.doctorlistbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        String visituserid=getRef(position).getKey();
        Intent profile=new Intent(doctoramain.this,doctor_profile.class);
        profile.putExtra("key",visituserid);
        startActivity(profile);

        Log.e("visit id",visituserid);
    }
});
                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(doctoramain.this, "Click on the button to view profile ", Toast.LENGTH_SHORT).show();
                    }
                });
            }


               };
        reccyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout root;
        public CircleImageView txtimgview;
        public ImageView doctorlistbtn;
        public TextView txthospital;
        public TextView txtspecial;
        public TextView txtusername;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root=itemView.findViewById(R.id.liist_root);
            txtimgview=itemView.findViewById(R.id.doctorlistimageview);
            txthospital=itemView.findViewById(R.id.doctorlisthospitalname);
            txtspecial=itemView.findViewById(R.id.doctorlistspeacialist);
            txtusername=itemView.findViewById(R.id.doctorlistname);
            doctorlistbtn=itemView.findViewById(R.id.doctorlistbutton);


        }
        public void setTxtimageview(String stringd) {
            Picasso.get().load(stringd).into(txtimgview);
        }
        public void setTxthospital(String stringa) {
            txthospital.setText(stringa);
        }
        public void setTxtspeical(String stringb) {
            txtspecial.setText(stringb);
        }
        public void setTxtusername(String stringc) {
            txtusername.setText(stringc);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

adapter.startListening();


    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}


