package com.battlehacknyc.owl;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class WatchSelectOwlActivity extends ActionBarActivity {
    public final static String USERNAME_TO_WATCH = "com.battlehacknyc.owl.username_to_watch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_select_owl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_watch_select_owl, menu);
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


    public void confirmOwlName(View view) {
        EditText userSelect = (EditText) findViewById(R.id.activity_watch_select_owl_user);
        String username = userSelect.getText().toString().trim();

        if (username == "") {
             /* Wait until the server gives the OK. */
            Toast.makeText(
                    getApplicationContext(),
                    "Please enter a username",
                    Toast.LENGTH_SHORT)
                    .show();
        } else {
            if (usernameExists(username)) {

                Intent i = new Intent(WatchSelectOwlActivity.this, WatchActivity.class);

                i.putExtra(USERNAME_TO_WATCH, username);
                startActivity(i);

            } else {
                userSelect.setText("");
            }
        }
    }

    public boolean usernameExists(String username) {
        /* Ask the server if the username exists */
        boolean exists = true;
        if (exists) {
            String confirmed;
            /* Ask the server to ask the Owl for confirmation. */
            confirmed = "success";

            if (confirmed=="success") {
                 /* Wait until the server gives the OK. */
                Toast.makeText(
                        getApplicationContext(),
                        username+" has confirmed your Watch request!",
                        Toast.LENGTH_SHORT)
                        .show();

                return true;
            } else if (confirmed=="nook") {
                 /* Wait until the server gives the OK. */
                Toast.makeText(
                        getApplicationContext(),
                        username+" has declined your Watch request.",
                        Toast.LENGTH_SHORT)
                        .show();

                return false;
            } else if (confirmed=="timeout") {
                 /* Wait until the server gives the OK. */
                Toast.makeText(
                        getApplicationContext(),
                        "Server timeout.",
                        Toast.LENGTH_SHORT)
                        .show();

                return false;
            }
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Owl not found.",
                    Toast.LENGTH_SHORT)
                    .show();

            return false;
        }

        return false;
    }

}
