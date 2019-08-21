package com.example.explore;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class testFormStatus extends AppCompatActivity {

    ListView listview;
    ArrayList<String> array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_form_status);

        array  = new ArrayList<>();
        listview = findViewById(R.id.listview_polls);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Employees");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            //    for(DataSnapshot ds:dataSnapshot.getChildren())
                {
/*                    Poll_Model pm = new Poll_Model();
                    pm.setTitle(ds.getValue(Poll_Model.class).getTitle());
                    pm.setDescription(ds.getValue(Poll_Model.class).getDescription());
                    pm.setQuery(ds.getValue(Poll_Model.class).getQuery());

                    array.add("Title: "+pm.getTitle()+"\n"+"Issue: "+pm.getQuery()+"\n"+"Description: "+pm.getDescription()+"\n");*/

                    ArrayList<String> array = new ArrayList<>();
                    for (DataSnapshot ds: dataSnapshot.getChildren())
                    {
                        USER user1 = new USER();
                        user1.setName(ds.getValue(USER.class).getName());
                        user1.setID(ds.getValue(USER.class).getID());

                        array.add("Employee Name: "+ user1.getName()+"\n"+"Employee ID: "+user1.getID());
                    }
                    ArrayAdapter adapter = new ArrayAdapter(testFormStatus.this,android.R.layout.simple_list_item_1,array);

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(testFormStatus.this,android.R.layout.simple_list_item_1);
                listview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(testFormStatus.this, "Error Fetching data", Toast.LENGTH_SHORT).show();
            }
        });
        listview.setClickable(true);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = listview.getItemAtPosition(position);
                String str = (String)o; //As you are using Default String Adapter
                Toast.makeText(getBaseContext(),str,Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(testFormStatus.this,submitResponse.class);
                intent.putExtra("name", str);
                startActivity(intent);
            }
        });
    }
        }




/*_________________________________________________________________________________________________________________________________________________________________

                                                    End Of Activity
/*_______________________________________________________________________________________________________________________________________________________________*/

