package navneet.com.quizrest;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference ref;
    private ArrayList<User> userArrayList=new ArrayList<>();
    private DataAdapter dataAdapter;
    private FirebaseAuth mAuth;
    private String userID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        ref = FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();

        FirebaseUser firebaseUseruser=mAuth.getCurrentUser();

        if (firebaseUseruser!=null) {
            userID=firebaseUseruser.getUid();
        }


        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        if (firebaseUseruser!=null)
        populateItems(firebaseUseruser);

    }

    private void populateItems(final FirebaseUser firebaseUser) {
        ref.child("users").orderByChild("descOrder").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user=new User();
                user=dataSnapshot.getValue(User.class);
                if (Integer.parseInt(user.getHighScore())>10) {
                    userArrayList.add(user);
                    dataAdapter = new DataAdapter(userArrayList, LeaderboardActivity.this,firebaseUser.getEmail());
                    recyclerView.setAdapter(dataAdapter);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
