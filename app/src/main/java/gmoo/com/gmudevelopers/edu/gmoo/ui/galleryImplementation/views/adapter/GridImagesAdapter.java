package gmoo.com.gmudevelopers.edu.gmoo.ui.galleryImplementation.views.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import gmoo.com.gmudevelopers.edu.gmoo.R;
import gmoo.com.gmudevelopers.edu.gmoo.ui.galleryImplementation.views.MediaGalleryView;


public class GridImagesAdapter extends RecyclerView.Adapter<GridImagesAdapter.ViewHolder> {
    private static final String TAG = "GridImagesAdapter";
    private ArrayList<String> imageURLs;
    private Context mContext;
    private Drawable imgPlaceHolderResId;
    private MediaGalleryView.OnImageClicked mClickListener;
    private int mHeight;
    private int mWidth;

    public GridImagesAdapter(Context activity, ArrayList<String> imageURLs, Drawable imgPlaceHolderResId) {
        this.imageURLs = imageURLs;
        this.mContext = activity;
        this.imgPlaceHolderResId = imgPlaceHolderResId;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, null));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Glide.with(mContext)
                .load(imageURLs.get(holder.getAdapterPosition()))
                .placeholder(imgPlaceHolderResId)
                .into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickListener == null) return;
                mClickListener.onImageClicked(holder.getAdapterPosition());
            }
        });

        ViewGroup.LayoutParams params = holder.image.getLayoutParams();

        if (mHeight != -1 && mHeight != MediaGalleryView.DEFAULT)
            params.height = mHeight;

        if (mWidth != -1 && mWidth != MediaGalleryView.DEFAULT)
            params.width = mWidth;

        holder.image.setLayoutParams(params);
    }


    public void setImgPlaceHolder(Drawable imgPlaceHolderResId) {
        this.imgPlaceHolderResId = imgPlaceHolderResId;
    }

    @Override
    public int getItemCount() {
        return imageURLs.size();
    }

    public void setOnImageClickListener(MediaGalleryView.OnImageClicked onImageClickListener) {
        this.mClickListener = onImageClickListener;
    }

    public void setImageSize(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }


}
