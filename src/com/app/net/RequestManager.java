
package com.app.net;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import android.content.Context;
import android.text.TextUtils;

/**
 * 
 * @author Administrator
 * 
 */
public class RequestManager {

	public static final String TAG = RequestManager.class.getSimpleName();

	private static RequestQueue mRequestQueue;

	private static ImageLoader mImageLoader;

	private RequestManager() {
		// no instances
	}

	public static void init(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);
		mImageLoader = new ImageLoader(mRequestQueue, new com.app.net.BitmapLruCache());
	}

	public static RequestQueue getRequestQueue() {
		if (mRequestQueue != null) {
			return mRequestQueue;
		} else {
			throw new IllegalStateException("RequestQueue not initialized");
		}
	}

	/**
	 * Returns instance of ImageLoader initialized with {@see FakeImageCache}
	 * which effectively means that no memory caching is used. This is useful
	 * for images that you know that will be show only once.
	 * 
	 * @return
	 */
	public static ImageLoader getImageLoader() {
		if (mImageLoader != null) {
			return mImageLoader;
		} else {
			throw new IllegalStateException("ImageLoader not initialized");
		}
	}

	public static <T> void addRequest(Request<T> request, String tag) {
		request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		mRequestQueue.add(request);
	}

	public static <T> void addRequest(Request<T> request) {
		request.setTag(TAG);
		mRequestQueue.add(request);
	}

	public static void cancelAll(Object tag) {
		mRequestQueue.cancelAll(tag);
//		mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
//		    @Override
//		        public boolean apply(Request<?> request) {
//		    		Log.i("tag", "RequestQueue cancelAll called");
//		            return true;
//		        }
//		});
	}
}
