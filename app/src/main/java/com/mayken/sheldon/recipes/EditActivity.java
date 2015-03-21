package com.mayken.sheldon.recipes;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

public class EditActivity extends ActionBarActivity {

    public final static String RECIPE_NAME_KEY = "com.mayken.sheldon.recipes.newname";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        setTitle(intent.getStringExtra(RECIPE_NAME_KEY));

        TabHost tabs = (TabHost)findViewById(R.id.edit_tab_host);
        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec("ingredientsSpec");
        spec.setContent(R.id.tab_ingredients);
        spec.setIndicator("Ingredients");
        tabs.addTab(spec);

        spec = tabs.newTabSpec("stepsSpec");
        spec.setContent(R.id.tab_steps);
        spec.setIndicator("Steps");
        tabs.addTab(spec);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save || id == R.id.action_cancel) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
