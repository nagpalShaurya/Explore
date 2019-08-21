package com.example.explore;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class test extends AppCompatActivity {


    EditText etid;
    Button btnsubmit;
    TextView tvstatus;

    String status="not submitted";
    int alluserid=0;
    private String proceed="false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        etid = findViewById(R.id.et_enterid);
        btnsubmit = findViewById(R.id.btn_proceed);
        tvstatus = findViewById(R.id.tv_status);


        final String[] authenticated_users = new String[1000];
        final DatabaseReference ref_auth = FirebaseDatabase.getInstance().getReference().child("Employees");

/*__________________________________________________________________________________________________

                            Get List of Authenticated User Ids
__________________________________________________________________________________________________*/

        ref_auth.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    USER user = new USER();
                    user.setID(ds.getKey());

                    authenticated_users[alluserid++] = user.getID();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

/*__________________________________________________________________________________________________

                                Button to Proceed
__________________________________________________________________________________________________*/

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid = etid.getText().toString();

                for (String checkid : authenticated_users) {
                    if (uid.equals(checkid)) {
                        proceed = "true";
                        break;
                    }
                }
                if (proceed.equals("true")) {

                    DatabaseReference ref_emps = FirebaseDatabase.getInstance().getReference().child("Employees").child(uid);

                    ref_emps.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            status = (String) dataSnapshot.child("status").getValue();
                            tvstatus.setText(status);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                        Intent intent = new Intent(test.this, submitResponse.class);
                        intent.putExtra("user_id", uid);
                        startActivity(intent);

                /*    else {
                        etid.setError("Response has been recorded");

                    }*/
                }
                else
                {
                    etid.setError("Cannot find ID");
                }
            }
        });
    }
}
