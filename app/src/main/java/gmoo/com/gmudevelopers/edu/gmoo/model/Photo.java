package gmoo.com.gmudevelopers.edu.gmoo.model;

import android.net.Uri;

/**
 * Created by daniel on 1//18.
 */

public class Photo {
    private Uri imageUri;
    boolean selectable;

    public Photo(Uri imageUri) {

        this.imageUri = imageUri;
    }

    public Uri getImageUri() {
        return this.imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }


}
