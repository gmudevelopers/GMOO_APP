package gmoo.com.gmudevelopers.edu.gmoo.model;

import java.io.Serializable;

/**
 *
 * Created by Eric on 1/10/2018.
 */

public class Category implements Serializable {

    private static final long serialVersionUID = 595841184825193826L;

    private String title;

    public Category() {
    }

    public Category(String title) {
        this.title = title;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Category{" +
                "title='" + title + '\'' +
                '}';
    }
}
