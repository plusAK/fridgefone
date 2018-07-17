package codepath.kaughlinpractice.fridgefone.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Recipe {

    private String name;
    private int id;
    private String image;
    private int readyInMinutes;
    private boolean vegetarian;
    private boolean vegan;
    private boolean glutenFree;
    private boolean dairyFree;
    private boolean veryHealthy;
    private boolean cheap;
    private boolean veryPopular;
    private int servings;
    private JSONObject ingredients;

    public static Recipe fromJSON(JSONObject jsonObject) throws JSONException {
        Recipe recipe = new Recipe();

        recipe.name = jsonObject.getString("title");
        // recipe.name = "FOOD NAME";
        recipe.id = jsonObject.getInt("id");
        recipe.image = jsonObject.getString("image");
        recipe.readyInMinutes = jsonObject.getInt("readyInMinutes");
        recipe.vegetarian = jsonObject.getBoolean("vegetarian");
        recipe.vegan = jsonObject.getBoolean("vegan");
        recipe.glutenFree = jsonObject.getBoolean("glutenFree");
        recipe.dairyFree = jsonObject.getBoolean("dairyFree");
        recipe.veryHealthy = jsonObject.getBoolean("veryHealthy");
        recipe.cheap = jsonObject.getBoolean("cheap");
        recipe.veryPopular = jsonObject.getBoolean("veryPopular");
        recipe.servings = jsonObject.getInt("servings");
        recipe.ingredients = jsonObject.getJSONObject("extendedIngredients");

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

    public JSONObject getIngredients() {
        return ingredients;
    }
}
