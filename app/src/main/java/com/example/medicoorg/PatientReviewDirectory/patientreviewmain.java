package com.example.medicoorg.PatientReviewDirectory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicoorg.R;
import com.example.medicoorg.doctorappointmentdirectory.patientproposal;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class patientreviewmain extends AppCompatActivity {
    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter adapter;
    RecyclerView reccyclerView;
    DatabaseReference patientreviewref;
    String currentuserid,patientname,patientimage;
    FirebaseAuth mAuth;
   String visituserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientreviewmain);
        getSupportActionBar().setTitle("Patient Review");
        mAuth=FirebaseAuth.getInstance();
        currentuserid=mAuth.getCurrentUser().getUid();
        patientreviewref= FirebaseDatabase.getInstance().getReference().child("users").child(currentuserid);
        patientreviewref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           if(dataSnapshot.exists()){
               patientname=dataSnapshot.child("username").getValue().toString();
               patientimage=dataSnapshot.child("profileimage").getValue().toString();
           }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reccyclerView=findViewById(R.id.patientreviewrecyclerView);

        linearLayoutManager = new LinearLayoutManager(this);

        reccyclerView.setLayoutManager(new LinearLayoutManager(patientreviewmain.this));
        fetch();
    }
    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("patientreview").child(currentuserid);
        FirebaseRecyclerOptions<patientreviewmodel> options =
                new FirebaseRecyclerOptions.Builder<patientreviewmodel>()
                        .setQuery(query, new SnapshotParser<patientreviewmodel>() {
                            @NonNull
                            @Override
                            public patientreviewmodel parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new patientreviewmodel(
                                        snapshot.child("doctorhospital").getValue().toString(),
                                        snapshot.child("doctorimage").getValue().toString(),
                                        snapshot.child("doctorname").getValue().toString(),
                                        snapshot.child("doctornumber").getValue().toString());
                            }
                        })
                        .build();
        adapter=new FirebaseRecyclerAdapter<patientreviewmodel,ViewHolder>(options) {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.patientreviewlist, parent, false);

                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull patientreviewmodel patientreviewmodel) {

                holder.setTxtimageview(patientreviewmodel.getDoctorimage());
                holder.setTxthospital(patientreviewmodel.getDoctorhospital());
                holder.setTxtusername(patientreviewmodel.getDoctorname());
                holder.setTxtspeical(patientreviewmodel.getDoctornumber());
               visituserid=getRef(position).getKey();


                holder.patientreviewbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text=holder.edittext.getText().toString();
                        if(text.isEmpty()){
                            Toast.makeText(patientreviewmain.this,"Please add a review", Toast.LENGTH_SHORT).show();
                        }
                        else{

                            DatabaseReference doctorreviewref=FirebaseDatabase.getInstance().getReference().child("Doctorreview").child(visituserid).child(currentuserid);
                            HashMap usermap=new HashMap();
                            usermap.put("patientname",patientname);
                            usermap.put("patientimage",patientimage);
                            usermap.put("patientreview",text);
                            doctorreviewref.updateChildren(usermap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful()){
                                      DatabaseReference deletereference=FirebaseDatabase.getInstance().getReference()
                                              .child("patientreview").child(currentuserid).child(visituserid);
                                        deletereference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                                    appleSnapshot.getRef().removeValue();
                                                    Toast.makeText(patientreviewmain.this,"Thanks for feedback", Toast.LENGTH_SHORT).show();
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                }
                            });


                        }
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
        public TextView txthospital;
        public TextView txtspecial;
        public TextView txtusername;
        public Button patientreviewbtn;
        public  EditText edittext;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root=itemView.findViewById(R.id.patientreviewlist_root);
            txtimgview=itemView.findViewById(R.id.patientreviewlistimageview);
            txthospital=itemView.findViewById(R.id.patientreviewlisthospitalname);
            txtspecial=itemView.findViewById(R.id.patientreviewlistspeacialist);
            txtusername=itemView.findViewById(R.id.patientreviewlistname);
            patientreviewbtn=itemView.findViewById(R.id.patientreviewlistbutton);
           edittext=itemView.findViewById(R.id.patientreviewlistedittext);


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
