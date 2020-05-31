package com.example.medicoorg.doctorhistorydirectory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicoorg.R;
import com.example.medicoorg.doctorappointmentdirectory.patientmodel;
import com.example.medicoorg.doctorappointmentdirectory.patientproposal;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Doctorhistory extends AppCompatActivity {
    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter adapter;
    RecyclerView reccyclerView;
    String image,name,number;
    String Currentuserid;
    FirebaseAuth mAuth;
    DatabaseReference historyref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorhistory);
        getSupportActionBar().setTitle("Doctors Previous Patients");
        mAuth = FirebaseAuth.getInstance();
        Currentuserid = mAuth.getCurrentUser().getUid();

        reccyclerView = findViewById(R.id.doctorhistoryrecyclerView);
       historyref = FirebaseDatabase.getInstance().getReference().child("DoctorHistory").child(Currentuserid);
        linearLayoutManager = new LinearLayoutManager(this);

        reccyclerView.setLayoutManager(new LinearLayoutManager(Doctorhistory.this));
        fetch();
    }

    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("DoctorHistory").child(Currentuserid);
        FirebaseRecyclerOptions<Doctorhistorymodel> doctorhistoryoptions =
                new FirebaseRecyclerOptions.Builder<Doctorhistorymodel>()
                        .setQuery(query, new SnapshotParser<Doctorhistorymodel>() {
                            @NonNull
                            @Override
                            public Doctorhistorymodel parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new Doctorhistorymodel(
                                        image=snapshot.child("patientimage").getValue().toString(),
                                        name=snapshot.child("patientname").getValue().toString(),
                                        number=snapshot.child("patientnumber").getValue().toString());
                            }
                        })
                        .build();
        adapter=new FirebaseRecyclerAdapter<Doctorhistorymodel,ViewHolder>(doctorhistoryoptions) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.doctorhistorylist, parent, false);

                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, final int position,@NonNull Doctorhistorymodel model) {
                holder.setTxtimageview(model.getPatientimage());
                holder.setTxthospital(model.getPatientname());
                holder.setTxtspeical(model.getPatientnumber());


                holder.Doctoryhistoryroot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }


        };
        reccyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout Doctoryhistoryroot;
        public CircleImageView doctorhistoryIV;
        public TextView doctorhistorytvname,doctorhistorytvnumber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Doctoryhistoryroot=itemView.findViewById(R.id.doctorhistorylistroot);
            doctorhistoryIV=itemView.findViewById(R.id.doctorhistorylistcircleimageview);

            doctorhistorytvname=itemView.findViewById(R.id.doctorhistorylistlistname);
            doctorhistorytvnumber=itemView.findViewById(R.id.doctorhistorylistlistnumber);

        }
        public void setTxtimageview(String stringd) {
            Picasso.get().load(stringd).into(doctorhistoryIV);
        }
        public void setTxthospital(String stringa) {
            doctorhistorytvname.setText(stringa);
        }
        public void setTxtspeical(String stringb) {
            doctorhistorytvnumber.setText(stringb);
        }



    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }
}
