package gmoo.com.gmudevelopers.edu.gmoo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

import gmoo.com.gmudevelopers.edu.gmoo.R;
import gmoo.com.gmudevelopers.edu.gmoo.model.PostAdd;

/**
 * Adaptor for the Post Add.
 * Created by Eric on 1/10/2018.
 */

public class PostAddRecyclerAdaptor extends RecyclerView.Adapter<PostAddRecyclerAdaptor.ViewHolder> {

    private static final String TAG = "PostAddRecyclerAdaptor";

    private Context context;
    private List<PostAdd> blogList;

    public PostAddRecyclerAdaptor(Context context, List<PostAdd> blogList) {
        this.context = context;
        this.blogList = blogList;
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * . Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_post_frame, parent, false);

        return new ViewHolder(view, context);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(PostAddRecyclerAdaptor.ViewHolder holder, int position) {

        // Where we bind the view and the data
        PostAdd postAdd = new PostAdd();

        String imageUrl;

        holder.title.setText(postAdd.getTitle());
        holder.description.setText(postAdd.getDescription());

        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(Long.valueOf(postAdd.getTimestamp())).getTime());

        // April 17 2017
        holder.timestamp.setText(formattedDate);
        imageUrl = postAdd.getImage();

        //TODO: Use Picasso library to load image
        Picasso.with(context)
                .load(imageUrl)
                .into(holder.imageView);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return blogList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private TextView description;
        private TextView timestamp;
        private ImageView imageView;

        private String userId;

        public ViewHolder(View itemView, Context context) {
            super(itemView);

            PostAddRecyclerAdaptor.this.context = context;

            title = itemView.findViewById(R.id.postTitleList);
            description = itemView.findViewById(R.id.postDescriptionList);
            timestamp = itemView.findViewById(R.id.postTimestampList);
            imageView = itemView.findViewById(R.id.postImageList);

            userId = null;
        }

        @Override
        public void onClick(View view) {




        }
    }
}
