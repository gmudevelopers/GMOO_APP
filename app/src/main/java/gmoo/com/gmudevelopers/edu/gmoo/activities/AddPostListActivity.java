package gmoo.com.gmudevelopers.edu.gmoo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import gmoo.com.gmudevelopers.edu.gmoo.R;
import gmoo.com.gmudevelopers.edu.gmoo.adapters.PostAddRecyclerAdaptor;
import gmoo.com.gmudevelopers.edu.gmoo.model.PostAdd;

public class AddPostListActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;
    private PostAddRecyclerAdaptor postAddRecyclerAdaptor;
    private List<PostAdd> postAddList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post_list);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("Post Adds");
        mDatabaseReference.keepSynced(true);

        postAddList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    protected void onStart() {
        super.onStart();

        // Set up everything here
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                // Retrieve from database and put it in the list
                PostAdd postAdd = dataSnapshot.getValue(PostAdd.class);
                postAddList.add(postAdd);

                postAddRecyclerAdaptor = new PostAddRecyclerAdaptor(AddPostListActivity.this, postAddList);
                recyclerView.setAdapter(postAddRecyclerAdaptor);
                postAddRecyclerAdaptor.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.add:

                if ( (mUser != null) && (mAuth != null) ) {

                    startActivity(new Intent(this, AddPostActivity.class));
                    finish(); // finish this activity

                }

                break;

            case R.id.sign_out:

                if ( (mUser != null) && (mAuth != null) ) {

                    mAuth.signOut();
                    startActivity(new Intent(this, MainActivity.class));
                    finish(); // finish this activity

                }

                break;

        }

        return super.onOptionsItemSelected(item);
    }
}
