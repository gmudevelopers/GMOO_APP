package gmoo.com.gmudevelopers.edu.gmoo.ui.galleryImplementation.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import gmoo.com.gmudevelopers.edu.gmoo.R;
import gmoo.com.gmudevelopers.edu.gmoo.ui.galleryImplementation.views.MediaGalleryView;


public class SelectedItemGallery extends AppCompatActivity implements MediaGalleryView.OnImageClicked{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_item_gallery);

       int pos = getIntent().getExtras().getInt("clickeditem");

        MediaGalleryView view = (MediaGalleryView) findViewById(R.id.gallery);
        view.setImages(getFakeList());
        view.setOnImageClickListener(this);
        view.setPlaceHolder(R.drawable.media_gallery_placeholder);
        view.setOrientation(MediaGalleryView.VERTICAL);
        view.setImageSize(500,MediaGalleryView.DEFAULT);
        view.notifyDataSetChanged();

    }

    @Override
    public void onImageClicked(int pos) {
        MediaGallery.Builder(this,getFakeList())
                .title("Media Gallery")
                .backgroundColor(R.color.white)
                .placeHolder(R.drawable.media_gallery_placeholder)
                .selectedImagePosition(pos)
                .show();
    }


    private ArrayList<String> getFakeList() {
        ArrayList<String> imagesList = new ArrayList<>();
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/12/23193634/tumblr_oiboua3s6F1slhhf0o1_500.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/08192732/tumblr_oev1qbnble1ted1sho1_500.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/18184202/tumblr_ntyttsx2Y51ted1sho1_500.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/25093310/2016-03-01-roman-drits-barnimages-008-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/25093310/2016-03-01-roman-drits-barnimages-008-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/25093331/tumblr_ofz20toUqd1ted1sho1_500.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/25093334/2016-11-21-roman-drits-barnimages-003-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/02093356/DSF1919-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/02093347/2016-11-21-roman-drits-barnimages-009-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/12/16094158/2016-12-05-roman-drits-barnimages-011-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/12/16094159/tumblr_o2z8oh0Ntt1ted1sho1_1280-683x1024.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/12/23193617/2016-11-13-barnimages-igor-trepeshchenok-01-768x509.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/08192739/tumblr_ofem6n49F61ted1sho1_500.jpg");
        return imagesList;
    }
}
