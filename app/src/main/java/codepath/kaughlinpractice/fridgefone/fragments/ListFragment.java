package codepath.kaughlinpractice.fridgefone.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import codepath.kaughlinpractice.fridgefone.R;
import codepath.kaughlinpractice.fridgefone.RecipeAdapter;
import codepath.kaughlinpractice.fridgefone.model.Recipe;

public class ListFragment extends Fragment {

    RecipeAdapter recipeAdapter;
    ArrayList<Recipe> recipes;
    RecyclerView rvRecipes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // find RecyclerView
        rvRecipes = (RecyclerView) view.findViewById(R.id.rvRecipes);
        // init the array list (data source)
        recipes = new ArrayList<>();

        //get bundle contents
        Bundle args = getArguments();
        String responseForBundle = args.getString("responseForBundle");



        JSONArray response = null;
        try {
            response = new JSONArray(responseForBundle);
        } catch (JSONException e) {
            Log.d("List Fragment", "Not api_call error: " + e.getMessage());
        }
        try {
            for (int i = 0; i < response.length(); i += 1) {
                Recipe recipe = Recipe.fromJSON(response.getJSONObject(i), getActivity());
                Log.d("List Fragment", recipe.getName());
                recipes.add(recipe);
            }
        } catch (JSONException e) {
            Log.d("List Fragment", e.getMessage());
        }



        //recipes.add(Recipe.fromString("Apple pie"));
        //recipes.add(Recipe.fromString("Juice"));

        // construct the adapter from this data source
        recipeAdapter = new RecipeAdapter(recipes);

        // RecyclerView setup (layout manager, user adapter)
        rvRecipes.setLayoutManager(new LinearLayoutManager(getActivity()));
        // set the adapter
        rvRecipes.setAdapter(recipeAdapter);



    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
        recipeAdapter.notifyDataSetChanged();
    }
}
