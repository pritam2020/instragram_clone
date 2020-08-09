package com.example.instragram_clone;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Objects;
public class recent_logs extends Fragment {

    String auth;
    final FirebaseDatabase mref= FirebaseDatabase.getInstance("https://instragram-clone-7ff94.firebaseio.com/");
    DatabaseReference db;
    DatabaseReference dbb;
    String key ;
    LinearLayoutManager lm;
    recent_logs_adapter adapter;
    String TAG="recent log";
            RecyclerView rv;
    TextView text;
    String profile_pic;
    String username;
    String username1;
    DatabaseReference dbbb;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recent_logs,container,false);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"onActivityCreated");
        auth=FirebaseAuth.getInstance().getUid();
        rv= Objects.requireNonNull(getView()).findViewById(R.id.logs);
        db = mref.getReference("username");
        dbb=mref.getReference("user_details");
        dbbb=mref.getReference("userinfo");
        text = getView().findViewById(R.id.textView1111);
        Read2(auth);
        Read();
    }
    private void Read() {
        db.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                reacted_photo.clear();   //
//                photo.clear();   //
//                name.clear();    //
                ArrayList<String> name=new ArrayList<>();
                ArrayList<String> photo=new ArrayList<>();
                ArrayList<String> reacted_photo=new ArrayList<>();
                for(DataSnapshot ds:snapshot.getChildren()){

                    if(Objects.equals(ds.getKey(), auth)){
                        for(DataSnapshot DS:ds.getChildren()){
                            for(DataSnapshot dss:DS.getChildren()) {
                                if (dss.child("reactions").exists()) {
                                    String react_photo = dss.child("link").getValue(String.class);
                                    reacted_photo.add(react_photo);
                                    name.add("you reacted on " +DS.getKey()+" post");
                                  //  name_read("", Objects.requireNonNull(DS.getKey()));
                                    photo.add(DS.getKey());
                                    Log.d(TAG,String.valueOf(key));
                                    Log.d(TAG, String.valueOf(photo));
                                }
                            }
                        }
                    }
                    else {
                        for(DataSnapshot DS:ds.getChildren()){
                            //name_read(ds.getKey(),"");
                            if (Objects.equals(DS.getKey(), username)) {
                            for(DataSnapshot dss:DS.getChildren()) {
                                if (dss.child("reactions").exists()) {
                                        String react_photo = dss.child("link").getValue(String.class);
                                        reacted_photo.add(react_photo);
                                        Log.d(TAG,ds.getKey());
                                    Log.d(TAG, String.valueOf(photo));
                                    Read3(ds.getKey(),photo,name);
                                    }
                                }
                            }
                        }
                    }
                }
                lm = new LinearLayoutManager(getContext());
                rv.setLayoutManager(lm);
                adapter=  new recent_logs_adapter(reacted_photo,name,photo,this);
                if(name.size()!=0){
                    rv.setAdapter( adapter);}
                else {text.setVisibility(View.VISIBLE);
                    text.setText("no logs to show");}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private  void Read2(final String auth){
        dbb.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    if(Objects.equals(ds.getValue(), auth)){
                        username=ds.getKey();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private  void Read3(final String auth, final ArrayList<String> photo, final ArrayList<String> name){
        dbb.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    if(Objects.equals(ds.getValue(), auth)){
                        username1=ds.getKey();
                        photo.add(username1);
                        name.add(username1 +" reacted on your post");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }}
