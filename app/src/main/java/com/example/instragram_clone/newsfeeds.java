package com.example.instragram_clone;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class newsfeeds extends Fragment {

    private static final String TAG = "done";
    private String Date;


    private CardView cv;
    private ProgressBar pb;
    ArrayList<String> bm=new ArrayList<>();
    private MyAdapter adapter;
    private RecyclerView rv;
    private LinearLayoutManager lm;
    private DatabaseReference frnd;
    private ArrayList<String> following_listname=new ArrayList<>();
    private DatabaseReference user_details;
    private ArrayList<String> gFollowing_listid=new ArrayList<>();
    private ArrayList<String> image_link_of_following=new ArrayList<>();
    private ArrayList<String> username_of_following=new ArrayList<>();
    final FirebaseDatabase mref= FirebaseDatabase.getInstance("https://instragram-clone-7ff94.firebaseio.com/");
    DatabaseReference dr;
    FirebaseAuth mauth;
    String user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.newsfeeds,container,false);

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).show();
        user_details=mref.getReference("user_details");
        mauth = FirebaseAuth.getInstance();
        user = Objects.requireNonNull(mauth.getCurrentUser()).getUid();
        cv = Objects.requireNonNull(getView()).findViewById(R.id.card1);
        pb = getView().findViewById(R.id.progress1);
        dr = mref.getReference("userinfo");
        frnd=mref.getReference("userinfo" + "/" + user + "/" + "friendlist/following/");
        rv = getView().findViewById(R.id.recycler);
        SimpleDateFormat dateFormat =new SimpleDateFormat("ddMMyyyy");
        Calendar calendar = Calendar.getInstance();

        Date = dateFormat.format(calendar.getTime());
        Read_data();
    }

    private void Read_data() {

        pb.setVisibility(View.VISIBLE);
        cv.setVisibility(View.VISIBLE);



        frnd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    String following_person=ds.getValue(String.class);
                    following_listname.add(following_person);


                }

                if(following_listname.size()!=0){

               userdetails();}
                else
                {pb.setVisibility(View.INVISIBLE);
                cv.setVisibility(View.INVISIBLE);}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void userdetails() {
        user_details.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(String ds:following_listname){
                    String following_id =snapshot.child(ds).getValue(String.class);
                    gFollowing_listid.add(following_id);

                }
                DR();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void DR() {
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String value,VALUE,name;
                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    value = ds.child("userid").getValue(String.class);
                    name=ds.child("username").getValue(String.class);
                    VALUE=ds.child("profile_pic").getValue(String.class);

//                    Toast toast=Toast.makeText(getContext(),name,Toast.LENGTH_SHORT);
//                    toast.show();
                    for(String df:gFollowing_listid){
                        assert value != null;
                        if(value.equals(df)){


                            for(DataSnapshot pic_link:ds.child("pic_link").child("date_pic_links").child(Date).getChildren()){
                               String picc=pic_link.getValue(String.class);
                                assert picc != null;
                                if(!picc.equals("")) {
                                    image_link_of_following.add(picc);
                                    username_of_following.add(name);
                                    assert VALUE != null;
                                    if(!VALUE.equals("")){
                                        bm.add(VALUE);
                                    }

                                }
                            }
                        }
                    }
                }



                lm = new LinearLayoutManager(getContext());
                rv.setLayoutManager(lm);
                Toast.makeText(getContext(),"pp",Toast.LENGTH_SHORT).show();

                adapter=  new MyAdapter(username_of_following,image_link_of_following,bm,this);
                if(image_link_of_following.size()!=0){
                    rv.setAdapter( adapter);}
                pb.setVisibility(View.INVISIBLE);
                cv.setVisibility(View.INVISIBLE);

//



            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.w(TAG, "Failed to read value.", error.toException());
                pb.setVisibility(View.INVISIBLE);
                cv.setVisibility(View.INVISIBLE);


            }

        });
        pb.setVisibility(View.INVISIBLE);//
        cv.setVisibility(View.INVISIBLE);//
    }


}
