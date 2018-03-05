package gmoo.com.gmudevelopers.edu.gmoo.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import gmoo.com.gmudevelopers.edu.gmoo.R;
import gmoo.com.gmudevelopers.edu.gmoo.ui.HomeActivity;

/**
 *
 */
public class AddPostActivity extends AppCompatActivity {

    private static final String TAG = "AddPostActivity";

    public static final int CAMERA_REQUEST_CODE = 10;
    public static final int REQUEST_CAMERA = 1;
    public static final int SELECT_FILE = 0;
    public static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 12;

    private ImageView mPostImage;

    private EditText mPostTitle;
    private EditText mPostDescription;

    private DatabaseReference mDatabaseReference;
    private FirebaseUser mUser;

    private Bitmap bmp = null;

    private Uri mImageUri; // Uniform Resource Identifier
    private StorageReference mStorage;

    private ProgressDialog mProgress;


    /**
     * @param savedInstanceState - The bundle to be passed to the method.
     */
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);


        mProgress = new ProgressDialog(this);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference();

        FirebaseDatabase mPostDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mPostDatabase.getReference().child("Post Adds");
        mDatabaseReference.keepSynced(true);

        mPostImage = findViewById(R.id.addPostImageView);
        mPostTitle = findViewById(R.id.postTitle);
        mPostDescription = findViewById(R.id.postDescription);
        Button mAddPostButton = findViewById(R.id.addPostBtn);

        FloatingActionButton fabCamera = findViewById(R.id.addPostCamera);
        FloatingActionButton fabGallery = findViewById(R.id.addPostGallery);


        /*
          This is the floating button for camera browsing.
         */
        fabCamera.setOnClickListener(view -> {
            // Check and ensure that appropriate permission is granted otherwise request for it.
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                invokeCamera();
            } else {
                String[] permissionsRequested = {Manifest.permission.CAMERA};
                requestPermissions(permissionsRequested, CAMERA_REQUEST_CODE);
            }
        });

        /*
          This is the floating button for gallery browsing.
         */
        fabGallery.setOnClickListener(view -> invokeGallery());


        mAddPostButton.setOnClickListener(view -> {
            // Posting to our database
            startPosting();
        });

        /* Clicking on the image view can also be clicked to activate camera. */
        mPostImage.setOnClickListener(view -> invokeCamera());

    }

    /**
     * Called by the fabCamera to activate and enable user camera for uploading images.
     */
    private void invokeCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        }
    }

    /**
     * Called by the fabGallery to browse image files and allow selection
     */
    private void invokeGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    /**
     * All results returned by the Activity is handled here.
     *
     * @param requestCode - uniquely identify the appropriate request processed.
     * @param resultCode  - the status of the request on return if success or failure.
     * @param data        - the data returned.
     */
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_CAMERA) {

                Bundle bundle = data.getExtras();

                if (bundle != null) {
                    bmp = (Bitmap) bundle.get("data");
                    this.mImageUri = data.getData();

                    // Request or permission if we do not have permission to write externally
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        this.mImageUri = getImageUri(AddPostActivity.this, bmp);
                    } else {
                        String[] permissionsRequested = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissionsRequested, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                    }

                    mPostImage.setImageURI(data.getData());
                    mPostImage.setImageBitmap(bmp);

                } else {
                    Log.d(TAG, "onActivityResult: bundle is null");
                }


            } else if (requestCode == SELECT_FILE) {
                this.mImageUri = data.getData();
                mPostImage.setImageURI(mImageUri);
            }
        }
    }

    /**
     * Permissions can be requested and the expected response from user is retrieved here.
     *
     * @param requestCode  - code to locate the appropriate permission requested
     * @param permissions  - the permission requested to be granted by user
     * @param grantResults - the results received per user's response.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Signals response for camera permission requested
        if (requestCode == CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                invokeCamera();
            } else {
                Toast.makeText(this, "Cannot process image without permission", Toast.LENGTH_LONG).show();
            }

        } else if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) { // to write to external storage

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.mImageUri = getImageUri(AddPostActivity.this, bmp);
            } else {
                Toast.makeText(this, "Cannot write image without permission", Toast.LENGTH_LONG).show();
            }

        }

    }

    /**
     * Helper method to convert Bitmap to Uri to be stored in database along with user's post.
     *
     * @param inContext - Current context for the URI
     * @param inImage   - Bitmap (bmp) to be converted to Uri.
     * @return the requested URI of the Bitmap.
     */
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /**
     * This is method for writing user's data to the database. Data must be populated and instantiated
     * before this method can successfully process.
     * <p>
     * User must be logged in for a successful post to database.
     */
    private void startPosting() {

        // Start progress to notify user of data processing.
        mProgress.setMessage("Posting to add...");
        mProgress.show();

        // Retrieve value of the title of post
        String titleValue = mPostTitle.getText().toString().trim();
        // Retrieve the description of the post
        String description = mPostDescription.getText().toString().trim();

        /* Ensure that the title, description and image Uri is not null. if it is do not process */
        if (!TextUtils.isEmpty(titleValue) && !TextUtils.isEmpty(description) && mImageUri != null) {

            // mImageUri.getLastPathSegment() = /image/myphoto.jpg.... example
            StorageReference filePath =
                    mStorage.child("Post_Images").child(mImageUri.getLastPathSegment());

            // If there is a successful save of the image to the storage, then process the info.
            // This indicates a valid image URI
            filePath.putFile(mImageUri).addOnSuccessListener(taskSnapshot -> {

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

                // The map object is saved to database with the setValue() and progress dismissed
                newPost.setValue(dataToSave);
                mProgress.dismiss();

                // User is transitioned to the home activity displaying all adds available.
                startActivity(new Intent(AddPostActivity.this, HomeActivity.class));
                finish();

            });

        } else {
            Log.d(TAG, "startPosting: mImageUrl is " + mImageUri);
        }
    }

}
