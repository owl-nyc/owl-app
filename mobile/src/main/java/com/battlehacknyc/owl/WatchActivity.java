package com.battlehacknyc.owl;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
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

    private int mInterval = 5000; // 5 seconds by default, can be changed later
    private Handler mHandler;

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

        mHandler = new Handler();

        startRepeatingTask();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            updateData();
            mHandler.postDelayed(mStatusChecker, mInterval);
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_watch, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
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

    public void updateData()  {

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

                            String alert = "";
                            for (int i = 0; i<alertMessages.length; i++) {
                                if (alertMessages[i].equals("battery")) {
                                    alert += owlName + "'s battery appears to be extremely low; take note!.\n";
                                } else if (alertMessages[i].equals("out_of_bounds")) {
                                    alert += owlName + " is out of the safe or known area!\n";
                                } else if (alertMessages[i].equals("speeding")) {
                                    alert += owlName + " is moving at an incredible speed, and may be drunk driving.\n";
                                } else if (alertMessages[i].equals("lost_track")) {
                                    alert += owlName + " hasn't responded in more than 3 minutes.\n";
                                } else if (alertMessages[i].equals("12_hour_lost")) {
                                    alert += owlName + " hasn't responded in more than 12 hours.\n";
                                }
                            }

                            if (alert.equals("")) {
                                activity_watch_alerts.setText("No alerts at this time.");
                            } else {
                                activity_watch_alerts.setText(alert);
                                vibrate();
                                //Define Notification Manager
                                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

//Define sound URI
                                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                                        .setSmallIcon(R.drawable.newlogo)
                                        .setContentTitle("ALERT: "+owlName+ " has an emergency")
                                        .setContentText("ALERTALERTALERTALERTALERT\n"+alert)
                                        .setSound(soundUri); //This sets the sound to play

//Display notification
                                notificationManager.notify(0, mBuilder.build());
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

    public void vibrate() {
        Intent intentVibrate =new Intent(getApplicationContext(),VibrateService.class);
        startService(intentVibrate);
    }

}
