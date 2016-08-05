package com.app.votodo.helper;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by anuj on 04/08/16.
 */
/**
 * Volley GET request which parses JSON server response into Java object.
 */

public class GsonRequest<T> extends JsonRequest<T> {

    private final Gson mGson;
    private final Class<T> mClassType;
    private final Map<String, String> mHeaders;
    private final Response.Listener<T> mListener;
    private String mContainerString = null;
    private int mStatusCode;


    public int getmStatusCode() {
        return mStatusCode;
    }

    public GsonRequest(int method, String url, Class<T> classType, JSONObject jsonRequest,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(method, url, classType, null, jsonRequest, listener, errorListener);
    }

    public GsonRequest(int method, String url, Class<T> classType, Map<String, String> headers,
                       JSONObject jsonRequest, Response.Listener<T> listener,
                       Response.ErrorListener errorListener) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener,
                errorListener);
        mGson = new Gson();
        mClassType = classType;
        mHeaders = headers;
        mListener = listener;
    }

    public void setmContainerString(String mContainerString) {
        this.mContainerString = mContainerString;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        mStatusCode = networkResponse.statusCode;
        Log.d("STATUS CODE" , String.valueOf(mStatusCode));

        try {
            String json = new String(networkResponse.data, HttpHeaderParser.parseCharset
                    (networkResponse.headers));
            Log.d("RESPONSE", json);

            HttpHeaderParser.parseCacheHeaders(networkResponse);
            if (mContainerString != null) {
                JSONObject object = new JSONObject(json);
                json = object.getString(mContainerString);
            }
            return Response.success(mGson.fromJson(json, mClassType),
                    HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }


    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse, int ttl) {
        mStatusCode = networkResponse.statusCode;
        Log.d("STATUS CODE" , String.valueOf(mStatusCode));
        try {
            String json = new String(networkResponse.data, HttpHeaderParser.parseCharset
                    (networkResponse.headers));

            Log.d("RESPONSE", json);

            Cache.Entry mFakeCache = HttpHeaderParser.parseCacheHeaders(networkResponse);
            mFakeCache.etag = null;
            mFakeCache.softTtl = System.currentTimeMillis() + ttl;
            mFakeCache.ttl = mFakeCache.softTtl;
            if (mContainerString != null) {
                JSONObject object = new JSONObject(json);
                json = object.getString(mContainerString);
            }
            return Response.success(mGson.fromJson(json, mClassType),
                    mFakeCache);
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }


    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }


}