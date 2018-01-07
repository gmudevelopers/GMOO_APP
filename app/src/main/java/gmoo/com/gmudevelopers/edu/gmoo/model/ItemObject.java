package gmoo.com.gmudevelopers.edu.gmoo.model;

import java.io.Serializable;

/**
 * Created by daniel on 1/5/18.
 */

public class ItemObject implements Serializable {

    /**
     * Serial Version unique id needed for serialization across multiple JVM
     */
    private static final long serialVersionUID = -6618815665851099001L;

    private String name;

    private int photo;

    public ItemObject(int photo) {
        this.photo = photo;
    }

    public ItemObject(String name, int photo) {
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemObject that = (ItemObject) o;

        if (photo != that.photo) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + photo;
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ItemObject{");
        sb.append("name='").append(name).append('\'');
        sb.append(", photo=").append(photo);
        sb.append('}');
        return sb.toString();
    }
}
