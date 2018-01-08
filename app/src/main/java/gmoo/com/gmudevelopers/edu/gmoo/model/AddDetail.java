package gmoo.com.gmudevelopers.edu.gmoo.model;


import java.io.Serializable;

/**
 *
 */
public class AddDetail implements Serializable {

    /**
     * Serial Version unique id needed for serialization across multiple JVM
     */
    private static final long serialVersionUID = -5518270874218095406L;

    private String imageUrl;
    private String name;

    public AddDetail(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public AddDetail(String imageUrl, String name) {
        this.imageUrl = imageUrl;
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddDetail addDetail = (AddDetail) o;

        if (imageUrl != null ? !imageUrl.equals(addDetail.imageUrl) : addDetail.imageUrl != null)
            return false;
        return name != null ? name.equals(addDetail.name) : addDetail.name == null;
    }

    @Override
    public int hashCode() {
        int result = imageUrl != null ? imageUrl.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AddDetail{");
        sb.append("imageUrl='").append(imageUrl).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
