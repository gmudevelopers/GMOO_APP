package gmoo.com.gmudevelopers.edu.gmoo.ui.staggeredgrid.griditems;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import gmoo.com.gmudevelopers.edu.gmoo.R;
import gmoo.com.gmudevelopers.edu.gmoo.ui.staggeredgrid.PictureActivity;
import gmoo.com.gmudevelopers.edu.gmoo.ui.staggeredgrid.StaggeredDemoApplication;
import gmoo.com.gmudevelopers.edu.gmoo.ui.staggeredgrid.StaggeredGridViewItem;
import gmoo.com.gmudevelopers.edu.gmoo.ui.staggeredgrid.model.FlickrImage;
import gmoo.com.gmudevelopers.edu.gmoo.ui.staggeredgrid.model.FlickrProfileResponse;
import gmoo.com.gmudevelopers.edu.gmoo.ui.staggeredgrid.volley.GsonRequest;


public class FlickrGridItem3 extends StaggeredGridViewItem {

	private RequestQueue mVolleyQueue;
	private ImageLoader mImageLoader;
	private FlickrImage mImage;
	private String mUserId;
	private View mView;
	private TextView mProfileName;
	private ImageView mProfileImage;
	private String mProfileUrl;
	private int mHeight;
	private  Context mContext;
	
	public FlickrGridItem3(Context context, FlickrImage image) {
		mContext = context;
		mImage=image;
		mUserId=image.getOwner();
		mProfileUrl = "http://www.flickr.com/people/"+mUserId;
		mVolleyQueue= StaggeredDemoApplication.getRequestQueue();
		mImageLoader=StaggeredDemoApplication.getImageLoader();
	}
	
	private void flickrGetUserRequest() {
		
		String url = "http://api.flickr.com/services/rest";
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("api_key", "5e045abd4baba4bbcd866e1864ca9d7b");
		builder.appendQueryParameter("method", "flickr.people.getInfo");
		builder.appendQueryParameter("format", "json");
		builder.appendQueryParameter("nojsoncallback", "1");
		builder.appendQueryParameter("user_id", mUserId);
		
		GsonRequest<FlickrProfileResponse> gsonObjRequest = new GsonRequest<FlickrProfileResponse>(Request.Method.GET, builder.toString(),
				FlickrProfileResponse.class, null, new Response.Listener<FlickrProfileResponse>() {
			@Override
			public void onResponse(FlickrProfileResponse response) {
				try { 
					if(response != null) {
				        mImageLoader.get(response.getPerson().getProfileImageUrl(), 
								ImageLoader.getImageListener(mProfileImage, R.drawable.bg_no_image, android.R.drawable.ic_dialog_alert));
				        mProfileName.setText(response.getPerson().getProfileName());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// Handle your error types accordingly.For Timeout & No connection error, you can show 'retry' button.
				// For AuthFailure, you can re login with user credentials.
				// For ClientError, 400 & 401, Errors happening on client side when sending api request.
				// In this case you can check how client is forming the api and debug accordingly.
				// For ServerError 5xx, you can do retry or handle accordingly.
				if( error instanceof NetworkError) {
				} else if( error instanceof ServerError) {
				} else if( error instanceof AuthFailureError) {
				} else if( error instanceof ParseError) {
				} else if( error instanceof NoConnectionError) {
				} else if( error instanceof TimeoutError) {
				}
				System.out.println("########## onError ########## "+error.getMessage());
			}
		});
		mVolleyQueue.add(gsonObjRequest);
	}
	
	@Override
	public View getView(LayoutInflater inflater, ViewGroup parent) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.grid_item3, null);
		ImageView image = (ImageView) mView.findViewById(R.id.image);
		mProfileImage = (ImageView)mView.findViewById(R.id.profile_image);
		mProfileName = (TextView) mView.findViewById(R.id.profile_name);
		
        mImageLoader.get(mImage.getImageUrl(), 
				ImageLoader.getImageListener(image,R.drawable.bg_no_image, android.R.drawable.ic_dialog_alert),parent.getWidth(),0);
		
        image.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, PictureActivity.class);
				intent.putExtra(PictureActivity.IMAGE_URL, mImage.getImageUrl());
				mContext.startActivity(intent);
			}
		});

        mProfileImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mProfileUrl));
				mContext.startActivity(browserIntent);
			}
		});

        mProfileName.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mProfileUrl));
				mContext.startActivity(browserIntent);
			}
		});
        
		flickrGetUserRequest();
		return mView;
	}

	@Override
	public int getViewHeight(LayoutInflater inflater, ViewGroup parent) {
		RelativeLayout item_containerFrameLayout = (RelativeLayout)mView.findViewById(R.id.container);
		item_containerFrameLayout.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		mHeight = item_containerFrameLayout.getMeasuredHeight();
		return mHeight;
	}


}
