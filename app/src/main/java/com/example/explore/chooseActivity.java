package com.example.explore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class chooseActivity extends AppCompatActivity {


    Button btnadd, btnview,btnlogout;

    //Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuitems,menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.menu_createpoll:
                startActivity(new Intent(chooseActivity.this,newpollActivity.class));
                break;

            case R.id.menu_allpolls:
                startActivity(new Intent(chooseActivity.this,allPollsActivity.class));
                break;
            case R.id.menu_results:
                startActivity(new Intent(chooseActivity.this,results.class));
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        btnadd = findViewById(R.id.btn_addemp);
        btnview = findViewById(R.id.btn_viewemp);
        btnlogout = findViewById(R.id.btn_logout);


        //Listeners

        //Add Employee
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            startActivity(new Intent(chooseActivity.this,registerActivity.class));

            }
        });

        //View Employees
        btnview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(chooseActivity.this,ViewUsers.class));

            }
        });

        //Logout
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(chooseActivity.this,MainActivity.class));
                Toast.makeText(chooseActivity.this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
