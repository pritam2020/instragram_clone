package com.example.instragram_clone;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText password;
    EditText email;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        password = findViewById(R.id.button7);
        email = findViewById(R.id.button6);
        Button create_account=findViewById(R.id.button4);
        Button sign_in=findViewById(R.id.button5);
        final ProgressBar pd=findViewById(R.id.progress);
        final CardView c=findViewById(R.id.card);



        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,createActivity.class);
                startActivity(intent);
            }
        });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Email=email.getText().toString();
                final String Password=password.getText().toString();

                if(!Email.equals("") && !Password.equals("")) {
                    pd.setVisibility(View.VISIBLE);
                    c.setVisibility(View.VISIBLE);


                    mAuth.fetchSignInMethodsForEmail(Email).addOnSuccessListener(new OnSuccessListener<SignInMethodQueryResult>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onSuccess(SignInMethodQueryResult signInMethodQueryResult) {

                            List ed = signInMethodQueryResult.getSignInMethods();
                            if (Objects.requireNonNull(ed).isEmpty()) {
                                Toast tb = Toast.makeText(getApplicationContext(), "not registered", Toast.LENGTH_SHORT);
                                tb.show();
                                pd.setVisibility(View.INVISIBLE);
                                c.setVisibility(View.INVISIBLE);
                            } else {

                                mAuth.signInWithEmailAndPassword(Email, Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        pd.setVisibility(View.INVISIBLE);
                                        c.setVisibility(View.INVISIBLE);
                                        updateui(user);
                                        Toast t = Toast.makeText(getApplicationContext(), "signed in", Toast.LENGTH_SHORT);
                                        t.show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pd.setVisibility(View.INVISIBLE);
                                        c.setVisibility(View.INVISIBLE);
                                        Toast tk = Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT);
                                        tk.show();
                                    }
                                });
                            }
                        }
                    });
                }else{
                    Toast.makeText(MainActivity.this, "texts fiends cannot be empty", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void updateui( FirebaseUser us) {

        if(us!=null){
            Intent intent = new Intent(MainActivity.this, Main2Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        updateui(currentUser);


    }





}





