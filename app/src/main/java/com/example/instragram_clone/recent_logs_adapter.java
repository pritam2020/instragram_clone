package com.example.instragram_clone;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class recent_logs_adapter extends RecyclerView.Adapter<recent_logs_adapter.MyViewHolder> {
    ArrayList<String> reacted_photo;
    ValueEventListener valueEventListener;
    ArrayList<String> name;
    ArrayList<String> photo;
    final FirebaseDatabase mref= FirebaseDatabase.getInstance("https://instragram-clone-7ff94.firebaseio.com/");
    DatabaseReference dbbb;
    DatabaseReference dbb;
    String profile_pic;
    String key ;

    public recent_logs_adapter(ArrayList<String> reacted_photo, ArrayList<String> name, ArrayList<String> photo,ValueEventListener valueEventListener) {
        this.reacted_photo=reacted_photo;
        this.name=name;
        this.valueEventListener=valueEventListener;
        this.photo=photo;
    }

    @NonNull
    @Override
    public recent_logs_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.recent_logs_recyler,parent,false);
        return new recent_logs_adapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull recent_logs_adapter.MyViewHolder holder, int position) {
        dbb=mref.getReference("user_details");
        dbbb=mref.getReference("userinfo");
       String Name=name.get(position);
       String link=photo.get(position);
        Log.d(TAG, "onBindViewHolder:"+link);
       holder.nname.setText(Name);
       name_read(link,holder);

    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nname;
        CircleImageView circleImageViewl;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nname=itemView.findViewById(R.id.textViewnull);
            circleImageViewl=itemView.findViewById(R.id.textView231l);
        }
    }
    private void name_read(final String username, final MyViewHolder holder){
        // if((!username.equals("")&&user_id.equals(""))||(username.equals("")&&!user_id.equals(""))){
        dbb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
//                    if(username.equals("")&&!user_id.equals("")) {
//                        if (user_id.equals(ds.getValue(String.class))) {
//
//                            key = ds.getKey();
//                        }
               //     }
                if(!username.equals("")) {
                        if (username.equals(ds.getKey())) {

                            key = ds.getValue(String.class);
                            photoo(key,holder);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });//}else {

        //}
    }


    private  void photoo (final String userid, final MyViewHolder holder){
        dbbb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    String profile_pic_linkds=ds.getKey();
                    assert profile_pic_linkds != null;
                    if(profile_pic_linkds.equals(userid)){
                        profile_pic=ds.child("profile_pic").getValue(String.class);
                        Log.d(TAG,profile_pic);
                        Glide.with(holder.circleImageViewl.getContext()).load(profile_pic).into(holder.circleImageViewl);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
