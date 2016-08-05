package com.app.votodo.helper;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.app.votodo.BuildConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anuj on 05/08/16.
 */
public class TaskHelper {

    public static final String JSON_URL = "https://lsaldfajsldbj3829.herokuapp.com/TaskList/";

    public static void getTaskList(final String userIdentifier, Response.Listener<com.app.votodo.model.Task> listener, Response.ErrorListener errorListener, Context context) {


        GsonRequest<com.app.votodo.model.Task> getTasks =
                new GsonRequest<com.app.votodo.model.Task>(Request.Method.GET, JSON_URL, com.app.votodo.model.Task.class, null,
                        listener, errorListener) {

                    @Override
                    protected Response<com.app.votodo.model.Task> parseNetworkResponse(NetworkResponse response) {
                        Log.d("response-headers", response.headers.toString());
                        return super.parseNetworkResponse(response);
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(NetworkConfig.ACCEPT, NetworkConfig.APPLICATION_JSON);
                        params.put(NetworkConfig.CONTENT_TYPE, NetworkConfig.URL_ENCODED);
                        params.put(NetworkConfig.USER_IDENTIFIER, userIdentifier);
                        params.put(NetworkConfig.API_TOKEN_KEY, "Podstur");
                        return params;
                    }

                };
        getTasks.setRetryPolicy(new DefaultRetryPolicy(
                NetworkConfig.MY_SOCKET_TIMEOUT_MS,
                NetworkConfig.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyRequestQueue.getInstance(context).addToRequestQueue(getTasks);
    }

}
