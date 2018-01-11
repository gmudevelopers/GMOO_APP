package gmoo.com.gmudevelopers.edu.gmoo.model;

import java.io.Serializable;

/**
 *
 * Created by Eric on 1/10/2018.
 */

public class PostAdd implements Serializable {

    private static final long serialVersionUID = 1717956340341433290L;

    private String title;
    private String description;
    private String image;
    private String timestamp;
    private String userId;

    public PostAdd() {
    }

    public PostAdd(String title, String description, String image, String timestamp, String userId) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.timestamp = timestamp;
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
