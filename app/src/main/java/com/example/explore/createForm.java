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

public class createForm extends AppCompatActivity {

    String formTit;

    //General
    TextView tvtitle, tvquery, tvdesc, tvxyz,resp,s;
    Button btnsubmit, btnlogout;
    EditText etid, etreview;
    RadioButton rbyes, rbno, rbmaybe;
    RadioButton responsebutton;
    RadioGroup rg;

    String status = "";
    String result = "";
    String response = "";

    long countYes = 0;
    long countNo = 0;
    long countMaybe = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_form);

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
        resp  = findViewById(R.id.response);
        s = findViewById(R.id.tvStatus);
/*__________________________________________________________________________________________________

                         Receive Intent from previous activity
__________________________________________________________________________________________________*/

        Bundle bundle = getIntent().getExtras();
        final String valueReceived1 = bundle.getString("name");
        final String valueReceived2 = bundle.getString("id");


        Toast.makeText(this, valueReceived2, Toast.LENGTH_LONG).show();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Published Forms").child(valueReceived1);
        DatabaseReference checkResp = FirebaseDatabase.getInstance().getReference().child("user responses").child(valueReceived1).child(valueReceived2);

        checkResp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {
                    Poll_Model pm = new Poll_Model();
                    pm.setStatus(dataSnapshot.getValue(Poll_Model.class).getStatus());
                    pm.setResult(dataSnapshot.getValue(Poll_Model.class).getResult());


                    String st1 = pm.getStatus();
                    status += st1;

                    String rs1 = pm.getResult();
                    result += rs1;



                    if (status.equals("submitted")) {
                        resp.setText(result);
                        btnsubmit.setVisibility(View.GONE);
                        rg.setVisibility(View.GONE);
                    } else {
                        s.setVisibility(View.GONE);
                        resp.setVisibility(View.GONE);
                        tvxyz.setVisibility(View.GONE);
                    }

                } else {
                    s.setVisibility(View.GONE);
                    resp.setVisibility(View.GONE);
                    tvxyz.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Poll_Model pm = new Poll_Model();
                pm.setTitle(dataSnapshot.getValue(Poll_Model.class).getTitle());
                String tit = pm.getTitle();
                tvtitle.setText(tit);

                pm.setQuery(dataSnapshot.getValue(Poll_Model.class).getQuery());
                String quer = pm.getQuery();
                tvquery.setText(quer);

                pm.setDescription(dataSnapshot.getValue(Poll_Model.class).getDescription());
                tvdesc.setText(pm.getDescription());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //logout button
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(createForm.this, MainActivity.class));
            }
        });

   // get count values

        final DatabaseReference dbrefVotes = FirebaseDatabase.getInstance().getReference().child("Published Forms").child(valueReceived1);

        dbrefVotes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    countYes =(Long) dataSnapshot.child("Yes").getValue();
                    countNo = (Long) dataSnapshot.child("No").getValue();
                    countMaybe = (Long) dataSnapshot.child("Maybe").getValue();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //radio button response
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                responsebutton = findViewById(checkedId);

                switch (checkedId) {
                    case R.id.radio_yes:
                        response += "Yes";
                        countYes++;
                        break;
                    case R.id.radio_no:
                        response += "No";
                        countNo++;
                        break;
                    case R.id.radio_maybe:
                        response += "Maybe";
                        countMaybe++;
                        break;
                }
            }
        });
        //button to submit form and upload to database
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatabaseReference dbrefresp = FirebaseDatabase.getInstance().getReference().child("user responses").child(valueReceived1).child(valueReceived2);


                //retrieve reviews
                final String reviews = etreview.getText().toString();

                switch (response) {
                    case "Yes":
                        dbrefresp.child("feedback").setValue(reviews);
                        dbrefresp.child("result").setValue(response);
                        dbrefresp.child("status").setValue("submitted");

                        dbrefVotes.child("Yes").setValue(countYes);
                        dbrefVotes.child("No").setValue(countNo);
                        dbrefVotes.child("Maybe").setValue(countMaybe);

                        break;

                    case "No":

                        dbrefresp.child("feedback").setValue(reviews);
                        dbrefresp.child("result").setValue(response);
                        dbrefresp.child("status").setValue("submitted");

                        dbrefVotes.child("Yes").setValue(countYes);
                        dbrefVotes.child("No").setValue(countNo);
                        dbrefVotes.child("Maybe").setValue(countMaybe);
                        break;

                    case "Maybe":

                        dbrefresp.child("feedback").setValue(reviews);
                        dbrefresp.child("result").setValue(response);
                        dbrefresp.child("status").setValue("submitted");

                        dbrefVotes.child("Yes").setValue(countYes);
                        dbrefVotes.child("No").setValue(countNo);
                        dbrefVotes.child("Maybe").setValue(countMaybe);
                        break;

                }
                Toast.makeText(createForm.this,
                        "Response has been recorded",
                        Toast.LENGTH_SHORT).show();
                resp.setText(result);
                btnsubmit.setVisibility(View.GONE);
                rg.setVisibility(View.GONE);

            }
        });
    }
}