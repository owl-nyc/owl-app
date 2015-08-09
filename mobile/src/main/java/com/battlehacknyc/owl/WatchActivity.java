package com.battlehacknyc.owl;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class WatchActivity extends ActionBarActivity {
    private static int ANIMATION_TIME_OUT = 1000;
    public static String NightOwl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);


        /* Grab elements from XML */
        TextView heading = (TextView) findViewById(R.id.activity_watch_heading);


        /* Grab username from intent if possible */
        Intent intent = getIntent();
        NightOwl = intent.getStringExtra(WatchSelectOwlActivity.USERNAME_TO_WATCH);


        if (NightOwl == null) {
            /* Run this if there isn't a session running */

            heading.setText("You currently aren't watching an Owl.");
            Toast.makeText(
                    getApplicationContext(),
                    "Redirecting to Choose an Owl",
                    Toast.LENGTH_SHORT)
                    .show();

            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(WatchActivity.this, WatchSelectOwlActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }, ANIMATION_TIME_OUT);
        } else {
            /* There is currently a session running. */

            heading.setText(NightOwl);




        }
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
}
