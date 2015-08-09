package com.battlehacknyc.owl;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    public static String username;

    /* Method to trigger the Night Owl activity. */
    public void startNightOwlActivity(View view) {
        Intent intent = new Intent(this, NightOwlActivity.class);
        startActivity(intent);
    }

    /* Method to trigger the Designated activity */
    public void startWatchActivity(View view) {
        Intent intent = new Intent(this, WatchActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        username = intent.getStringExtra(NameActivity.USERNAME);

        String welcome = username + ", are you a Watch or an Owl tonight?";

        // Show the welcome message
        TextView welcomeMessage = (TextView) findViewById(R.id.activity_main_welcome);
        welcomeMessage.setText(welcome);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
