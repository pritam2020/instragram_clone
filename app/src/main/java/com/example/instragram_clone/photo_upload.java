package com.example.instragram_clone;


import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class photo_upload extends Fragment {

    ArrayList<Uri> list;


    public photo_upload(ArrayList<Uri> list) {
        this.list = list;
    }


    final FirebaseDatabase mref = FirebaseDatabase.getInstance("https://instragram-clone-7ff94.firebaseio.com/");
    FirebaseStorage riversRef = FirebaseStorage.getInstance("gs://instragram-clone-7ff94.appspot.com");
    DatabaseReference db1;
    DatabaseReference db2;
    StorageReference st;
    FirebaseAuth mauth;
    String user;
    String Date;
    private second_uploading_photo_adapter adapter2;
    private uploading_photo_adapter adapter1;
    private RecyclerView rv1;
    private RecyclerView rv2;
    private LinearLayoutManager lm1;
    private LinearLayoutManager lm2;
    ArrayList<String> image_link_fordatabase=new ArrayList<>();

    FloatingActionButton floatingActionButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.upload_photo, container, false);

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mauth = FirebaseAuth.getInstance();
        user = Objects.requireNonNull(mauth.getCurrentUser()).getUid();
        floatingActionButton = Objects.requireNonNull(getView()).findViewById(R.id.floatingActionButton);
        //big = getView().findViewById(R.id.upload_recycle);
        //sma = getView().findViewById(R.id.second_upload_recycle);
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        Calendar calendar = Calendar.getInstance();


        rv1=getView().findViewById(R.id.upload_recycle);
        rv2=getView().findViewById(R.id.second_upload_recycle);
        lm1 = new LinearLayoutManager(getContext());
        lm1.setOrientation(RecyclerView.HORIZONTAL);
        rv1.setLayoutManager(lm1);
        Toast.makeText(getContext(),"pp",Toast.LENGTH_SHORT).show();
        adapter1=  new uploading_photo_adapter(list);
        if(list.size()!=0){
            rv1.setAdapter( adapter1);}



        lm2 = new LinearLayoutManager(getContext());
        lm2.setOrientation(RecyclerView.HORIZONTAL);
        rv2.setLayoutManager(lm2);
        Toast.makeText(getContext(),"pp",Toast.LENGTH_SHORT).show();
        adapter2=  new second_uploading_photo_adapter(list);
        if(list.size()!=0){
            rv2.setAdapter( adapter2);}

        Date = dateFormat.format(calendar.getTime());

        db1 = mref.getReference("userinfo" + "/" + user + "/" + "pic_link"+"/"+"all_pic_links");
        db2 = mref.getReference("userinfo" + "/" + user + "/" + "pic_link"+"/"+"date_pic_links");
        st = riversRef.getReference(user + "/" + "all_pic");
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                upload(list);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void upload(final ArrayList<Uri> list) {
        Toast.makeText(getContext(),"uploading",Toast.LENGTH_SHORT).show();
        //int c=0;
//               new Thread(new Runnable() {
//                   @Override
//                   public void run() {
                       for(Uri photo:list){
                           // c++;
                           //final String s=Integer.toString(c);
                           double x=Math.random();
                           final String X=Double.toString(x);
                           final StorageReference ST=st.child(X);
                           UploadTask uploadTask = ST.putFile(photo);
                           uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                               @Override
                               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                   ST.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                       @Override
                                       public void onSuccess(Uri uri) {
                                           String po=uri.toString();
//
                                           db1.push().setValue(po);
                                           db2.child(Date).push().setValue(po);






                                       }
                                   });

                               }
                           });
                       }

//                   }
//               }).start();

      list.clear();
       image_link_fordatabase.clear();
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().remove(this).commit();

    }

}
