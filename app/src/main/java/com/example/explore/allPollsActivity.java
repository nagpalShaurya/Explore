package com.example.explore;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class allPollsActivity extends AppCompatActivity {

    private RecyclerView mBlockList;
    private DatabaseReference dbref;
    private String uid = "1711981278";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_polls);


/*_________________________________________________________________________________________________________________________________________________________________

                                                    DataBase Reference
/*_______________________________________________________________________________________________________________________________________________________________*/


                    dbref = FirebaseDatabase.getInstance().getReference().child("Published Forms");
                    dbref.keepSynced(true);

                    //find views
        mBlockList = findViewById(R.id.recyclerView_admin);
        mBlockList.setHasFixedSize(true);
        mBlockList.setLayoutManager(new LinearLayoutManager(this));
    }
/*_________________________________________________________________________________________________________________________________________________________________

                                                    On Start Method && Recycler view Code
/*_______________________________________________________________________________________________________________________________________________________________*/

                @Override
                protected void onStart() {
                    super.onStart();

                    FirebaseRecyclerOptions<Poll_Model> options = new FirebaseRecyclerOptions.Builder<Poll_Model>()
                            .setQuery(dbref,Poll_Model.class)
                            .build();

                    FirebaseRecyclerAdapter<Poll_Model, PollViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Poll_Model, PollViewHolder>
                            (options) {
                        @Override
                        protected void onBindViewHolder(@NonNull PollViewHolder holder, int position, @NonNull Poll_Model model) {

                            holder.title.setText(model.getTitle());
                            holder.description.setText(model.getDescription());

                        }

                        @NonNull
                        @Override
                        public PollViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_card_layout,viewGroup,false);
                            PollViewHolder viewHolder = new PollViewHolder(view);
                            return viewHolder;
                        }
                    };
                    mBlockList.setAdapter(firebaseRecyclerAdapter);
                    firebaseRecyclerAdapter.startListening();
                }

    public static class PollViewHolder extends RecyclerView.ViewHolder {

                    TextView title,description,status;

                    public PollViewHolder(@NonNull View itemView)
                    {
                        super(itemView);
                        status = itemView.findViewById(R.id.formstatus);
                        title = itemView.findViewById(R.id.formTitle);
                        description = itemView.findViewById(R.id.formDesc);
                    }

    }
}
/*_________________________________________________________________________________________________________________________________________________________________

                                                    End Of Activity
/*_______________________________________________________________________________________________________________________________________________________________*/
