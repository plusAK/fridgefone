package codepath.kaughlinpractice.fridgefone.model;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import codepath.kaughlinpractice.fridgefone.FridgeClient;
import codepath.kaughlinpractice.fridgefone.RecipeAdapter;
import codepath.kaughlinpractice.fridgefone.Singleton;
import cz.msebera.android.httpclient.Header;

@ParseClassName("Recipe")
public class Recipe extends ParseObject{

    public final static String[] recipe_traits = {"vegetarian", "vegan", "glutenFree", "dairyFree", "veryHealthy", "veryPopular", "cheap"};

    private String name;
    private int id;
    private String image;
    private HashSet<String> ingredients;
    public HashMap<String, Boolean> recipe_dict;

    static FridgeClient mClient;
    static ArrayList<Recipe> recipes;

    private static final String KEY_NAME = "name";
    private static final String KEY_ID = "id";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_RESPONSE = "response";
    private static final String KEY_INFORMATION = "recipeInformation";
    private static final String KEY_INSTRUCTION = "instructions";
    private static final int QUERY_LIMIT = 1000;

    private HashSet<Integer> serverRecipeIds;

    public Recipe() { }

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name) {
        this.name = name;
        put(KEY_NAME, name);
    }

    public int getId() {
        return (int) getNumber(KEY_ID);
    }

    public void setId(int id) {
        this.id = id;
        put(KEY_ID, id);
    }

    public String getImage() {
        return getString(KEY_IMAGE);
    }

    public void setImage(String image) {
        this.image = image;
        put(KEY_IMAGE, image);
    }

    public String getRecipeInformation() {
        return getString(KEY_INFORMATION);
    }

    public void setRecipeInformation(String recipeInformation) {
        put(KEY_INFORMATION, recipeInformation);
    }

    public String getInstructions() {
        return getString(KEY_INSTRUCTION);
    }

    public void setInstructions(String recipeInstruction) {
        put(KEY_INSTRUCTION, recipeInstruction);
    }


    public void setResponse(String response) {
        put(KEY_RESPONSE, response);
    }

    public static class Query extends ParseQuery<Recipe> {
        public Query() {
            super(Recipe.class);
        }
    }

    public static Recipe fromJSON(final JSONObject jsonObject, final Context context, final Bundle args, final RecipeAdapter recipeAdapter) throws JSONException {

        // initialize the client
        mClient = new FridgeClient(context);

        final Recipe recipe = new Recipe();

        recipe.setName(jsonObject.getString("title"));
        recipe.setImage(jsonObject.getString("image"));
        final int id = jsonObject.getInt("id");
        recipe.setId(id);
        recipe.setResponse(jsonObject.toString());

        recipes = new ArrayList<>();
        recipe.recipe_dict = new HashMap<>();
        recipe.ingredients = new HashSet<>();

        Recipe serverRecipe = findRecipeInServer(id);
        // if the serverRecipeInfo is null, then we have not encountered this recipe before

        // delete duplicates of recipes
        // recipe.deleteDuplicates();

        if (serverRecipe == null || serverRecipe.getRecipeInformation() == null) {
            // execute a GET request expecting a JSON object response

            mClient.getInformation(id, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    String r = response.toString();
                    recipe.setRecipeInformation(r);
                    recipe.parseResponse(response);
                    // if recipe attributes fit the user's desires (ex. vegan), then add to adapter
                    if (recipe.isValid()) {
                        recipeAdapter.add(recipe);
                    }

                    recipe.saveToServer();
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("Recipe", "Error: " + throwable);
                }
            });
        } else {
            try {
                JSONObject response = new JSONObject(serverRecipe.getRecipeInformation());
                // recipe.setRecipeInformation(stringResponse);
                recipe.setName(serverRecipe.getName());
                recipe.setId(serverRecipe.getId());
                recipe.setImage(serverRecipe.getImage());
                recipe.parseResponse(response);

                if (recipe.isValid()) {
                    recipeAdapter.add(recipe);
                }
            } catch (JSONException err) {
                err.printStackTrace();
            }
        }
        return recipe;
    }

    public static Recipe findRecipeInServer(int currId) {
        Recipe.Query recipeQuery = new Recipe.Query();
        recipeQuery.setLimit(QUERY_LIMIT);
        try {
            recipeQuery.whereEqualTo("id", currId);
            List<Recipe> recipesWithSameId = recipeQuery.find();
            // if this is of size 0, then we have no encountered this recipe before
            if (recipesWithSameId.size() != 0) {
                Recipe serverRecipe = recipesWithSameId.get(0);
                return serverRecipe;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void parseResponse(JSONObject response) {
        // save attributes of recipe, like if vegan or chep
        populateDictionary(response);
        // iterate through ingredients in the response and save to ArrayList
        saveIngredients(response);
    }

    private void populateDictionary(JSONObject response) {
        for (int i = 0; i < recipe_traits.length; i++) {
            makeDict(recipe_traits[i], response);
        }
    }

    private void makeDict(String trait, JSONObject response) {
        try {
            recipe_dict.put(trait, response.getBoolean(trait));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveToServer() {
        saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                } else {
                    Log.d("Recipe", "Error");
                    e.printStackTrace();
                }
            }
        });
    }

    public HashSet<String> getIngredients() {
        return ingredients;
    }

    public boolean isValid() {
        for (String trait: recipe_traits) {
            if (Singleton.getSingletonInstance().getmUserDict().get(trait) && !this.recipe_dict.get(trait)) {
                return false;
            }
        }
        return true;
    }

    private void saveIngredients(JSONObject response) {
        try {
            JSONArray ingredientsJSON = response.getJSONArray("extendedIngredients");
            for (int i = 0; i < ingredientsJSON.length(); i += 1) {
                String ingredient = ingredientsJSON.getJSONObject(i).getString("name");
                if (!ingredients.contains(ingredient)) {
                    ingredients.add(ingredient);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static String getRandomRecipeFromServer (List<Recipe> serverRecipes) {
        int randomPosition = (int) Math.random() * serverRecipes.size();
        Recipe randomRecipe = serverRecipes.get(randomPosition);
        return randomRecipe.getRecipeInformation();
    }

    private void deleteDuplicates() {
        serverRecipeIds = new HashSet<>();
        Recipe.Query recipeQuery = new Recipe.Query();
        recipeQuery.setLimit(QUERY_LIMIT);
        recipeQuery.findInBackground(new FindCallback<Recipe>() {
            @Override
            public void done(List<Recipe> objects, ParseException e) {
                for (Recipe serverRecipe: objects) {
                    int serverId = serverRecipe.getId();
                    if (serverRecipeIds.contains(serverId)) {
                        serverRecipe.deleteInBackground();
                    } else {
                        serverRecipeIds.add(serverId);
                    }
                }
            }
        });
    }
}
