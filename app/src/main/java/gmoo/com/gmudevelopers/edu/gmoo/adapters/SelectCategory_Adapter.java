package gmoo.com.gmudevelopers.edu.gmoo.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import gmoo.com.gmudevelopers.edu.gmoo.R;
import gmoo.com.gmudevelopers.edu.gmoo.helpers.RecyclerViewClickListener;
import gmoo.com.gmudevelopers.edu.gmoo.model.SelectCategoryItem;
import gmoo.com.gmudevelopers.edu.gmoo.ui.PostAdd.PostAddActivity;

public class SelectCategory_Adapter extends RecyclerView.Adapter<SelectCategory_Adapter.ViewHolder>  {
    private String[] mDataset;
 private   View v;

 private ArrayList<SelectCategoryItem> items;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;

        public ImageView placePic;
        public RecyclerViewClickListener mListener;
        public ViewHolder(View v) {
            super(v);
            placePic = (ImageView) v.findViewById(R.id.category_pic);
            mTextView =(TextView) v.findViewById(R.id.category_text);
        }


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SelectCategory_Adapter(ArrayList<SelectCategoryItem> items) {
       this.items = items;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public SelectCategory_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
      //  TextView v = (TextView) LayoutInflater.from(parent.getContext())


        v   = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_category_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(items.get(position).getText());
        /*Picasso.with(v.getContext()).load("Ur").placeholder(items.get(position).getImage())
                .into(holder.placePic);*/
        holder.placePic.setImageDrawable( items.get(position).getImage());


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }
}


