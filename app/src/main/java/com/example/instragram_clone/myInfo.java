package com.example.instragram_clone;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class myInfo extends Fragment {
    String passed_user;
    public myInfo(String passed_user){
        this.passed_user=passed_user;
    }
    final FirebaseDatabase mref= FirebaseDatabase.getInstance("https://instragram-clone-7ff94.firebaseio.com/");
    FirebaseAuth mauth;
    my_recycler adapter;
    private RecyclerView rv;
    CardView cv;
    ProgressBar pb;
    ImageView profile_pic_image;
    String user;
    DatabaseReference passed_user_database;
    int i=0;
    CardView follow_card;
    DatabaseReference friendlist_of_passeduser;
    DatabaseReference friendlist_of_currentuser;
    DatabaseReference user_details;
    String current_username;
    String passed_username;
    Button follow_unfollow;
    TextView following;
    TextView follower;
    TextView bio;
    private Uri Filepath;
    FirebaseStorage riversRef= FirebaseStorage.getInstance("gs://instragram-clone-7ff94.appspot.com");
    StorageReference st;
    private static final int PICK_IMAGE_REQUEST_CODE = 123;
    ArrayList<String> all_photo_link_list=new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Filepath = data.getData();

            Glide.with(Objects.requireNonNull(getContext())).load(Filepath).into(profile_pic_image);
            st = riversRef.getReference(passed_user + "/" + "profile_pic");
            UploadTask uploadTask=st.putFile(Filepath);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    st.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String po=uri.toString();

                            passed_user_database.child("profile_pic").setValue(po);

//

                        }
                    });

                }
            });



        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.myinfo,container,false);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)//
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).show();
        follower = Objects.requireNonNull(getView()).findViewById(R.id.follower_mun);
        following = getView().findViewById(R.id.following_num);
        bio=getView().findViewById(R.id.textbio);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).show();//
        mauth = FirebaseAuth.getInstance();
        rv= Objects.requireNonNull(getView()).findViewById(R.id.myrecyclerView);//
        cv=getView().findViewById(R.id.card3);
        pb=getView().findViewById(R.id.progress3);
        follow_card=getView().findViewById(R.id.cardView);
        user = Objects.requireNonNull(mauth.getCurrentUser()).getUid();//
        follow_unfollow = getView().findViewById(R.id.button10);
        friendlist_of_passeduser=mref.getReference("userinfo"+"/"+passed_user+"/"+"friendlist"+"/"+"follower");
        friendlist_of_currentuser=mref.getReference("userinfo" + "/" + user + "/" + "friendlist" + "/" + "following");
        user_details=mref.getReference("user_details");
        profile_pic_image = getView().findViewById(R.id.profile_pic_imageView);
        passed_user_database = mref.getReference("userinfo"+"/"+passed_user);

//

        if(passed_user.equals(user)){
            follow_unfollow.setVisibility(View.INVISIBLE);
            follow_card.setVisibility(View.INVISIBLE);
            data();
        }
        else {
            data();
        }
        read();
        follow_unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              if(follow_unfollow.getText().toString().equals("follow")){
                  friendlist_of_currentuser.child(passed_username).setValue(passed_username);
                  friendlist_of_passeduser.child(current_username).setValue(current_username);

              }
              else {

                  friendlist_of_currentuser.child(passed_username).removeValue();
                  friendlist_of_passeduser.child(current_username).removeValue();

              }
            }
        });


        profile_pic_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passed_user.equals(user)){
                    showFileChooser();
                }
            }
        });

        bio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), my_bio.class);
                startActivity(intent);
            }
        });
    }
    private void data() {
        user_details.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    String username=ds.getKey();
                    String id=ds.getValue(String.class);
                    assert id != null;//
                    if(id.equals(user)){
                        current_username=username;
                    }
                    if(id.equals(passed_user)){
                        passed_username=username;
                        Objects.requireNonNull(getActivity()).setTitle(passed_username);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void read() {
        pb.setVisibility(View.VISIBLE);
        cv.setVisibility(View.VISIBLE);

                passed_user_database.addValueEventListener(new ValueEventListener() {

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)//
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        DataSnapshot S=snapshot.child("friendlist").child("following");
                        DataSnapshot ss=snapshot.child("friendlist").child("follower");
                        long number_of_follower=ss.getChildrenCount();
                        long number_of_following=S.getChildrenCount();
                        follower.setText(Long.toString(number_of_follower));
                        following.setText(Long.toString(number_of_following));

                        if(number_of_follower!=0){
                            for(DataSnapshot ds:ss.getChildren()){
                                String name=ds.getValue(String.class);
                                assert name != null;//
                                if(name.equals(current_username)) {
                                    follow_unfollow.setText(R.string.unfollow);

                                    break;
                                }
                                else {
                                    follow_unfollow.setText(R.string.follow);
                                }
                            }
                        }else {
                            follow_unfollow.setText(R.string.follow);
                        }
//

                        for(DataSnapshot ds:snapshot.getChildren()){
                            String key=ds.getKey();
                            assert key != null;//
                            if(key.equals("username")){

                                String username=ds.getValue(String.class);

                            }
                            if(key.equals("profile_pic")){

                                String profile_pic =ds.getValue(String.class);
                                Picasso.get().load(profile_pic).into(profile_pic_image);
                            }
                            if(key.equals("pic_link")){

                                for(DataSnapshot dd:ds.getChildren()){
                                    String key1=dd.getKey();
                                    assert key1 != null;//
                                    if(key1.equals("all_pic_links")){

                                        for(DataSnapshot DD:dd.getChildren()){
                                            String all_photo_link=DD.getValue(String.class);
                                            all_photo_link_list.add(all_photo_link);
                                        }
                                    }
                                }
                            }
                            if(key.equals("bio")){
                                String bio_d=ds.getValue(String.class);
                                bio.setText(bio_d);
                            }
                        }
                        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),3,LinearLayoutManager.VERTICAL,false);

                        rv.setLayoutManager(gridLayoutManager);

                        adapter=  new my_recycler(all_photo_link_list,i,passed_user,this);
                        if(all_photo_link_list.size()!=0){
                            rv.setAdapter( adapter);}
                        pb.setVisibility(View.INVISIBLE);
                        cv.setVisibility(View.INVISIBLE);

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

private void showFileChooser() {
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent, "select an image "), PICK_IMAGE_REQUEST_CODE);
}



}
