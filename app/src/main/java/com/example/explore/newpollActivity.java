package com.example.explore;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;

public class newpollActivity extends AppCompatActivity {

    //General
    EditText ettitle,etquery,etdesc;
    Button btnpublish;
    Poll_Model model;

    long maxid = 0;

    //FireBase Fields
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpoll);

       //notification channel
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("MyNotifications","MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

 /*--------------------- add sample form at the starting of the app ------------------------------*

        DatabaseReference formref = FirebaseDatabase.getInstance().getReference().child("Published Forms").child("current form");
        //also upload sample form at this time
        String title = "Sample Title";
        String query = "Sample Query";
        String desc = "Sample description";
        model.setTitle(title);
        model.setQuery(query);
        model.setDescrition(desc);

        formref.setValue(model);
/*-----------------------------------------------------------------------------------------------*/

        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "hello";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                       // Toast.makeText(newpollActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        mAuth = FirebaseAuth.getInstance();


        //find views
        ettitle = findViewById(R.id.et_title);
        etquery = findViewById(R.id.et_query);
        etdesc = findViewById(R.id.et_description);
        btnpublish = findViewById(R.id.btnpublish);


        final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Published Forms");
        final DatabaseReference currentref = FirebaseDatabase.getInstance().getReference().child("current form");
        final DatabaseReference us = FirebaseDatabase.getInstance().getReference().child("user responses");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    maxid = dataSnapshot.getChildrenCount();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Button listener
        btnpublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = ettitle.getText().toString();
                String query = etquery.getText().toString();
                String desc = etdesc.getText().toString();

                model = new Poll_Model();

                model.setTitle(title);
                model.setQuery(query);
                model.setDescription(desc);

                //Upload to firebase
                dbref.child(title).setValue(model);
                currentref.setValue(model);


                dbref.child(title).child("Yes").setValue(0);
                dbref.child(title).child("No").setValue(0);
                dbref.child(title).child("Maybe").setValue(0);
            }
        });

    }
}
