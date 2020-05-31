package com.example.medicoorg.PatientHistoryDirectory;

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

import com.example.medicoorg.R;
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

public class Patienthistory extends AppCompatActivity {
    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter adapter;
    RecyclerView reccyclerView;
    String image,name,number;
    String Currentuserid;
    FirebaseAuth mAuth;
    DatabaseReference patienthistoryref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patienthistory);
        getSupportActionBar().setTitle("Previous Doctors");
        mAuth = FirebaseAuth.getInstance();
        Currentuserid = mAuth.getCurrentUser().getUid();
        reccyclerView = findViewById(R.id.patienthistoryrecyclerView);
        patienthistoryref = FirebaseDatabase.getInstance().getReference().child("PatientHistory").child(Currentuserid);
        linearLayoutManager = new LinearLayoutManager(this);

        reccyclerView.setLayoutManager(new LinearLayoutManager(Patienthistory.this));
fetch();
    }
    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("PatientHistory").child(Currentuserid);
        FirebaseRecyclerOptions<patienthistorymodel> doctorhistoryoptions =
                new FirebaseRecyclerOptions.Builder<patienthistorymodel>()
                        .setQuery(query,
                                new SnapshotParser<patienthistorymodel>() {
                            @NonNull
                            @Override
                            public patienthistorymodel parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new patienthistorymodel(
                                        image=snapshot.child("doctorimage").getValue().toString(),
                                        name=snapshot.child("doctorname").getValue().toString(),
                                        number=snapshot.child("doctornumber").getValue().toString());
                            }
                        })
                        .build();
        adapter=new FirebaseRecyclerAdapter<patienthistorymodel,ViewHolder>(doctorhistoryoptions) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.doctorhistorylist, parent, false);

                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, final int position,@NonNull patienthistorymodel model) {
                holder.setTxtimageview(model.getDoctorimage());
                holder.setTxthospital(model.getDoctorname());
                holder.setTxtspeical(model.getDoctornumber());


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
