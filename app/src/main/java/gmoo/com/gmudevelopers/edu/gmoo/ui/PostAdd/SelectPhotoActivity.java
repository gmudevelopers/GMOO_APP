package gmoo.com.gmudevelopers.edu.gmoo.ui.PostAdd;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.StandaloneActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.support.v7.view.ActionMode;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import gmoo.com.gmudevelopers.edu.gmoo.R;
import gmoo.com.gmudevelopers.edu.gmoo.activities.AddPostActivity;
import gmoo.com.gmudevelopers.edu.gmoo.activities.MainActivity;
import gmoo.com.gmudevelopers.edu.gmoo.adapters.PostAddPhotoListAdapter;
import gmoo.com.gmudevelopers.edu.gmoo.adapters.SelectCategory_Adapter;
import gmoo.com.gmudevelopers.edu.gmoo.helpers.RecyclerItemClickListener;
import gmoo.com.gmudevelopers.edu.gmoo.helpers.RecyclerViewClickListener;
import gmoo.com.gmudevelopers.edu.gmoo.model.Photo;
import gmoo.com.gmudevelopers.edu.gmoo.model.SelectCategoryItem;
import gmoo.com.gmudevelopers.edu.gmoo.ui.SelectCategoryActivity;


public class SelectPhotoActivity extends AppCompatActivity implements View.OnClickListener{


    private static final String DEMO_PHOTO_PATH = "MyDemoPhotoDir";
    private static final String TAG =  "SelectPhotoActivity";

    LinearLayout llPhotoContainer;

    public static final int GALLERY_CODE = 1;
    public static final int CAMERA_REQUEST_CODE = 10;
    public static final int REQUEST_CAMERA = 1;
    public static final int SELECT_FILE = 0;
    public static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 12;
    private Bitmap bmp = null;
    ActionMode mActionMode;
    RecyclerView rv_upload_photos;
    PostAddPhotoListAdapter photoListAdapter;
    private PostAddPhotoListAdapter mAdapter;

    @BindView(R.id.menu)
    FloatingActionMenu fabMenu;
 ////   @BindView(R.id.take_aphoto)
  FloatingActionButton take_photo;
 ////   @BindView(R.id.choose_existing)
    FloatingActionButton choose_photo;
    ArrayList<Photo> photos ;

    private Uri mImageUri;
    Toolbar toolbar;
    private int photoPosition=0;

    int COUNTER =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);
      //  toolbar = (Toolbar) findViewById(R.id.toolbar);
     //   toolbar.setTitle("Select Photos");
//        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.Red));
     //   setSupportActionBar(toolbar);
        choose_photo = (FloatingActionButton) findViewById(R.id.choose_existing);
        take_photo = (FloatingActionButton) findViewById(R.id.take_aphoto);
        rv_upload_photos = findViewById(R.id.rvphoto_cards);
        photos = new ArrayList<>(4);
        // specify an adapter (see also next example)


        // only for GALLERY pick and API >18
        //config.maxExportingSize = 1000;

        mAdapter = new PostAddPhotoListAdapter(items());


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        rv_upload_photos.setLayoutManager(mLayoutManager);
        rv_upload_photos.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(1), true));

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        rv_upload_photos.setItemAnimator(itemAnimator);

        // rv_upload_photos.setLayoutManager(new GridLayoutManager(SelectPhotoActivity.this, 2));
        rv_upload_photos.setAdapter(mAdapter);
     /*   rv_upload_photos.addOnItemTouchListener(

                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(SelectPhotoActivity.this, "[" + position + "]", Toast.LENGTH_SHORT).show();


                    }


                })



        );*/
     rv_upload_photos.addOnItemTouchListener(new RecyclerItemClickListener(this, rv_upload_photos,  new RecyclerItemClickListener.OnItemClickListener() {
         @Override
         public void onItemClick(View view, int position) {



             // Single item click
            // Toast.makeText(SelectPhotoActivity.this, "Single click event",Toast.LENGTH_SHORT).show();
         }

         @Override
         public void onItemLongClick(View view, int position) {
             // Long item click
           if(  mAdapter.getPhotoItem().get(position).isSelectable())
           {
               view.setHapticFeedbackEnabled(true);
               view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
             Toast.makeText(SelectPhotoActivity.this, "Long click event",Toast.LENGTH_SHORT).show();
               photoPosition = position;

               startSupportActionMode( mCallback );

               // Start the CAB



               view.setSelected(true);





         }
         }
     }));

     choose_photo.setOnClickListener(this);
     take_photo.setOnClickListener(this);


    }




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.choose_existing :

                invokeGallery();
                break;
            case R.id.take_aphoto :
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                    invokeCamera();

                } else {

                    String[] permissionsRequested = {Manifest.permission.CAMERA};
                    requestPermissions(permissionsRequested, CAMERA_REQUEST_CODE);

                }

                break;

        }

    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    public ArrayList<Photo> items() {





        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + getResources().getResourcePackageName(R.drawable.square)
                + '/' + getResources().getResourceTypeName(R.drawable.square) + '/' + getResources().getResourceEntryName(R.drawable.square) );
       Log.i("ContentUri",imageUri+"");
       Photo dummy = new Photo(null);
       dummy.setSelectable(false);
        photos.add(dummy);
        photos.add(dummy);
        photos.add(dummy);
        photos.add(dummy);
        return photos;


    }

    public void addPhototoList(Uri uri){

        if(COUNTER<4){
           Photo newPhoto = new Photo(uri);
           newPhoto.setSelectable(true);
            photos.remove(COUNTER);////
            photos.add(COUNTER, newPhoto);
            COUNTER++;
        }
        else {
            Toast.makeText(this, "Gallery ful",Toast.LENGTH_SHORT).show();
        }
        mAdapter.notifyDataSetChanged();
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
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    /**
     * Converting dp to pixel
     */


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
                this.mImageUri = getImageUri(SelectPhotoActivity.this, bmp);
            } else {
                Toast.makeText(this, "Cannot write image without permission", Toast.LENGTH_LONG).show();
            }

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_selectphoto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.done :
                getSelectedPhotos();
                break;

        }
        return super.onOptionsItemSelected(item);
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
                        this.mImageUri = getImageUri(SelectPhotoActivity.this, bmp);
                    } else {

                        String[] permissionsRequested = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissionsRequested, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                    }




                } else {
                    Log.d(TAG, "onActivityResult: bundle is null");
                }
                addPhototoList(mImageUri);

            } else if (requestCode == SELECT_FILE) {

                this.mImageUri = data.getData();
                addPhototoList(mImageUri);

            }

        }

    }
    private ActionMode.Callback mCallback = new ActionMode.Callback()
    {
        @Override
        public boolean onPrepareActionMode( ActionMode mode, Menu menu )
        {
            return false;
        }
        @Override
        public void onDestroyActionMode( ActionMode mode )
        {
            // TODO Auto-generated method stub
        }
        @Override
        public boolean onCreateActionMode( ActionMode mode, Menu menu )
        {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate( R.menu.actionmode_delete_menu, menu );
         //   toolbar.setVisibility(View.GONE);
           // MenuItem item = menu.findItem( R.id.action_text );




            return true;
        }
        @Override
        public boolean onActionItemClicked( ActionMode mode, MenuItem item )
        {
            boolean ret = false;
            if(item.getItemId() == R.id.action_delete)
            {
                deletePhoto(photoPosition);
                mode.finish();
                ret = true;
            }
            return ret;
        }
    };

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    private  void deletePhoto(int index){
        photos.remove(index);
        Photo newPhoto = new Photo(null);
        photos.add(newPhoto);
        COUNTER--;
        mAdapter.notifyDataSetChanged();


    }

    private ArrayList<Photo> getSelectedPhotos(){
        ArrayList<Photo> selectedphotos = new ArrayList<>();
        for(Photo photo : photos){

            if((photo.getImageUri()!=(null))){
                selectedphotos.add(photo);
                Log.i("Selected photo uri",""+ photo.getImageUri());
            }
        }

        return selectedphotos;
    }
}

