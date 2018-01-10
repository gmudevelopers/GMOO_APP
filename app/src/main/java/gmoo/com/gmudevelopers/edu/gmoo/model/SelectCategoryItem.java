package gmoo.com.gmudevelopers.edu.gmoo.model;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.widget.TextView;

import gmoo.com.gmudevelopers.edu.gmoo.adapters.SelectCategory_Adapter;

/**
 * Created by daniel on 1/9/18.
 */

public class SelectCategoryItem {
    String text;
   Drawable image;
    public SelectCategoryItem(String text, Drawable image){
        this.text = text;
        this.image = image;
    }

    public Drawable getImage() {
        return image;
    }

    public String getText() {
        return text;
    }
}
