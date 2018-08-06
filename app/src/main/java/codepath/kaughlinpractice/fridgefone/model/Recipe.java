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
import codepath.kaughlinpractice.fridgefone.R;
import codepath.kaughlinpractice.fridgefone.RecipeAdapter;
import codepath.kaughlinpractice.fridgefone.Singleton;
import cz.msebera.android.httpclient.Header;

@ParseClassName("Recipe")
public class Recipe extends ParseObject{

    public final static String[] recipe_traits = {"vegetarian", "vegan", "glutenFree", "dairyFree", "veryHealthy", "veryPopular", "cheap"};

    String name;
    int id;
    String image;
    int readyInMinutes;
    boolean vegetarian;
    boolean vegan;
    boolean glutenFree;
    boolean dairyFree;
    boolean veryHealthy;
    boolean cheap;
    boolean veryPopular;
    int servings;
    private HashSet<String> ingredients;
    public HashMap<String, Boolean> recipe_dict;
    private Context mContext;

    static RecipeAdapter adapter;
    static FridgeClient mClient;
    static ArrayList<Recipe> recipes;

    private static final String KEY_NAME = "name";
    private static final String KEY_ID = "id";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_RESPONSE = "response";
    private static final String KEY_INFORMATION = "recipeInformation";

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

    public String getResponse() {
        return getString(KEY_RESPONSE);
    }

    public void setResponse(String response) {
        put(KEY_RESPONSE, response);
    }

    public static class Query extends ParseQuery<Recipe> {
        public Query() {
            super(Recipe.class);
        }
    }

    public static Recipe fromJSON(final JSONObject jsonObject, final Context context, final Bundle args, final ArrayList<Recipe> rec, final RecipeAdapter recipeAdapter) throws JSONException {

        // initialize the client
        mClient = new FridgeClient(context);
        adapter = new RecipeAdapter(recipes);

        final Recipe recipe = new Recipe();

        recipe.setName(jsonObject.getString("title"));
        final int id = jsonObject.getInt("id");
        recipe.setId(id);
        recipe.setImage(jsonObject.getString("image"));
        recipe.setResponse(jsonObject.toString());

        recipes = new ArrayList<>();
        recipe.recipe_dict = new HashMap<>();
        recipe.ingredients = new HashSet<>();
        recipe.mContext = context;

        final Recipe.Query recipeQuery = new Recipe.Query();
        recipeQuery.findInBackground(new FindCallback<Recipe>() {
            @Override
            public void done(List<Recipe> objects, ParseException e) {
                if (e == null) {
                    if (!haveRecipeInServer(objects, id)) {
                        if (FridgeClient.mUseRecipeInformationAPI) {

                            // execute a GET request expecting a JSON object response
                            mClient.getInformation(id, new JsonHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                            String r = response.toString();
                                            recipe.setRecipeInformation(r);
                                            recipe.parseResponse(response);

                                            // if recipe attributes fit the user's desires (ex. vegan), then add to adapter
                                            if (recipe.isValid(args)) {
                                                rec.add(recipe);
                                                //recipeAdapter.notifyItemInserted(rec.size() - 1);
                                            }

                                            recipe.saveToServer();
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                            Log.d("Recipe", "JSON Object Error: " + throwable);
                                        }
                                    });
                        }
                        else {
                            // To conserve API calls, just randomly select a recipe we have
                            String stringResponse = getRandomRecipeFromServer(objects);
                            try {
                                JSONObject response = new JSONObject(stringResponse);
                                recipe.setRecipeInformation(stringResponse);
                                recipe.parseResponse(response);

                                if (recipe.isValid(args)) {
                                    rec.add(recipe);
                                    recipeAdapter.notifyItemInserted(rec.size() - 1);
                                }

                                recipe.saveToServer();
                            } catch (JSONException err) {
                                err.printStackTrace();
                            }

                        }
                    } else {
                        String stringResponse = findRecipeInServer(objects, id);
                        try {
                            Log.d("Recipe", "We already have recipe id " + id);
                            JSONObject response = new JSONObject(stringResponse);
                            recipe.setRecipeInformation(stringResponse);
                            recipe.name = recipe.getName();
                            recipe.id = recipe.getId();
                            recipe.image = recipe.getImage();
                            recipe.parseResponse(response);

                            if (recipe.isValid(args)) {
                                rec.add(recipe);
                                recipeAdapter.notifyItemInserted(rec.size() - 1);
                            }
                        } catch (JSONException err) {
                            err.printStackTrace();
                        }
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
        return recipe;
    }

    private void parseResponse(JSONObject response) {
        // save attributes of recipe, like if vegan or chep
        populateDictionary(response);
        // iterate through ingredients in the response and save to ArrayList
        saveIngredients(response);

        try {
            readyInMinutes = response.getInt("readyInMinutes");
            servings = response.getInt("servings");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // this is for testing purposes
    public static Recipe fromString(String name) {
        Recipe recipe = new Recipe();
        recipe.name = name;
        return recipe;
    }

    private void populateDictionary(JSONObject response) {
        makeDict(mContext.getString(R.string.vegetarian), response);
        makeDict(mContext.getString(R.string.vegan), response);
        makeDict(mContext.getString(R.string.gluten_free), response);
        makeDict(mContext.getString(R.string.dairy_free), response);
        makeDict(mContext.getString(R.string.very_healthy), response);
        makeDict(mContext.getString(R.string.very_popular), response);
        makeDict(mContext.getString(R.string.cheap), response);
    }

    private void makeDict(String trait, JSONObject response) {
        try {
            recipe_dict.put(trait, response.getBoolean(trait));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveToServer() {
        saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("Recipe", "Recipe created successfully");
                } else {
                    Log.d("Recipe", "Recipe failure");
                    e.printStackTrace();
                }
            }
        });
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public boolean isVegetarian() {
        return recipe_dict.get("vegetarian");
    }

    public boolean isVegan() {
        return recipe_dict.get("vegan");
    }

    public boolean isGlutenFree() {
        return recipe_dict.get("gluten_free");
    }

    public boolean isDairyFree() {
        return recipe_dict.get("dairy_free");
    }

    public boolean isVeryHealthy() {
        return recipe_dict.get("very_healthy");
    }

    public boolean isCheap() {
        return recipe_dict.get("cheap");
    }

    public boolean isVeryPopular() {
        return recipe_dict.get("very_popular");
    }

    public boolean isFast() {
        if (getReadyInMinutes() < 30) {
            return true;
        } else {
            return false;
        }
    }

    public int getServings() {
        return servings;
    }

    public HashSet<String> getIngredients() {
        return ingredients;
    }

    public boolean isValid(Bundle args) {
        for (String trait: recipe_traits) {
            if (Singleton.getSingletonInstance().getmUserDict().get(trait) && !this.recipe_dict.get(trait)) {
                return false;
            }
        }
        return true;
    }

    private static boolean haveRecipeInServer(List<Recipe> serverRecipes, int currId) {
        for (Recipe recipe: serverRecipes) {
            if (recipe.getId() == currId) {
                return true;
            }
        }
        return false;
    }

    private static String findRecipeInServer(List<Recipe> serverRecipes, int currId) {
        for (Recipe recipe: serverRecipes) {
            if (recipe.getId() == currId) {
                return recipe.getRecipeInformation();
            }
        }
        return null;
    }

    private static String getRandomRecipeFromServer (List<Recipe> serverRecipes) {
        int randomPosition = (int) Math.random() * serverRecipes.size();
        Recipe randomRecipe = serverRecipes.get(randomPosition);
        return randomRecipe.getRecipeInformation();
    }

    private void saveIngredients(JSONObject response) {
        try {
            JSONArray ingredientsJSON = response.getJSONArray("extendedIngredients");
            for (int i = 0; i < ingredientsJSON.length(); i += 1) {
                String ingredient = ingredientsJSON.getJSONObject(i).getString("name");
                if (!ingredients.contains(ingredient)) {
                    ingredients.add(ingredient);
                    Log.d("Recipe", ingredient);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
