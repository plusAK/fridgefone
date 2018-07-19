package codepath.kaughlinpractice.fridgefone.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Recipe {

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
    // JSONObject ingredients;
    // TODO -- figure out how to have ingredients without erroring

    public static Recipe fromJSON(JSONObject jsonObject) throws JSONException {
        Recipe recipe = new Recipe();
        recipe.name = jsonObject.getString("title");
        recipe.id = jsonObject.getInt("id");
        recipe.image = jsonObject.getString("image");
        Log.d("Recipe", "Have access to basic Recipe Info");

        // GET Recipe from Ingredients API only gives name, id, image
        if (jsonObject.has("readyInMinutes")) {
            Log.d("Recipe", "Have access to additional Recipe Info");
            // GET Recipe Information gives additional information about recipe
            recipe.readyInMinutes = jsonObject.getInt("readyInMinutes");
            recipe.vegetarian = jsonObject.getBoolean("vegetarian");
            recipe.vegan = jsonObject.getBoolean("vegan");
            recipe.glutenFree = jsonObject.getBoolean("glutenFree");
            recipe.dairyFree = jsonObject.getBoolean("dairyFree");
            recipe.veryHealthy = jsonObject.getBoolean("veryHealthy");
            recipe.cheap = jsonObject.getBoolean("cheap");
            recipe.veryPopular = jsonObject.getBoolean("veryPopular");
            recipe.servings = jsonObject.getInt("servings");
            // recipe.ingredients = jsonObject.getJSONObject("extendedIngredients");
        }
        return recipe;
    }

    // this is for testing purposes
    public static Recipe fromString(String name) {
        Recipe recipe = new Recipe();
        recipe.name = name;
        return recipe;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public boolean isVegan() {
        return vegan;
    }

    public boolean isGlutenFree() {
        return glutenFree;
    }

    public boolean isDairyFree() {
        return dairyFree;
    }

    public boolean isVeryHealthy() {
        return veryHealthy;
    }

    public boolean isCheap() {
        return cheap;
    }

    public boolean isVeryPopular() {
        return veryPopular;
    }

    public int getServings() {
        return servings;
    }

    /**
    public JSONObject getIngredients() {
        return ingredients;
    }
     */
}
