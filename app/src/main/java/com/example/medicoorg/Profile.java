package com.example.medicoorg;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.medicoorg.R.color.main_color;

public class Profile extends AppCompatActivity {
String user_name;
TextInputLayout speciallayout,hospitallayout;
TextInputEditText name,number,specialization,hospital;
Button profilebuttn;
     DatabaseReference doctorref;
CircleImageView circleImageView;
    int GALLERY_PICK=1234;
    String image;
    String empty="";
    FirebaseAuth mAuth;
    StorageReference userprofileimageRef;
    DatabaseReference userRef ;
    String Current_user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent=getIntent();
        getSupportActionBar().hide();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        user_name = intent.getStringExtra("user");
        Log.e("user,",user_name);
speciallayout=findViewById(R.id.specializedinsub);
hospitallayout=findViewById(R.id.hospitaaalname);
        name=findViewById(R.id.nameedittext);
specialization=findViewById(R.id.specializedinedittext);
hospital=findViewById(R.id.hospitalnameedittext);

        number=findViewById(R.id.numberedittext);
        profilebuttn=findViewById(R.id.buttonprofile);
        circleImageView=findViewById(R.id.circularrrrimageview);
        mAuth=FirebaseAuth.getInstance();

        userprofileimageRef = FirebaseStorage.getInstance().getReference().child("profileimage");
        Current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        doctorref=FirebaseDatabase.getInstance().getReference("Doctors").child(Current_user_id);
        userRef= FirebaseDatabase.getInstance().getReference().child("users").child(Current_user_id);
        if(TextUtils.equals(user_name,"Patient")){
            speciallayout.setHintTextColor(ColorStateList.valueOf(getResources().getColor(main_color)));
            hospitallayout.setHintTextColor(ColorStateList.valueOf(getResources().getColor(main_color)));
            speciallayout.setHint("Specialist you prefer");
            hospitallayout.setHint("Which Hospital you prefer");

        }
        else{
            speciallayout.setHintTextColor(ColorStateList.valueOf(getResources().getColor(main_color)));
            hospitallayout.setHintTextColor(ColorStateList.valueOf(getResources().getColor(main_color)));
            speciallayout.setHint("Specialization i.e Physician etc");
            hospitallayout.setHint("Hospital you worked in");


        }
        profilebuttn.setOnClickListener(

                new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(name.getText().toString().isEmpty()){
                    name.setError("Please enter username");
                }
                else if(hospital.getText().toString().isEmpty()){
                    hospital.setError("Please enter hospital");
                }
                else if(specialization.getText().toString().isEmpty()){
                    specialization.setError("Please enter specialization");
                }
               else if(number.getText().toString().isEmpty()&& number.getText().toString().length() <11){
number.setError("Please enter number with length 11");
               }
                else if(image==null) {
Toast.makeText(Profile.this,"please uploadimage",Toast.LENGTH_SHORT).show();
                }

                else{
                    HashMap usermap=new HashMap();
                    usermap.put("username",user_name+"  "+name.getText().toString());
                    usermap.put("number",number.getText().toString());
                    usermap.put("specialized",specialization.getText().toString());
                    usermap.put("hospital",hospital.getText().toString());
                    userRef.updateChildren(usermap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            Toast.makeText(Profile.this,"Data enter successfully",Toast.LENGTH_SHORT).show();
                            Intent mainintent=new Intent(Profile.this,MainActivity.class);
                            mainintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainintent);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Profile.this,"Data enter failed",Toast.LENGTH_SHORT).show();

                        }
                    });
                    if(user_name.equals("Doctor")){
                    doctorref.updateChildren(usermap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {


                        }
                    });}

                }
            }
        });


        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryintent=new Intent();
                galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent, GALLERY_PICK);

            }
        });
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.hasChild("profileimage")) {
                         image= dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.man).into(circleImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }



        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1234 ){

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
            Uri ImageUri=data.getData();
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                final ProgressDialog progress = new ProgressDialog(Profile.this);
                final Timer t = new Timer();
                progress.setTitle("Loading");
                progress.setMessage("Wait while uploading...");
                progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                progress.show();
                t.schedule(new TimerTask() {
                    public void run() {
                        progress.dismiss();
                        // when the task active then close the dialog
                        t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                    }
                }, 6000);
                Uri resulturi=result.getUri();
                final StorageReference filepath=userprofileimageRef.child(Current_user_id +".jpg");
                filepath.putFile(resulturi).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful()){

                            /*final String downloadurl= task.getResult().getStorage().getDownloadUrl().toString();*/
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String downloadurl=uri.toString();


                                    userRef.child("profileimage").setValue(downloadurl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {


                                            Toast.makeText(Profile.this,"image stored",Toast.LENGTH_LONG).show();
                                            if(user_name.equals("Doctor")){
                                                doctorref.child("profileimage").setValue(downloadurl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                    }
                                                });

                                            }

                                        }
                                    });
                                }
                            });

                        }
                    }


                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {

                    }
                });

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
