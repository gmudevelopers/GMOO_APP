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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import gmoo.com.gmudevelopers.edu.gmoo.R;
import gmoo.com.gmudevelopers.edu.gmoo.ui.HomeActivity;

public class AddPostActivity extends AppCompatActivity {

    private static final String TAG = "AddPostActivity";

    public static final int GALLERY_CODE = 1;
    public static final int CAMERA_REQUEST_CODE = 10;
    public static final int REQUEST_CAMERA = 1;
    public static final int SELECT_FILE = 0;
    public static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 12;

    private ImageButton mPostImage;

    private EditText mPostTitle;
    private EditText mPostDescription;

    private Button mAddPostButton;

    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mPostDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    private Bitmap bmp = null;

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

        FloatingActionButton fabCamera = findViewById(R.id.addPostCamera);
        FloatingActionButton fabGallery = findViewById(R.id.addPostGallery);


        fabCamera.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                    invokeCamera();

                } else {

                    String[] permissionsRequested = {Manifest.permission.CAMERA};
                    requestPermissions(permissionsRequested, CAMERA_REQUEST_CODE);

                }

            }
        });


        fabGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invokeGallery();
            }
        });


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
                invokeCamera();
            }
        });

    }

    private void invokeCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        }
    }

    private void invokeGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_CAMERA) {

                Bundle bundle = data.getExtras();

                if (bundle != null) {
                    bmp = (Bitmap) bundle.get("data");
                    //mImageUri = getImageUri(AddPostActivity.this, bmp);
                    this.mImageUri = data.getData();

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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                invokeCamera();
            } else {
                Toast.makeText(this, "Cannot process image without permission", Toast.LENGTH_LONG).show();
            }

        } else if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.mImageUri = getImageUri(AddPostActivity.this, bmp);
            } else {
                Toast.makeText(this, "Cannot write image without permission", Toast.LENGTH_LONG).show();
            }

        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void startPosting() {

        mProgress.setMessage("Posting to add...");
        mProgress.show();

        String titleValue = mPostTitle.getText().toString().trim();
        String description = mPostDescription.getText().toString().trim();

        if (!TextUtils.isEmpty(titleValue) && !TextUtils.isEmpty(description) && mImageUri != null) {

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

        } else {
            Log.d(TAG, "startPosting: mImageUrl is " + mImageUri);
        }
    }

}
