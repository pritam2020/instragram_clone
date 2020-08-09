package com.example.instragram_clone;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class createActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    private Uri Filepath;
    private String user;
    CircleImageView circleImageView;
    private static final int PICK_IMAGE_REQUEST_CODE = 123;
    final FirebaseDatabase mref= FirebaseDatabase.getInstance("https://instragram-clone-7ff94.firebaseio.com/");
    FirebaseStorage riversRef= FirebaseStorage.getInstance("gs://instragram-clone-7ff94.appspot.com");
    DatabaseReference dr;
    StorageReference st;
    DatabaseReference dR;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Filepath = data.getData();
            Picasso.get().load(Filepath).into(circleImageView);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        mAuth = FirebaseAuth.getInstance();
        Button create_account=findViewById(R.id.b2);
        final CardView pd=findViewById(R.id.card1);
        final ProgressBar c=findViewById(R.id.progress1);
        final EditText email=findViewById(R.id.t2);
        final EditText password=findViewById(R.id.t3);
        final EditText username_entered=findViewById(R.id.t1);







        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username=username_entered.getText().toString().trim();
                final String Email=email.getText().toString().trim();
                final String Password=password.getText().toString().trim();
                if(!Email.equals("") && !Password.equals("")&&!username.equals("")){
                    pd.setVisibility(View.VISIBLE);
                    c.setVisibility(View.VISIBLE);
                mAuth.fetchSignInMethodsForEmail(Email).addOnSuccessListener(new OnSuccessListener<SignInMethodQueryResult>() {
                    @Override
                    public void onSuccess(SignInMethodQueryResult signInMethodQueryResult) {
                        List e=signInMethodQueryResult.getSignInMethods();
                        if(Objects.requireNonNull(e).isEmpty()){
                            mAuth.createUserWithEmailAndPassword(Email, Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    user = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                                    Toast t = Toast.makeText(getApplicationContext(), "created", Toast.LENGTH_SHORT);
                                    t.show();
                                    dr = mref.getReference("userinfo" + "/" + user);
                                    dR=mref.getReference("user_details" + "/" + username);
                                    st = riversRef.getReference(user + "/" + "profile_pic");
                                    dr.child("username").setValue(username);
                                    dr.child("userid").setValue(user);
                                    dR.setValue(user);
                                    if (Filepath != null) {
                                    UploadTask uploadTask = st.putFile(Filepath);
                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            st.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    String po=uri.toString();

                                                    dr.child("profile_pic").setValue(po);

                                                    Toast tv = Toast.makeText(getApplicationContext(), "url", Toast.LENGTH_SHORT);
                                                    tv.show();


                                                }
                                            });

                                        }
                                    });
                                    }
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    pd.setVisibility(View.INVISIBLE);
                                    c.setVisibility(View.INVISIBLE);
                                    updateui(user);

                                }
                            });
                        }else {
                            Toast t=Toast.makeText(getApplicationContext(),"already exists",Toast.LENGTH_SHORT);
                            t.show();
                            pd.setVisibility(View.INVISIBLE);
                            c.setVisibility(View.INVISIBLE);
                        }


                    }
                });
                }else{
                    Toast.makeText(createActivity.this, "texts fiends cannot be empty", Toast.LENGTH_SHORT).show();
                }

            }
        });


        circleImageView = findViewById(R.id.imageView2);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();

            }
        });



    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select an image "), PICK_IMAGE_REQUEST_CODE);
    }
    private void updateui( FirebaseUser us) {

        if(us!=null){
            Intent intent = new Intent(createActivity.this, my_bio.class);
            startActivity(intent);
            finishAffinity();
        }
    }
}
