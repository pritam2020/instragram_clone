package com.example.instragram_clone;


import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolderm>{


    ArrayList<String> list;
    final String TAG = "pagol";
    ArrayList<String> List;
    ArrayList<String> list1;
    ArrayList<String> bm;
    ValueEventListener context;
    FirebaseAuth mAuth;
    DatabaseReference DR;
    String user;
    String pass="null";
    int c=0;

   final FirebaseDatabase mref= FirebaseDatabase.getInstance("https://instragram-clone-7ff94.firebaseio.com/");
    public MyAdapter(ArrayList<String> list, ArrayList<String> list1, ArrayList<String> bm,ValueEventListener context) {
        this.list=list;
        this.context=context;
        this.list1=list1;
        this.bm=bm;
        List=new ArrayList<>(list);



    }




    @NonNull
    @Override
    public MyViewHolderm onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.recycle,parent,false);
        return new MyViewHolderm(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolderm holder, final int position) {
        mAuth = FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser().getUid();
     final String title= list.get(position);
     holder.tn.setText(title);
     final String link=list1.get(position);
     String DP=bm.get(position);

        Glide.with(holder.tm.getContext()).load(link).apply(new RequestOptions().override(411,355)).into(holder.tm);
        Glide.with(holder.dp.getContext()).load(DP).into(holder.dp);
        final DatabaseReference username=mref.getReference("username/"+ user +"/"+title).push();
        final DatabaseReference USERNAME=mref.getReference("username/"+ user +"/"+title);
        read(USERNAME,link,holder);

    holder.react.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (holder.react.isChecked()) {
                pass = "null";
                Log.d(TAG, "onCheckedChanged: ");
                username.child("link").setValue(link);
                username.child("reactions").setValue("r");
            }else {
                Log.d(TAG,"remave");
                pass="remove";
                read(USERNAME,link ,holder);
            }
        }
    });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class MyViewHolderm extends RecyclerView.ViewHolder {
        ImageView tm,comment;
        CheckBox react;
        TextView tn;
        CircleImageView dp;

        public MyViewHolderm(@NonNull View itemView) {
            super(itemView);
            tm = itemView.findViewById(R.id.textView23);
            tn = itemView.findViewById(R.id.textView33);
            dp=itemView.findViewById(R.id.dp);
            react=itemView.findViewById(R.id.button11);
            comment=itemView.findViewById(R.id.button12);



        }
    }


    private void read(DatabaseReference USERNAME, final String link, final MyViewHolderm holder) {

        USERNAME.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG,"onDataChange");
                Log.d(TAG,pass+"pass");
                for(DataSnapshot ds:snapshot.getChildren()){
                    Log.d(TAG,"insideloop");
                    String LINK=ds.child("link").getValue(String.class);
                    if(link.equals(LINK)){
                        Log.d(TAG,"first if");

                        DR = ds.getRef();
                        if(Objects.equals(pass, "remove")){
                            Log.d(TAG, pass);
                            if(!ds.child("comment").exists()) {
                                DR.removeValue();
                                Log.d(TAG,"1");
                                //pass = "";
                            }else {
                                DR.child("reaction").removeValue();
                                Log.d(TAG,"2");
                                //pass="";
                            }
                            holder.react.setChecked(false);
                            pass="null";

                        }else if(Objects.equals(ds.child("reactions").getValue(String.class), "r")){

                            holder.react.setChecked(true);
                        }

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}
