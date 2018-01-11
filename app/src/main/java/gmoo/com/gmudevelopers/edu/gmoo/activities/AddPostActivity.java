package gmoo.com.gmudevelopers.edu.gmoo.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import gmoo.com.gmudevelopers.edu.gmoo.R;
import gmoo.com.gmudevelopers.edu.gmoo.ui.HomeActivity;

public class AddPostActivity extends AppCompatActivity {

    public static final int GALLERY_CODE = 1;

    private ImageButton mPostImage;

    private EditText mPostTitle;
    private EditText mPostDescription;
    private EditText mPostTimestamp;

    private Button mAddPostButton;

    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mPostDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    private Uri mImageUri; // Uniform Resource Identifier
    private StorageReference mStorage;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);


        mProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference();

        mPostDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mPostDatabase.getReference().child("Post Adds");
        mDatabaseReference.keepSynced(true);

        mPostImage = findViewById(R.id.addPostImageBtn);
        mPostTitle = findViewById(R.id.postTitle);
        mPostDescription = findViewById(R.id.postDescription);
        mAddPostButton = findViewById(R.id.addPostBtn);

        mAddPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Posting to our database
                startPosting();
            }
        });

        mPostImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");

                startActivityForResult(galleryIntent, GALLERY_CODE);

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {

            mImageUri = data.getData();
            mPostImage.setImageURI(mImageUri);

        }
    }

    private void startPosting() {

        mProgress.setMessage("Posting to add...");
        mProgress.show();

        String titleValue = mPostTitle.getText().toString().trim();
        String description = mPostDescription.getText().toString().trim();

        if (!TextUtils.isEmpty(titleValue) && !TextUtils.isEmpty(description) && mImageUri != null) {
            // Start the uploading...

            // mImageUri.getLastPathSegment() = /image/myphoto.jpg.... example
            StorageReference filePath =
                    mStorage.child("Post_Images").child(mImageUri.getLastPathSegment());

            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost = mDatabaseReference.push();

                    Map<String, String> dataToSave = new HashMap<>();
                    dataToSave.put("title", titleValue);
                    dataToSave.put("description", description);
                    if (downloadUrl != null) {
                        dataToSave.put("image", downloadUrl.toString());
                    }

                    dataToSave.put("timestamp", String.valueOf(System.currentTimeMillis()));

                    dataToSave.put("userId", mUser.getUid());

                    newPost.setValue(dataToSave);

                    mProgress.dismiss();

                    startActivity(new Intent(AddPostActivity.this, HomeActivity.class));
                    finish();

                }
            });


        }

    }

}
