package com.battlehacknyc.owl;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


public class WatchActivity extends ActionBarActivity {
    private static int ANIMATION_TIME_OUT = 1000;
    String username;

    /* Owl data */
    private static String owlName;
    private static double latitude, longitude;
    private static double last_lat, last_long;
    private static long last_updated;
    private static String[] alerts;

    RequestQueue queue;

    // Tag used to log messages
    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);

        Intent intent = getIntent();
        username = intent.getStringExtra(NameActivity.USERNAME);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_watch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateData(View view)  {

        // Setup Volley networking request
        queue = Volley.newRequestQueue(this); // Need to set up a queue that holds all Volley requests
        String url = "http://agnok.com/get_state?from="+username;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(
                                getApplicationContext(),
                                response,
                                Toast.LENGTH_SHORT)
                                .show();

                        // Handle the response here.
                        try {
                            JSONObject data = new JSONObject(response);
                            owlName = data.getString("name");
                            latitude = data.getDouble("lat");
                            longitude = data.getDouble("lon");
                            last_lat = data.getDouble("orig_lat");
                            last_long = data.getDouble("orig_lon");
                            //alerts = data.getJSONArray())


                        } catch (Exception e) {

                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, error.toString(), error);
                    Toast.makeText(
                            getApplicationContext(),
                            "Error updating data.",
                            Toast.LENGTH_SHORT)
                            .show();
                    }
            });

        // Add the request to the Volley request queue
        queue.add(request);
    }
}
