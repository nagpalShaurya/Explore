package com.example.explore;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class fetchlist extends AppCompatActivity {

    // FireBase stuff
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String UserID;
    private ListView mListView;

    String title;
    String status="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetchlist);
        mAuth = FirebaseAuth.getInstance();
        mListView = findViewById(R.id.listview);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("Published Forms");

        Intent i = getIntent();
        final String id1 = i.getStringExtra("id");

        final FirebaseUser user = mAuth.getCurrentUser();
        UserID = user.getUid();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final ArrayList<String> array = new ArrayList<>();
                for (DataSnapshot ds: dataSnapshot.getChildren())
            {
/*                    USER user1 = new USER();
                    user1.setName(ds.getValue(USER.class).getName());
                    user1.setID(ds.getValue(USER.class).getID());

                    array.add("Employee Name: "+ user1.getName()+"\n"+"Employee ID: "+user1.getID());*/

                final Poll_Model pm = new Poll_Model();
                pm.setTitle(ds.getValue(Poll_Model.class).getTitle());
                pm.setDescription(ds.getValue(Poll_Model.class).getDescription());
                pm.setQuery(ds.getValue(Poll_Model.class).getQuery());

                title = pm.getTitle();
                array.add(pm.getTitle());
            }
            ArrayAdapter adapter = new ArrayAdapter(fetchlist.this,android.R.layout.simple_list_item_1,array);
                mListView.setAdapter(adapter);
        }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mListView.setClickable(true);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                Object o = mListView.getItemAtPosition(position);
                String str = (String)o; //As you are using Default String Adapter
                Toast.makeText(getBaseContext(),str, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(fetchlist.this,createForm.class);
               intent.putExtra("name", str);
                intent.putExtra("id",id1);
                startActivity(intent);
            }
        });
    }
}

