package com.example.firebase_auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity {
    Button signup;
    EditText name,mail,pass,conpass,usn;
    TextView signin;
    FirebaseAuth auth;
    CircleImageView Image;
    Uri imageuri;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String imageURI;
    ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        signin=findViewById(R.id.signin);
        signup=findViewById(R.id.signup);
        name=findViewById(R.id.regname);
        usn=findViewById(R.id.regusn);
        mail=findViewById(R.id.regmail);
        pass=findViewById(R.id.regpass);
        Image=findViewById(R.id.viewimage);
        conpass=findViewById(R.id.regconpass);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rname=name.getText().toString();
                String rmail=mail.getText().toString();
                String rpass=pass.getText().toString();
                String rconpass=conpass.getText().toString();
                String rusn=usn.getText().toString();
                if(TextUtils.isEmpty(rname)||TextUtils.isEmpty(rmail)||TextUtils.isEmpty(rpass)||TextUtils.isEmpty(rconpass)|| TextUtils.isEmpty(rusn)){
                    Toast.makeText(RegistrationActivity.this, "Enter valid credentials", Toast.LENGTH_SHORT).show();
                }
                if(!rpass.equals(rconpass))
                {
                    Toast.makeText(RegistrationActivity.this, "Password entered is different", Toast.LENGTH_SHORT).show();
                }
                if(rpass.length()<6){
                    Toast.makeText(RegistrationActivity.this, "Password Entered is too short", Toast.LENGTH_SHORT).show();
                }else{
                    auth.createUserWithEmailAndPassword(rmail,rpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                DatabaseReference reference= database.getReference().child("user").child(auth.getUid());
                                StorageReference storageReference= storage.getReference().child("upload").child(auth.getUid());
                                if(imageuri!=null){
                                    storageReference.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if(task.isSuccessful()){
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageURI=uri.toString();
                                                        Users user=new Users(auth.getUid(), rname,rmail,imageURI,rusn);
                                                        reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    startActivity(new Intent(RegistrationActivity.this,Admin_panel.class));
                                                                    finish();
                                                                }else{
                                                                    Toast.makeText(RegistrationActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }else{
                                    imageURI="https://firebasestorage.googleapis.com/v0/b/test-e07a7.appspot.com/o/profile.png?alt=media&token=1293edc7-7c9d-488b-8340-950d11636308";
                                    Users user=new Users(auth.getUid(),rname,rmail,imageURI,rusn);
                                    reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            startActivity(new Intent(RegistrationActivity.this,Admin_panel.class));
                                            finish();
                                        }else{
                                            Toast.makeText(RegistrationActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                        }

                                        }
                                    });
                                }
                            }else{
                                Toast.makeText(RegistrationActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this,Login_page.class));
                finish();
            }
        });
        Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if(data!=null){
                imageuri=data.getData();
                Image.setImageURI(imageuri);
            }
        }
    }
    @Override
    public void onBackPressed() {
        // Navigate back to the adminpanel activity
        Intent intent = new Intent(RegistrationActivity.this, Admin_panel.class);
        startActivity(intent);
        finish();
    }
}