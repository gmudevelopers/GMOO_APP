package gmoo.com.gmudevelopers.edu.gmoo.model;


import java.io.Serializable;

public class AddDetail  implements Serializable{
    private String imageUrl;

    private String name;

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

    public AddDetail(String imageUrl){
        this.imageUrl = imageUrl;
        //  this.name = name;
    }
}
