package gmoo.com.gmudevelopers.edu.gmoo.ui.staggeredgrid.volley;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;


/**
 * LruCache: For caches that do not override sizeOf(K, V), this is the maximum number of entries in the cache. 
 * For all other caches, this is the maximum sum of the sizes of the entries in this cache.
 * Ref:http://developer.android.com/reference/android/util/LruCache.html#LruCache(int)
 * 
 * Currently, the cache keeps maximum of 100 Cache entries.
 * @author Mani Selvaraj
 *
 */
public class BitmapLruCache extends LruCache<String,Bitmap> implements ImageLoader.ImageCache {
    public BitmapLruCache(int maxSize) {
        super(maxSize);
        //or setLimit(Runtime.getRuntime().maxMemory()/4);
    }
    
    /*@Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }*/
 
    @Override
    public Bitmap getBitmap(String url) {
        return (Bitmap)get(url);
    }
 
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
    
    public void clearBitmapCache() {
    	this.evictAll();
    }
}

