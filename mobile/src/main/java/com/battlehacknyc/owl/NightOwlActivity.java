package com.battlehacknyc.owl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class NightOwlActivity extends ActionBarActivity {
    String username;

    Button btnShowLocation;

    // GPSTracker class
    GPSTracker gps;

    RequestQueue queue;

    // Tag used to log messages
    private static final String TAG = MainActivity.class.getSimpleName();



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




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_night_owl);

        gps = new GPSTracker(NightOwlActivity.this);

        Intent i = getIntent();
        username = i.getStringExtra(NameActivity.USERNAME);

    }


    /* Method to send GPS coordinates to the server. */
    public void sendGPS() {
        // create class object

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();



            // Setup Volley networking request
            queue = Volley.newRequestQueue(this); // Need to set up a queue that holds all Volley requests
            String url = "http://agnok.com/set_state?name="+username+"&lat="+latitude+"&lon"+longitude;

            StringRequest request = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Coordinates updated.",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, error.toString(), error);
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Watch not added.",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });

            // Add the request to the Volley request queue
            queue.add(request);


        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

    }

}

