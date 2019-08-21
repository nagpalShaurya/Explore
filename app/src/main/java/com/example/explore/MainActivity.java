package com.example.explore;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    //Field Declarations
    EditText etemail, etpassword, etid;
    Button btnregister, btnlogin;
    String adminmail = "admin@gmail.com";
    String status = "not submitted";
    String emailid;

    int check;
    int proceedid = 0;

    int alluserid = 0;
    private String proceed = "";

    //FireBase-related
    private FirebaseAuth firebaseAuth;

    //SQLite related
    DatabaseHelper databaseHelper;

    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String id = user.getEmail();

            if (id.equals(adminmail)) {
                startActivity(new Intent(MainActivity.this, chooseActivity.class));
            }
            /*else
            {
                startActivity(new Intent(MainActivity.this,fetchlist.class));
            }*/
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Finding Views
        etemail = findViewById(R.id.et_email);
        etpassword = findViewById(R.id.et_pass);
        btnlogin = findViewById(R.id.btnLogin);
        etid = findViewById(R.id.et_id);
        final TextView report = findViewById(R.id.tvreport);

        final String[] authenticated_users = new String[1000];


        //FireBase related
        firebaseAuth = FirebaseAuth.getInstance();
        final DatabaseReference ref_auth = FirebaseDatabase.getInstance().getReference().child("Employees");


/*__________________________________________________________________________________________________

                            Get List of Authenticated User Ids
__________________________________________________________________________________________________*/

        ref_auth.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
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


        //Listeners
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etemail.getText().toString();
                String password = etpassword.getText().toString();
                final String id = etid.getText().toString();
/*__________________________________________________________________________________________________

                                check User Id is not fake
__________________________________________________________________________________________________*/

                for (String au : authenticated_users) {
                    if (id.equals(au)) {
                        proceedid = 1;
                        break;
                    }
                }



                if (proceedid == 1) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Employees").child(id);

                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            USER us = new USER();
                            us.setEmail(dataSnapshot.getValue(USER.class).getEmail());
                            emailid = us.getEmail();


                            if (email.equals(emailid)) {
                                proceed += "true";

                            }
                            else
                            {
                                proceed += "false";
                            }
                        }

/*__________________________________________________________________________________________________

                                provide auth="pass | fail"
__________________________________________________________________________________________________*/

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    if (proceed.equals("true")) {
                                proceed="";
                            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Employee Logged In", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, fetchlist.class);
                                        intent.putExtra("id", id);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(MainActivity.this, "Details Mis-Match", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else if(proceed.equals("false")){
                            proceed="";
                            Toast.makeText(MainActivity.this, "Please login with your account", Toast.LENGTH_SHORT).show();
                        }
                    }


            else
                {


                    if (email.equals("admin@gmail.com") && password.equals("admin123")) {
                        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, " Admin Logged In", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, chooseActivity.class);
                                    startActivity(intent);

                                }
                            }
                        });
                    }
                    else
                    Toast.makeText(MainActivity.this, "Invalid ID", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
