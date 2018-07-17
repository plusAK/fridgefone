package codepath.kaughlinpractice.fridgefone.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Recipe {

    private String name;

    public static Recipe fromJSON(JSONObject jsonObject) throws JSONException {
        Recipe recipe = new Recipe();

        // recipe.name = jsonObject.getString("title");
        recipe.name = "FOOD NAME";

        return recipe;
    }

    // this is for testing purposes
    public static Recipe fromJSON(String name) {
        Recipe recipe = new Recipe();

        // recipe.name = jsonObject.getString("title");
        recipe.name = name;

        return recipe;
    }

    public String getName() {
        return name;
    }
}
