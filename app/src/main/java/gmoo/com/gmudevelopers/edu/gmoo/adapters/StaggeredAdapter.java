package gmoo.com.gmudevelopers.edu.gmoo.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import gmoo.com.gmudevelopers.edu.gmoo.R;
import gmoo.com.gmudevelopers.edu.gmoo.helpers.Constants;
import gmoo.com.gmudevelopers.edu.gmoo.model.AddDetail;


public class StaggeredAdapter extends RecyclerView.Adapter<StaggeredAdapter.ViewHolder> {

    private ArrayList<AddDetail> addList;
    View v;
    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView placeName;
        public ImageView placePic;
        //public Button addToWIshlist;

        public ViewHolder(View itemView) {
            super(itemView);
            // placeName = (TextView) itemView.findViewById(R.id.placeName);

            placePic = (ImageView) itemView.findViewById(R.id.placePic);
           // addToWIshlist = (Button) itemView.findViewById(R.id.btnWishList);
        }
    }

    public StaggeredAdapter(ArrayList<AddDetail> placeList){
        this.addList = placeList;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public StaggeredAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        v   = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.staggered_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        // holder.placeName.setText(placeList.get(position).getName());



        Picasso.with(v.getContext()).load(addList.get(position).getImageUrl())
                .into(holder.placePic);
        holder.placePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           /*     Bundle bundle=new Bundle();
                //bundle.putInt("clickeditem",position);
              //  Intent intent = new Intent(v.getContext(),MediaGalleryActivity.class);
                intent.putExtra(Constants.IMAGES,getFakeList());
                intent.putExtra(Constants.TITLE, "Fake title");
                intent.putExtra(Constants.BACKGROUND_COLOR,R.color.white);
                intent.putExtra(Constants.PLACE_HOLDER,R.drawable.media_gallery_placeholder);
                intent.putExtra(Constants.SELECTED_IMAGE_POSITION,position);


                v.getContext().startActivity(intent);
*/
                Toast.makeText(v.getContext(), "item clicked"+position,Toast.LENGTH_SHORT).show();

            }
        });
        /*holder.addToWIshlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.addToWIshlist.setBackgroundResource(R.drawable.wishlistselected);
                // Toast.makeText(v.getContext(),"Added to favorites", Toast.LENGTH_SHORT).show();
            }
        });
*/

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
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return addList.size();
    }
}


