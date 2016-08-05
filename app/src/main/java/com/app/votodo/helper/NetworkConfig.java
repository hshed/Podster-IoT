package com.app.votodo.helper;

/**
 * Created by anuj on 04/08/16.
 */
public class NetworkConfig {

    /**
     * Header keys
     */
    static String ACCEPT = "Accept";
    static String CONTENT_TYPE="content-type";
    static String USER_IDENTIFIER = "X-User-Identifier";
    static String API_TOKEN_KEY = "X-API-TOKEN";

    /**
     * Header Values
     */

    static String APPLICATION_JSON = "application/json";
    static String URL_ENCODED = "application/x-www-form-urlencoded";

    static final int MY_SOCKET_TIMEOUT_MS = 60000;
    static final int DEFAULT_MAX_RETRIES = 3;


}
