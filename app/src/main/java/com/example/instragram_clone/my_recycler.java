package com.example.instragram_clone;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class my_recycler extends RecyclerView.Adapter<my_recycler.MyViewHolder> {


    private ArrayList<String> all_photo_link_list;
    private ValueEventListener context;
    String passed_user;
    int value;
    FirebaseAuth mauth;
    String user;


    public my_recycler(ArrayList<String> all_photo_link_list,int value, String passed_user,ValueEventListener context){
       this.all_photo_link_list=all_photo_link_list;
       this.context=context;
       this.value=value;
       this.passed_user=passed_user;
    }
    @NonNull
    @Override
    public my_recycler.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.myphotorecycle,parent,false);
        return new MyViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(@NonNull final my_recycler.MyViewHolder holder, int position) {
        mauth=FirebaseAuth.getInstance();
        user= Objects.requireNonNull(mauth.getCurrentUser()).getUid();
        String link1= all_photo_link_list.get(position);
        Glide.with(holder.image1.getContext()).load(link1).apply(new RequestOptions().override(400,400)).into(holder.image1);
//        if(passed_user.equals(user)) {
//            holder.delete_layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    PopupMenu popupMenu = new PopupMenu(v.getContext(), holder.delete_layout);
//                    popupMenu.getMenuInflater().inflate(R.menu.delete, popupMenu.getMenu());
//                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                        @Override
//                        public boolean onMenuItemClick(MenuItem item) {
//                            db=mref.getReference("userinfo/"+user);
//                            return true;
//                        }
//                    });
//                    popupMenu.show();
//                }
//            });
//        }
//        all_photo_link_list.remove(position);
//         int size=all_photo_link_list.size();
//         if(size>1){
//           String link2=all_photo_link_list.get(position+1);
//           Picasso.get().load(link2).into(holder.image2);
//           all_photo_link_list.remove(position+1);
//       }
//       if(size>2){
//           String link3=all_photo_link_list.get(position+2);
//           Picasso.get().load(link3).into(holder.image3);
//           all_photo_link_list.remove(position+2);
//       }
    }

    @Override
    public int getItemCount() {
        return all_photo_link_list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView image1;
        LinearLayout delete_layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
             image1 = itemView.findViewById(R.id.image1);

            delete_layout=itemView.findViewById(R.id.delete_layout);

        }
    }
}
