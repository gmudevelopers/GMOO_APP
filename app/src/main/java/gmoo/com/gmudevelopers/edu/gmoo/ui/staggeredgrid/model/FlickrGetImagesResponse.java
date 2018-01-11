package gmoo.com.gmudevelopers.edu.gmoo.ui.staggeredgrid.model;

import java.util.List;

public class FlickrGetImagesResponse {

	public String id;
	
	List<FlickrImage> photo;

	public List<FlickrImage> getPhotos() {
		return photo;
	}
}
