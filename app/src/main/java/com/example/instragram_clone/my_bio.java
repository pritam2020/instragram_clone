package com.example.instragram_clone;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class my_bio extends AppCompatActivity {
    EditText bio;
    MaterialCheckBox checkBox;
    Button next;
    FirebaseAuth mauth;
    String user;
    final FirebaseDatabase mref= FirebaseDatabase.getInstance("https://instragram-clone-7ff94.firebaseio.com/");
    DatabaseReference db;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bio);
        bio=findViewById(R.id.bio);
        checkBox=findViewById(R.id.materialCheckBox);
        next=findViewById(R.id.next);
        mauth=FirebaseAuth.getInstance();
        user= Objects.requireNonNull(mauth.getCurrentUser()).getUid();
        db=mref.getReference("userinfo/"+user);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Bio=bio.getText().toString().trim();
                if(!Bio.equals("")){
                db.child("bio").setValue(Bio);
                    Toast.makeText(getApplicationContext(), "bio added", Toast.LENGTH_SHORT).show();

                }
                if (checkBox.isChecked()){
                    db.child("anonimasity").setValue("yes");

                }
                Intent intent = new Intent(my_bio.this, Main2Activity.class);
                startActivity(intent);
                finishAffinity();

            }
        });


    }
}