package com.example.medicoorg.Doctorreview;

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

import com.example.medicoorg.PatientReviewDirectory.patientreviewmain;
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

public class Doctorreview extends AppCompatActivity {
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
        setContentView(R.layout.activity_doctorreview);
        getSupportActionBar().setTitle("Doctor Review");
        mAuth=FirebaseAuth.getInstance();
        currentuserid=mAuth.getCurrentUser().getUid();
        reccyclerView=findViewById(R.id.doctorreviewrecyclerView);

        linearLayoutManager = new LinearLayoutManager(this);

        reccyclerView.setLayoutManager(new LinearLayoutManager(Doctorreview.this));
        fetch();
    }
    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Doctorreview").child(currentuserid);
        FirebaseRecyclerOptions<Doctorreviewmodel> options =
                new FirebaseRecyclerOptions.Builder<Doctorreviewmodel>()
                        .setQuery(query, new SnapshotParser<Doctorreviewmodel>() {
                            @NonNull
                            @Override
                            public Doctorreviewmodel parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new Doctorreviewmodel(
                                        snapshot.child("patientimage").getValue().toString(),
                                        snapshot.child("patientname").getValue().toString(),
                                        snapshot.child("patientreview").getValue().toString());

                            }
                        })
                        .build();
        adapter=new FirebaseRecyclerAdapter<Doctorreviewmodel,ViewHolder>(options) {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.doctorreviewlist, parent, false);

                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull Doctorreviewmodel doctorreviewmodel) {

                holder.setTxtimageview(doctorreviewmodel.getPatientimage());
                holder.setTxtspeical(doctorreviewmodel.getPatientreview());
                holder.setTxtusername(doctorreviewmodel.getPatientname());
                visituserid=getRef(position).getKey();

            }


        };
        reccyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout root;
        public CircleImageView txtimgview;

        public TextView txtview;
        public TextView txtusername;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root=itemView.findViewById(R.id.doctorreviewlist_root);
            txtimgview=itemView.findViewById(R.id.doctorreviewlistimageview);
            txtview=itemView.findViewById(R.id.docotorreviewtextview);
            txtusername=itemView.findViewById(R.id.doctorreviewlistname);


        }
        public void setTxtimageview(String stringd) {
            Picasso.get().load(stringd).into(txtimgview);
        }

        public void setTxtspeical(String stringb) {
            txtview.setText(stringb);
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
