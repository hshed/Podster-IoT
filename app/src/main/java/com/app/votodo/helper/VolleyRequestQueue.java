package com.app.votodo.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by anuj on 04/08/16.
 */
public class VolleyRequestQueue {

    public static final String DEFAULT_TAG = "VolleyRequest";
    private static VolleyRequestQueue mInstance;
    private final ImageLoader mImageLoader;
    private RequestQueue mRequestQueue;
    private Context mContext;

    private VolleyRequestQueue(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();

         mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });


    }

    public static synchronized VolleyRequestQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyRequestQueue(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(DEFAULT_TAG);
        getRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? DEFAULT_TAG : tag);
        getRequestQueue().add(request);
    }

    public ImageLoader getmImageLoader() {
        return mImageLoader;
    }

    public void cancelPendingRequests(Object tag) {
        getRequestQueue().cancelAll(tag);
    }
}
