package com.example.instragram_clone;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
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

public class searchActivity extends Fragment {
    ArrayList<String> profile_pic_list=new ArrayList<>();
    ArrayList<String> username_list=new ArrayList<>();

    final FirebaseDatabase mref= FirebaseDatabase.getInstance("https://instragram-clone-7ff94.firebaseio.com/");
    FirebaseAuth mauth;
    String user;
    DatabaseReference username_reference;
    private MyAdapter2 adapter;
    private RecyclerView rv;
    private LinearLayoutManager lm;
    private CardView cv;
    private ProgressBar pb;
    EditText searchView;
    DatabaseReference username_reference1;
    String user_name;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search,container,false);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mauth=FirebaseAuth.getInstance();
        user= Objects.requireNonNull(mauth.getCurrentUser()).getUid();
        rv = Objects.requireNonNull(getView()).findViewById(R.id.recycler1);
        cv = getView().findViewById(R.id.card2);
        pb = getView().findViewById(R.id.progress2);
        username_reference1=mref.getReference("user_details");
        readData_1();

        username_reference=mref.getReference("userinfo");
        readData();


        searchView = getView().findViewById(R.id.searchview);
        //profile_pic_reference=mref.getReference("user_details"+"/"+user+"/"

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void readData_1() {
        username_reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    String name=ds.getValue(String.class);
                    if(user.equals(name)){
                        user_name=ds.getKey();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readData() {
        pb.setVisibility(View.VISIBLE);
        cv.setVisibility(View.VISIBLE);
        username_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    String USERNAME=ds.child("username").getValue(String.class);
                    String PROFILE_PIC=ds.child("profile_pic").getValue(String.class);
//                    Toast toast=Toast.makeText(getContext(),USERNAME,Toast.LENGTH_SHORT);
//                    toast.show();
                    assert USERNAME != null;
                    if(!USERNAME.equals(user_name)){
                        username_list.add(USERNAME);
                    profile_pic_list.add(PROFILE_PIC);}

                }
                lm = new LinearLayoutManager(getContext());
                rv.setLayoutManager(lm);

                adapter=  new MyAdapter2(username_list,profile_pic_list,this);
               // if(profile_pic_list.size()!=0){
                rv.setAdapter( adapter);//}
                pb.setVisibility(View.INVISIBLE);
                cv.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pb.setVisibility(View.INVISIBLE);
                cv.setVisibility(View.INVISIBLE);

            }
        });

    }
}
