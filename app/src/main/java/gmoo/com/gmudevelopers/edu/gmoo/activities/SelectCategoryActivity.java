package gmoo.com.gmudevelopers.edu.gmoo.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import gmoo.com.gmudevelopers.edu.gmoo.R;
import gmoo.com.gmudevelopers.edu.gmoo.ui.HomeActivity;
import gmoo.com.gmudevelopers.edu.gmoo.ui.SearchActivity;

public class SelectCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SelectCategoryActivity";

    public static final int REQUEST_CODE = 32498;

    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    private Button booksCategory;
    private Button electronicsCategory;
    private Button servicesCategory;
    private Button othersCategory;

    @BindView(R.id.drawer)
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        booksCategory = findViewById(R.id.booksCategoryId);
        electronicsCategory = findViewById(R.id.electronicsCategoryId);
        servicesCategory = findViewById(R.id.servicesCategoryId);
        othersCategory = findViewById(R.id.otherCategoryId);

        booksCategory.setOnClickListener(this);
        electronicsCategory.setOnClickListener(this);
        servicesCategory.setOnClickListener(this);
        othersCategory.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("Blog");
        mDatabaseReference.keepSynced(true);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_add:

                if ( (mUser != null) && (mAuth != null) ) {

                    startActivity(new Intent(this, AddPostActivity.class));
                    finish(); // finish this activity

                }

                break;

            case R.id.action_sign_out:

                if ( (mUser != null) && (mAuth != null) ) {

                    mAuth.signOut();
                    startActivity(new Intent(this, HomeActivity.class));
                    finish(); // finish this activity

                }

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.booksCategoryId:

                break;

            case R.id.electronicsCategoryId:

                break;

            case R.id.servicesCategoryId:

                break;

            case R.id.otherCategoryId:


                //TODO: Going to implement this as generic.
                startActivity(new Intent(SelectCategoryActivity.this, AddPostActivity.class));
                finish();

                break;

        }

    }
}
