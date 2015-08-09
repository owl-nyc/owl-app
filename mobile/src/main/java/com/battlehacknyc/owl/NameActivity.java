package com.battlehacknyc.owl;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class NameActivity extends ActionBarActivity {
    public final static String USERNAME = "com.battlehacknyc.owl.USERNAME";
    private Toast nonameToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        // Warning Message as Toast
        Context context = getApplicationContext();
        CharSequence text = "Please enter a name!";
        int duration = Toast.LENGTH_SHORT;

        nonameToast = Toast.makeText(context, text, duration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_name, menu);
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


    public void startMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        EditText activity_name_name = (EditText) findViewById(R.id.activity_name_name);
        String username = activity_name_name.getText().toString().trim();

        if (username.length() == 0) {
            activity_name_name.setText("");

            nonameToast.show();
            return;
        }

        intent.putExtra(USERNAME, username);

        startActivity(intent);
    }

}
