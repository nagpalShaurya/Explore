package com.example.explore;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewUsers extends AppCompatActivity {


    // FireBase stuff
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String UserID;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);


        mAuth = FirebaseAuth.getInstance();
        mListView = findViewById(R.id.listview);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("Employees");

        final FirebaseUser user = mAuth.getCurrentUser();
        UserID = user.getUid();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<String> array = new ArrayList<>();
                for (DataSnapshot ds: dataSnapshot.getChildren())
                {
                    USER user1 = new USER();
                    user1.setName(ds.getValue(USER.class).getName());
                    user1.setID(ds.getValue(USER.class).getID());

                    array.add("Employee Name: "+ user1.getName()+"\n"+"Employee ID: "+user1.getID());
                }
                ArrayAdapter adapter = new ArrayAdapter(ViewUsers.this,android.R.layout.simple_list_item_1,array);
                mListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
