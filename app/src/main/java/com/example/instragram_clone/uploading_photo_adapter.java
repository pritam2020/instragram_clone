package com.example.instragram_clone;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.util.ArrayList;

public class uploading_photo_adapter extends RecyclerView.Adapter<uploading_photo_adapter.MyViewHolder> {
    ArrayList<Uri> list;
    public uploading_photo_adapter(ArrayList<Uri> list){
        this.list=list;
    }
    @NonNull
    @Override
    public uploading_photo_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.upload_photo_recycle,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull uploading_photo_adapter.MyViewHolder holder, int position) {
        Uri link1= list.get(position);
        Glide.with(holder.image1.getContext()).load(link1).into(holder.image1);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder{
        ImageView image1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image1=itemView.findViewById(R.id.uploading_image);
        }
    }
}
