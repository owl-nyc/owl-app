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

import org.json.JSONArray;
import org.json.JSONObject;


public class WatchActivity extends ActionBarActivity {
    private static int ANIMATION_TIME_OUT = 1000;
    String username;

    /* Owl data */
    private static String owlName;
    private static double latitude, longitude;
    private static double last_lat, last_long;
    private static long last_updated;
    private static String[] alertMessages;


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
        String url = "http://agnok.com/get_state?from="+"genji";

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
                            JSONArray dataList = new JSONArray(response);
                            JSONObject data = new JSONObject(dataList.getString(0));
                            owlName = data.getString("name");
                            latitude = data.getDouble("lat");
                            longitude = data.getDouble("lon");
                            last_lat = data.getDouble("orig_lat");
                            last_long = data.getDouble("orig_lon");
                            last_updated = data.getLong("last_updated");

                            System.out.println();
                            System.out.println();
                            System.out.println();
                            System.out.println();
                            System.out.println();
                            System.out.println();
                            System.out.println();
                            System.out.println();
                            System.out.println();
                            System.out.println();
                            System.out.println();
                            System.out.println();
                            System.out.println(owlName);

                            JSONArray alerts = data.getJSONArray("alerts");
                            alertMessages = new String[alerts.length()];
                            for (int i=0; i<alertMessages.length; i++) {
                                alertMessages[i] = alerts.getString(i);
                            }

                            TextView activity_watch_alerts = (TextView) findViewById(R.id.activity_watch_alerts),
                                    activity_watch_name = (TextView) findViewById(R.id.activity_watch_name),
                                    activity_watch_last = (TextView) findViewById(R.id.activity_watch_last),
                                    activity_watch_latitude = (TextView) findViewById(R.id.activity_watch_latitude),
                                    activity_watch_longitude = (TextView) findViewById(R.id.activity_watch_longitude),
                                    activity_watch_orig_lat = (TextView) findViewById(R.id.activity_watch_orig_lat),
                                    activity_watch_orig_lon = (TextView) findViewById(R.id.activity_watch_orig_lon);

                            activity_watch_name.setText(owlName);
                            activity_watch_last.setText(Long.toString(last_updated));
                            activity_watch_latitude.setText(Double.toString(latitude));
                            activity_watch_longitude.setText(Double.toString(longitude));
                            activity_watch_orig_lat.setText(Double.toString(last_lat));
                            activity_watch_orig_lon.setText(Double.toString(last_long));

                            for (int i = 0; i<alertMessages.length; i++) {
                                if (alertMessages[i] == "battery") {
                                    activity_watch_alerts.append(owlName + " appears to be low on battery.\n");
                                } else if (alertMessages[i] == "out_of_bounds") {
                                    activity_watch_alerts.append(owlName + " is out of the safe or known area!");
                                } else if (alertMessages[i] == "speeding") {
                                    activity_watch_alerts.append(owlName + " is moving at an incredible speed, and may be drunk driving.");
                                } else if (alertMessages[i] == "lost_track") {
                                    activity_watch_alerts.append(owlName + " hasn't responded in more than 3 minutes.");
                                } else if (alertMessages[i] == "12_hour_lost") {
                                    activity_watch_alerts.append(owlName + " hasn't responded in more than 12 hours.");
                                }
                            }

                        } catch (Exception e) {
                            System.out.println();
                            System.out.println();
                            System.out.println();
                            System.out.println();
                            System.out.println();
                            System.out.println();
                            System.out.println();
                            System.out.println(e);
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

    public void vibrate(View view) {
        Intent intentVibrate =new Intent(getApplicationContext(),VibrateService.class);
        startService(intentVibrate);
    }

}
