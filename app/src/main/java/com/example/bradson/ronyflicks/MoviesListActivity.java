package com.example.bradson.ronyflicks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.loopj.android.http.AsyncHttpClient.log;

public class MoviesListActivity extends AppCompatActivity {
    //constants
    //the base URL for API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    //the parameter name for the API key
    public final static String API_KEY_PARAM = "api_key";
    // tag for logging from this  activity
    public final static String TAG = "MoviesListActivity";

    // Instant field
    AsyncHttpClient client;
    // the base url for loading images
    String imageBaseUrl;
    // the poster size to use when fetching image, part of url
    String posterSize;
    // the

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);
        client = new AsyncHttpClient();
        // get the configuration on app creation
        getConfiguration();
    }
    // Get the configuration from the API
    private void getConfiguration(){
        // create the URl
        String url = API_BASE_URL + "/configuration";
        // set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); // API KEY always require
        // execute a get request expecting JSON object response
        client.get(url,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    JSONObject images = response.getJSONObject("images");
                    // get the image base url
                    imageBaseUrl = images.getString("secure_base_url");
                    // get the Poster Size
                    JSONArray posterSizeOption = images.getJSONArray("poster_sizes");
                    // use the option at the index 3 or w342 as fallback
                    posterSize = posterSizeOption.optString(3,"w342");
                } catch (JSONException e) {
                    logError("failed parsing configuration", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed getting configuration", throwable, true);
            }
        });
    }
    //handle errors, log and alert user
    private void logError(String message, Throwable error, boolean alertUser){
        // always log error
        log.e(TAG, message, error);
        // alert the user to avoid silent errors
        if (alertUser){
            // show a long toast with error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}
