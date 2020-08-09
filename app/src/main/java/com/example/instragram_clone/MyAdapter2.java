package com.example.instragram_clone;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.MyViewHolder> implements Filterable {
    ArrayList<String> list;
    ArrayList<String> List;
    ArrayList<String> list1;
    ValueEventListener context;
    String userp;
    public MyAdapter2(ArrayList<String> list, ArrayList<String> list1, ValueEventListener context) {
        this.list=list;
        this.context=context;
        this.list1=list1;
        List=new ArrayList<>(list);

        //this.bm=bm;
    }

    @NonNull
    @Override
    public MyAdapter2.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.recycle2,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter2.MyViewHolder holder, int position) {
        final String title= list.get(position);
        holder.user_name.setText(title);

        String link=list1.get(position);
        //= FirebaseAuth.getInstance().getCurrentUser().getUid();
        Glide.with(holder.profile_pic.getContext()).load(link).into(holder.profile_pic);
        final FirebaseDatabase mref= FirebaseDatabase.getInstance("https://instragram-clone-7ff94.firebaseio.com/");


        holder.user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final DatabaseReference db=mref.getReference("user_details"+"/"+ title);
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       // String name=title;
                        userp = snapshot.getValue(String.class);
                        AppCompatActivity ft=(AppCompatActivity)v.getContext();
                        ft.getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new myInfo(userp)).commit();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

               // ft.addToBackStack(null);
              //  Toast.makeText((Context) context,"onclick",Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public Filter getFilter() {
        return filter;

    }

    private Filter filter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<String> filteredlist=new ArrayList<>();
            if(constraint==null || constraint.length()==0){
                filteredlist.addAll(List);
            }else {
                String filterpattern = constraint.toString().toLowerCase().trim();

                for (String ss : List) {
                    if (ss.toLowerCase().contains(filterpattern)){
                        filteredlist.add(ss);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredlist;
            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            list.clear();
            list.addAll((java.util.List)results.values);
            notifyDataSetChanged();
        }
    };

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView profile_pic;
        TextView user_name;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_pic=itemView.findViewById(R.id.textView231);
            user_name=itemView.findViewById(R.id.textView332);


        }
    }
}
