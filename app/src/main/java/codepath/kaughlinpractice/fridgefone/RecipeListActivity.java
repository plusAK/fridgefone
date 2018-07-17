package codepath.kaughlinpractice.fridgefone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import codepath.kaughlinpractice.fridgefone.model.Recipe;

public class RecipeListActivity extends AppCompatActivity {

    RecipeAdapter recipeAdapter;
    ArrayList<Recipe> recipes;
    RecyclerView rvRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        // find RecyclerView
        rvRecipes = (RecyclerView) findViewById(R.id.rvRecipes);
        // init the array list (data source)
        recipes = new ArrayList<>();
        recipes.add(Recipe.fromString("Apple pie"));
        recipes.add(Recipe.fromString("Juice"));
        // construct the adapter from this data source
        recipeAdapter = new RecipeAdapter(recipes);

        // RecyclerView setup (layout manager, user adapter)
        rvRecipes.setLayoutManager(new LinearLayoutManager(this));
        // set the adapter
        rvRecipes.setAdapter(recipeAdapter);
    }

    // TODO CHANGE MANIFEST TO LEAD TO FRIDGE

}
