package com.example.explore;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Objects;

public class submitResponse extends AppCompatActivity {



    //General
    TextView tvtitle,tvquery,tvdesc,tvxyz;
    Button btnsubmit,btnlogout;
    EditText etid,etreview;
    RadioButton rbyes,rbno,rbmaybe;
    RadioButton responsebutton;
    RadioGroup rg;


    int  countyes=0;
    int countno=0;
    int  countmaybe=0;
    private String response="";
    private String formTitle;
    private String respStatus="";
    private String uid;

/*________________________________________________________________________________________________*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_response);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;

/*__________________________________________________________________________________________________

                                      General
__________________________________________________________________________________________________*/




        //findViews
        tvtitle = findViewById(R.id.tv_title);
        tvdesc = findViewById(R.id.tv_description);
        tvquery = findViewById(R.id.tv_query);
        btnsubmit = findViewById(R.id.btn_submit);
        rbyes = findViewById(R.id.radio_yes);
        rbno = findViewById(R.id.radio_no);
        rbmaybe = findViewById(R.id.radio_maybe);
        rg = findViewById(R.id.radiogroup);
        etreview = findViewById(R.id.et_feedback);
        btnlogout = findViewById(R.id.btn_logoutfromsubmit);
        tvxyz = findViewById(R.id.tv_btnreplace);


/*__________________________________________________________________________________________________

                         Receive Intent from previous activity
__________________________________________________________________________________________________*/

      Intent intent = getIntent();
      uid = intent.getStringExtra("id");

/*__________________________________________________________________________________________________

                                Database References
__________________________________________________________________________________________________*/





/*__________________________________________________________________________________________________

                            Check the RESPONSE Status :-
__________________________________________________________________________________________________*/


 DatabaseReference ref_emps = FirebaseDatabase.getInstance().getReference().child("current form").child(uid);

ref_emps.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        if(dataSnapshot.exists()) {
            respStatus = ((dataSnapshot.child("status").getValue())).toString();
            hideField(respStatus);
        }
        else
        {
            hideField(respStatus);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});


/*__________________________________________________________________________________________________

                                   Set Up The Form
__________________________________________________________________________________________________*/

    DatabaseReference ref_setUpForm = FirebaseDatabase.getInstance().getReference().child("current form");

    //retrieve from FireBase
    ref_setUpForm.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            Poll_Model model = new Poll_Model();

            //title
            model.setTitle(dataSnapshot.getValue(Poll_Model.class).getTitle());
            tvtitle.setText(model.getTitle());
            formTitle = model.getTitle();

            //description
            model.setDescription(dataSnapshot.getValue(Poll_Model.class).getDescription());
            tvdesc.setText(model.getDescription());

            //query
            model.setQuery(dataSnapshot.getValue(Poll_Model.class).getQuery());
            tvquery.setText(model.getQuery());

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });


/*__________________________________________________________________________________________________

                                   Buttons and listeners
__________________________________________________________________________________________________*/


    //logout button
    btnlogout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(submitResponse.this, MainActivity.class));
        }
    });


    //radio button response
    rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            responsebutton = findViewById(checkedId);

            switch (checkedId) {
                case R.id.radio_yes:
                    response = "Yes";
                    break;
                case R.id.radio_no:
                    response = "No";
                    break;
                case R.id.radio_maybe:
                    response = "Maybe";
                    break;
            }
        }
    });


    //button to submit form and upload to database
    btnsubmit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            DatabaseReference dbrefresp = FirebaseDatabase.getInstance().getReference().child("User Responses").child(formTitle).child(uid);
            DatabaseReference liveformresp = FirebaseDatabase.getInstance().getReference("current form").child(uid);
            DatabaseReference ref_emps = FirebaseDatabase.getInstance().getReference("Employees").child(uid);

            /* -------------  CHECK IF USER HAS ALREADY SUBMITTED ONCE   -------------*/

            if (!respStatus.equals("Submitted")) {
                //retrieve reviews
                final String reviews = etreview.getText().toString();

                switch (response) {
                    case "Yes":
                        dbrefresp.child("Feedback").setValue(reviews);
                        dbrefresp.child("Response").setValue(response);
                        dbrefresp.child("ID").setValue(uid);

                        liveformresp.child("status").setValue("submitted");

                        countyes++;
                        ref_emps.child("status").setValue("Submitted");

                        break;

                    case "No":
                        dbrefresp.child("Feedback").setValue(reviews);
                        dbrefresp.child("Response").setValue(response);
                        dbrefresp.child("ID").setValue(uid);

                        liveformresp.child("status").setValue("submitted");

                        countno++;

                        ref_emps.child("status").setValue("Submitted");
                        break;

                    case "Maybe":

                        dbrefresp.child("Feedback").setValue(reviews);
                        dbrefresp.child("Response").setValue(response);
                        dbrefresp.child("ID").setValue(uid);

                        liveformresp.child("status").setValue("submitted");

                        countmaybe++;

                        ref_emps.child("status").setValue("Submitted");
                        break;

                }
                Toast.makeText(submitResponse.this,
                        "Response has been added",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(submitResponse.this, "You can submit only once", Toast.LENGTH_SHORT).show();
            }
        }
    });

    }

    private void hideField(String respStatus) {
        if(respStatus.equals("submitted"))
        {
            btnsubmit.setVisibility(View.GONE);
        }
        else
        {
            tvxyz.setVisibility(View.GONE);
        }
    }
}
/*__________________________________________________________________________________________________

                                      END OF ACTIVITY
__________________________________________________________________________________________________*/
