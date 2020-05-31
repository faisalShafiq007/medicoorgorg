package com.example.medicoorg;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class settings extends AppCompatActivity {
    EditText name,number,specialized,hname;
    Button savesettings;
    String fullname,hospitalname ,phonenumber1 ,specializationstring;
CircleImageView profileimgae;
    StorageReference userprofileimageRef ;
    DatabaseReference settingsuserref;
    String Currentuserid;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Settings");
        mAuth=FirebaseAuth.getInstance();
        Currentuserid=mAuth.getCurrentUser().getUid();
        name=findViewById(R.id.settings_username);
        number=findViewById(R.id.settings_number);
        specialized=findViewById(R.id.settings_specialist);
        hname=findViewById(R.id.settings_hospitalname);
        savesettings=findViewById(R.id.settings_button);
        profileimgae=findViewById(R.id.settings_profile_image);
        userprofileimageRef= FirebaseStorage.getInstance().getReference().child("profileimage");
        settingsuserref= FirebaseDatabase.getInstance().getReference().child("users").child(Currentuserid);
        settingsuserref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                     fullname = dataSnapshot.child("username").getValue().toString();
                    hospitalname = dataSnapshot.child("hospital").getValue().toString();
                     phonenumber1 = dataSnapshot.child("number").getValue().toString();
                     specializationstring= dataSnapshot.child("specialized").getValue().toString();
                    String image=dataSnapshot.child("profileimage").getValue().toString();
                    Picasso.get().load(image).placeholder(R.drawable.man).into(profileimgae);
                    name.setText(fullname);
                    number.setText(phonenumber1);
                    specialized.setText(specializationstring);
                    hname.setText(hospitalname);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        savesettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validateaccountinfo();
            }
        });
        profileimgae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryintent=new Intent();
                galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent, 158);
            }


        });
        //retrieving image from storage
        settingsuserref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String image=dataSnapshot.child("profileimage").getValue().toString();
                    Picasso.get().load(image).placeholder(R.drawable.profile).into(profileimgae);

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==158 ){

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
            Uri ImageUri=data.getData();
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                Uri resulturi=result.getUri();
                final StorageReference filepath=userprofileimageRef.child( Currentuserid+".jpg");
                filepath.putFile(resulturi).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful()){
                            final ProgressDialog progress = new ProgressDialog(settings.this);
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
                            }, 4000);
                            /*final String downloadurl= task.getResult().getStorage().getDownloadUrl().toString();*/
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadurl=uri.toString();
                                    settingsuserref.child("profileimage").setValue(downloadurl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(settings.this,"image stored",Toast.LENGTH_LONG).show();


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
    }

    private void Validateaccountinfo() {
        if(hospitalname.isEmpty()){
            hname.setError("please enter hospital name");
        }
        else if(phonenumber1.isEmpty()){
            number.setError("please enter number");
        }
        else if(specializationstring.isEmpty()){
            specialized.setError("please enter Major");

        }
        else if(fullname.isEmpty()){
            name.setError("please enter name");
        }
        else {
           String uname = name.getText().toString();
            String uhospitalname = hname.getText().toString();
            String uphonenumber1 = number.getText().toString();
            String uspecializationstring= specialized.getText().toString();
            HashMap account=new HashMap();
            account.put("username",uname);
            account.put("hospital",uhospitalname);
            account.put("number",uphonenumber1);
            account.put("specialized",uspecializationstring);
            settingsuserref.updateChildren(account).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    Toast.makeText(settings.this,"Data Updated",Toast.LENGTH_LONG).show();
                }
            });
        }

    }

}