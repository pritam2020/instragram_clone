package com.example.instragram_clone;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static java.security.AccessController.getContext;


public class Main2Activity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST_CODE = 12;
    FirebaseAuth mauth;
    String user;
    ArrayList<Uri> list = new ArrayList<>();
    final FirebaseDatabase mref = FirebaseDatabase.getInstance("https://instragram-clone-7ff94.firebaseio.com/");
    FirebaseStorage riversRef = FirebaseStorage.getInstance("gs://instragram-clone-7ff94.appspot.com");
    DatabaseReference dr;
    StorageReference st;
    public TextView notify_number;
    DatabaseReference ppp ;
    int c = 0;
    int a = 0;
    boolean B;
    String TAG="hellow";
    int p=-2;
    ArrayList<Integer>arrayList=new ArrayList<>();
    FileOutputStream fOut;
    FileInputStream fin;
    int k;
    String temp="";
    SharedPreferences pref;
    Editor editor;
    DataSnapshot check;
    ValueEventListener ll;
    ArrayList<String> ss=new ArrayList<>();


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getApplicationContext(), "result_1", Toast.LENGTH_SHORT).show();
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Toast.makeText(getApplicationContext(), "result_1.1", Toast.LENGTH_SHORT).show();
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                Uri imageUri;
                Toast.makeText(getApplicationContext(), "result_2", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < count; i++) {
                    imageUri = data.getClipData().getItemAt(i).getUri();
                    //imageUri_tostring=imageUri.toString();
                    list.add(imageUri);
                    //do something with the image (save it to some directory or whatever you need to do with it here)
                }
            } else if (data.getData() != null) {
                // String imagePath = data.getData().getPath();
                Toast.makeText(getApplicationContext(), "result_3", Toast.LENGTH_SHORT).show();
                //do something with the image (save it to some directory or whatever you need to do with it here)
            }

//            Filepath = data.getData();
//            String po=Filepath.toString();
//            Picasso.get().load(Filepath).into(circleImageView);


            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.framelayout, new photo_upload(list));
            //ft.addToBackStack(null);
            ft.commit();


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        //inflater.inflate(R.menu.posting_photos,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                // pb.setVisibility(View.VISIBLE);
                // cv.setVisibility(View.VISIBLE);
                mauth.signOut();
                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                //pb.setVisibility(View.INVISIBLE);
                //cv.setVisibility(View.INVISIBLE);
                Toast t = Toast.makeText(getApplicationContext(), "signed out", Toast.LENGTH_SHORT);
                t.show();

                return true;

            case R.id.item2:
                //pb.setVisibility(View.VISIBLE);
                //cv.setVisibility(View.VISIBLE);
                FirebaseUser current_user = mauth.getCurrentUser();
                assert current_user != null;
                current_user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent antent = new Intent(Main2Activity.this, MainActivity.class);
                        antent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(antent);
                        dr = mref.getReference("userinfo/" + user);
                        dr.removeValue();
                        st = riversRef.getReference(user);
                        st.delete();
                        //pb.setVisibility(View.INVISIBLE);
                        //cv.setVisibility(View.INVISIBLE);
                        Toast t = Toast.makeText(getApplicationContext(), "Acount Deleted", Toast.LENGTH_SHORT);
                        t.show();

                    }
                });

                //showFileChooser();


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent=new Intent(Main2Activity.this,notificationService.class);
        startService(intent);
        notify_number = findViewById(R.id.notify_number);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
       a= pref.getInt("notification_number",-1);
       notify_number.setText(Integer.toString(a));
       if(a!=0){
       notify_number.setVisibility(View.VISIBLE);}


        Button add_post = findViewById(R.id.button4);
        add_post.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        mauth = FirebaseAuth.getInstance();
        Button newsfeed = findViewById(R.id.button);
        Button search = findViewById(R.id.button2);
        user = Objects.requireNonNull(mauth.getCurrentUser()).getUid();
        ppp=mref.getReference("user_id"+"/"+user);
        Button myinfo = findViewById(R.id.button3);
        Button recent_logs = findViewById(R.id.button5);

        CHECK();
//        try {
//
//            fin = openFileInput("file");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//
//                while (true) {
//                    try {
//                        if ((k = fin.read()) != -1) break;
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    temp = temp + Character.toString((char)k);
//                }
//        try {
//            fin.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            fOut = openFileOutput("file",MODE_WORLD_READABLE);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        for (int i=0;i<=200;i++){
            p=p+3;
            arrayList.add(p);
        }





        //notify_number.setText(temp);
//        a=Integer.parseInt(temp);
//        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.framelayout,new newsfeeds());
//        //ft.addToBackStack(null);
//
//        ft.commit();
        newsfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.framelayout, new newsfeeds());
                //ft.addToBackStack(null);

                ft.commit();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getSupportActionBar()).hide();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.framelayout, new searchActivity());
                //ft.addToBackStack(null);
                ft.commit();

            }
        });

        myinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.framelayout, new myInfo(user));
                //ft.addToBackStack(null);
                ft.commit();
            }
        });

        recent_logs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify_number.setVisibility(View.INVISIBLE);
                a = 0;
                Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(Main2Activity.this)).getSupportActionBar()).hide();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.framelayout, new recent_logs());
                //ft.addToBackStack(null);
                ft.commit();
            }
        });

//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        finish();
//    }
    }

    private void CHECK() {
       ll= ppp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    String key=ds.getKey();
                    ss.add(key);}
//                check=snapshot;
                nofify(true);
                ppp.removeEventListener(ll);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select an image "), PICK_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: 1");

        editor.putInt("notification_number",a);
        editor.commit();
        }


    private void nofify(boolean b) {
        B = b;
                ppp.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        String key=snapshot.getKey();
                        if(!ss.contains(key)) {
                            a++;
                            notify_number.setVisibility(View.VISIBLE);
                            notify_number.setText(Integer.toString(a));
                        }


                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                       // p=p+3;
//                        c++;
//
//                        if(arrayList.contains(c)) {
//                            a++;
//                            notify_number.setText(Integer.toString(a));
//                            notify_number.setVisibility(View.VISIBLE);
//                            String str = Integer.toString(a);
//                            try {
//                                fOut.write(str.getBytes());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            try {
//                                fOut.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
 //                       }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: 1");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: 1");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: 1");


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: 1");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: 1");
    }
}




