package com.example.messenger;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.play.integrity.internal.v;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.View;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    RecyclerView mainUserRecyclerview;
    UserAdapter adapter;
    FirebaseDatabase database;
    ArrayList<Users> userArrayList;
    ImageView imglogout;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        DatabaseReference refrence = database.getReference().child("user");

        userArrayList = new ArrayList<Users>();

        refrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    Users users = dataSnapshot.getValue(Users.class);
                    userArrayList.add(users);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        imglogout = findViewById(R.id.logoutimg);
        imglogout.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                Dialog dialog =new Dialog(MainActivity.this,R.style.dialog);
                dialog.setContentView(R.layout.dialog_layout);

                Button no,yes;
                yes= dialog.findViewById(R.id.yet);
                no= dialog.findViewById(R.id.yesbnt);
                yes.setOnClickListener(new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(android.view.View view) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent= new Intent(MainActivity.this,login.class);
                        startActivity(intent);
                    }
                });
                no.setOnClickListener(new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(android.view.View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });





        mainUserRecyclerview = findViewById(R.id.mainUserRecyclerview);
        mainUserRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(MainActivity.this,userArrayList);
        mainUserRecyclerview.setAdapter(adapter);

        if(auth.getCurrentUser() == null){
           Intent intent=new Intent(MainActivity.this,login.class);
            startActivity(intent);
            imglogout = findViewById(R.id.logoutmainimg);

            }
    }
}


