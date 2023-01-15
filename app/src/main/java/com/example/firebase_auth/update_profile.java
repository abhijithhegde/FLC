package com.example.firebase_auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class update_profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        EditText regmail, newpass, oldpass;
        CircleImageView viewimage;

        newpass = findViewById(R.id.newpass);
        viewimage = findViewById(R.id.viewimage);
        regmail = findViewById(R.id.regmail);
        oldpass = findViewById(R.id.oldpass);

        String npass = newpass.getText().toString();
        String rmail = regmail.getText().toString();
        String opass = oldpass.getText().toString();


        Button button = findViewById(R.id.update);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(npass) || TextUtils.isEmpty(rmail) || TextUtils.isEmpty(opass)) {
                    Toast.makeText(update_profile.this, "Enter the valid details", Toast.LENGTH_SHORT).show();
                } else {
                    AuthCredential credential = EmailAuthProvider.getCredential(rmail, opass);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(npass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(update_profile.this, "Password Updated", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(update_profile.this, "Not sussessful", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }

        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(update_profile.this, Admin_panel.class);
        startActivity(intent);
        finish();
    }
}
