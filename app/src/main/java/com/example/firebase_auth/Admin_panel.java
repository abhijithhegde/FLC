package com.example.firebase_auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Admin_panel extends AppCompatActivity {
    ImageView logouthome;
    RecyclerView recyclerView;
    Adaptor adaptor;
    TextView name;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        //getSupportActionBar().hide();
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("user"), Users.class)
                        .build();
        adaptor = new Adaptor(options);
        recyclerView.setAdapter(adaptor);


//        logouthome = findViewById(R.id.logouthome);
//        logouthome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Dialog dialog = new Dialog(Admin_panel.this, R.style.Dialoge);
//                dialog.setContentView(R.layout.dialog_layout);
//                TextView yesbtn, nobtn;
//                yesbtn = dialog.findViewById(R.id.yesbtn);
//                nobtn = dialog.findViewById(R.id.nobtn);
//                yesbtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        FirebaseAuth.getInstance().signOut();
//                        startActivity(new Intent(Admin_panel.this, Login_page.class));
//                        finish();
//                    }
//                });
//                nobtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//
//                    }
//                });
//                dialog.show();
//            }
//        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        adaptor.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adaptor.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        getMenuInflater().inflate(R.menu.logout,menu);
        MenuItem item1 = menu.findItem(R.id.search);
        MenuItem item2 = menu.findItem(R.id.logout);
        MenuItem item3 = menu.findItem(R.id.update);

        item2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Admin_panel.this, Login_page.class));
                finish();
                return false;
            }
        });

        item3.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(Admin_panel.this,update_profile.class));
                finish();
                return false;
            }
        });

        SearchView searchView = (SearchView) item1.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                txtSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                txtSearch(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void txtSearch(String str) {
        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("user").orderByChild("name").startAt(str).endAt(str + "~"), Users.class)
                        .build();
        adaptor = new Adaptor(options);
        adaptor.startListening();
        recyclerView.setAdapter(adaptor);
    }

}