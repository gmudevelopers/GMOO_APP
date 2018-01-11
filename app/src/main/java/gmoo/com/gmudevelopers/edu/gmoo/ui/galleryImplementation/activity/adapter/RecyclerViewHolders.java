package gmoo.com.gmudevelopers.edu.gmoo.ui.galleryImplementation.activity.adapter;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import gmoo.com.gmudevelopers.edu.gmoo.R;


public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

   // public TextView countryName;
    public ImageView countryPhoto;
    public CardView cardview;
    private int mGridViewBGColor = Color.parseColor("#FF86A38B");
    public RecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        //countryName = (TextView)itemView.findViewById(R.id.country_name);
        countryPhoto = (ImageView)itemView.findViewById(R.id.country_photo);
        cardview = (CardView)itemView.findViewById(R.id.card);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StateListAnimator stateListAnimator = AnimatorInflater
                    .loadStateListAnimator(itemView.getContext(), R.drawable.lift_on_touch);
            cardview.setStateListAnimator(stateListAnimator);
        }
        cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Clicked me = " + getPosition(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
        ColorDrawable[] colors = {
                new ColorDrawable(Color.RED), // Animation starting color
                new ColorDrawable(mGridViewBGColor) // Animation ending color
        };

        // Initialize a new transition drawable instance
        TransitionDrawable transitionDrawable = new TransitionDrawable(colors);

        // Set the clicked item background
        view.setBackground(transitionDrawable);

        // Finally, Run the item background color animation
        // This is the grid view item click effect
        transitionDrawable.startTransition(600); // 600 Milliseconds
    }
    }
