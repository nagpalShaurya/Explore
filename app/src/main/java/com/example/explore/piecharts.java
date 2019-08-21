package com.example.explore;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class piecharts extends AppCompatActivity {


    long yes=0;
    long no=0;
    long maybe = 0;

    float yess;
    float noo;
    float maybee;
    PieChartView pieChartView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piecharts);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Published Forms");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                yess  = (Float) dataSnapshot.child("Poll2").child("Yes").getValue();
                noo  = (Float) dataSnapshot.child("Poll2").child("No").getValue();
                maybee  = (Float) dataSnapshot.child("Poll2").child("Maybe").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        pieChartView = findViewById(R.id.chart);

        float YES = yes;
        float NO = no;
        float MAYBE = maybe;

        List pieData = new ArrayList<>();
        pieData.add(new SliceValue(yess, Color.BLUE).setLabel("Yes Votes"));
        pieData.add(new SliceValue(noo, Color.RED).setLabel("No Votes"));
        pieData.add(new SliceValue(maybee, Color.MAGENTA).setLabel("Maybe Votes"));

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(22);
        pieChartData.setHasCenterCircle(true).setCenterText1("Final Results").setCenterText1FontSize(26).setCenterText1Color(Color.parseColor("#0097A7"));
        pieChartView.setPieChartData(pieChartData);
    }
}