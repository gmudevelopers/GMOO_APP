package gmoo.com.gmudevelopers.edu.gmoo.model;

/**
 * Created by daniel on 1/5/18.
 */

public class ItemObject {

    private String name;
    private int photo;

    public ItemObject(int photo) {

        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}
