package com.example.explore;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class registerActivity extends AppCompatActivity {

    //Various Fields
    Button btnadd;
    EditText etname,etid,etpass,etemail;

    //Firebase Fields
    FirebaseAuth mAuth;
    FirebaseUser user;

    //SQLite Database
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseHelper = new DatabaseHelper(this);

        //Finding Views
        etname = findViewById(R.id.et_regname);
        etid = findViewById(R.id.et_regid);
        etemail = findViewById(R.id.et_regemail);
        etpass = findViewById(R.id.et_regpass);


        //Listeners
        btnadd = findViewById(R.id.btn_regemp);

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth = FirebaseAuth.getInstance();
                user = mAuth.getCurrentUser();
                String currentUID = etid.getText().toString();

                //Register Employee
                String email = etemail.getText().toString();
                String password = etpass.getText().toString();
                String id = etid.getText().toString();
                String Name = etname.getText().toString();

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(registerActivity.this, "Employee Added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                //Add to DataBase
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                DatabaseReference myRef = database.getReference("Employees");

                myRef.child(currentUID).child("Name").setValue(Name);
                myRef.child(currentUID).child("Email").setValue(email);
                myRef.child(currentUID).child("ID").setValue(id);
                myRef.child(currentUID).child("Password").setValue(password);
                myRef.child(currentUID).child("status").setValue("not submitted");

                //Add to SQLite
                if (Validate()) {
                    //SQLite
                    String uid = etid.getText().toString();
                    String uname = etname.getText().toString();
                    String upass = etemail.getText().toString();

                    USER u = new USER();
                    u.setID(uid);
                    u.setName(uname);
                    u.setPassword(upass);
                    databaseHelper.addUser(u);
                    Toast.makeText(registerActivity.this, "Employee Added Successfully", Toast.LENGTH_LONG).show();

                    etname.setText("");
                    etid.setText("");
                    etemail.setText("");
                    etpass.setText("");

                }

            }
        });

    }
    //SQLite Validate Function
    public boolean Validate()
    {
        String uid = etid.getText().toString();
        String uname = etname.getText().toString();
        String upass = etpass.getText().toString();

        if(uid.isEmpty() || uname.isEmpty() || upass.isEmpty())
        {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_LONG).show();
            return false;
        }
        else
        {
            return true;
        }
    }

}

