package com.mayken.sheldon.recipes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;


public class RecipeList extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        String[] testRecipes = {"Blue Cheese Porkchops", "Mushchops",
                "Lemon Chicken Soup", "Chili", "Tuna Salad"};

        ListView listView = (ListView)findViewById(R.id.recipeList);
        listView.setAdapter(new ArrayAdapter<>(this,
                R.layout.recipe_list_item, R.id.recipeName, testRecipes));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_new) {
            openEditActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openEditActivity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText input = new EditText(this);
        input.setHint("New Recipe Name");
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(RecipeList.this, EditActivity.class);
                intent.putExtra(EditActivity.RECIPE_NAME_KEY, input.getText().toString());
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
