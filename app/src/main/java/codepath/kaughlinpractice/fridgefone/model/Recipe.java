package codepath.kaughlinpractice.fridgefone.model;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

import codepath.kaughlinpractice.fridgefone.R;
import codepath.kaughlinpractice.fridgefone.RecipeAdapter;
import cz.msebera.android.httpclient.Header;

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

    static RecipeAdapter adapter;
    static AsyncHttpClient client;
    static ArrayList<Recipe> recipes;
    static boolean use_api = false;

    // the base URL for the API
    public final static String API_BASE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com";
    // the parameter name for the API key
    public final static String API_KEY_PARAM = "X-Mashape-Key";
    public final static String KEY_ACCEPT_PARAM = "Accept";

    // JSONObject ingredients;
    // TODO -- figure out how to have ingredients without erroring

    public static Recipe fromJSON(JSONObject jsonObject, Context context) throws JSONException {


        recipes = new ArrayList<>();
        // initialize the client
        client = new AsyncHttpClient();
        adapter = new RecipeAdapter(recipes);

        Recipe recipe = new Recipe();
        recipe.name = jsonObject.getString("title");
        recipe.id = jsonObject.getInt("id");
        recipe.image = jsonObject.getString("image");
        Log.d("Recipe", "Have access to basic Recipe Info");

        if (use_api) {
            String url = API_BASE_URL +"/recipes/" + recipe.id + "/information";
            // set the request parameters
            RequestParams params = new RequestParams();
           // String x = R.string.api_key;

            client.addHeader(API_KEY_PARAM, context.getString(R.string.api_key));
            client.addHeader(KEY_ACCEPT_PARAM, "application/json");

            // execute a GET request expecting a JSON object response
            client.get(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    String r = response.toString();
                    Log.d("Recipe", "JSONObject : " + r);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("Recipe", "JSON Object Error: " + throwable);
                }
            });
        }
        else {
            //String stringResponse = context.getString(R.string.api_fake_RecipeInfoData);
            String stringResponse = "{\n" +
                    "  \"vegetarian\": false,\n" +
                    "  \"vegan\": false,\n" +
                    "  \"glutenFree\": true,\n" +
                    "  \"dairyFree\": true,\n" +
                    "  \"veryHealthy\": false,\n" +
                    "  \"cheap\": false,\n" +
                    "  \"veryPopular\": false,\n" +
                    "  \"sustainable\": false,\n" +
                    "  \"weightWatcherSmartPoints\": 21,\n" +
                    "  \"gaps\": \"no\",\n" +
                    "  \"lowFodmap\": false,\n" +
                    "  \"ketogenic\": false,\n" +
                    "  \"whole30\": false,\n" +
                    "  \"servings\": 10,\n" +
                    "  \"sourceUrl\": \"http://www.epicurious.com/recipes/food/views/Char-Grilled-Beef-Tenderloin-with-Three-Herb-Chimichurri-235342\",\n" +
                    "  \"spoonacularSourceUrl\": \"https://spoonacular.com/char-grilled-beef-tenderloin-with-three-herb-chimichurri-156992\",\n" +
                    "  \"aggregateLikes\": 0,\n" +
                    "  \"creditText\": \"Epicurious\",\n" +
                    "  \"sourceName\": \"Epicurious\",\n" +
                    "  \"extendedIngredients\": [\n" +
                    "    {\n" +
                    "      \"id\": 1022009,\n" +
                    "      \"aisle\": \"Ethnic Foods\",\n" +
                    "      \"image\": \"https://spoonacular.com/cdn/ingredients_100x100/chili-powder.jpg\",\n" +
                    "      \"name\": \"ancho chile powder\",\n" +
                    "      \"amount\": 1.5,\n" +
                    "      \"unit\": \"teaspoons\",\n" +
                    "      \"unitShort\": \"t\",\n" +
                    "      \"unitLong\": \"teaspoons\",\n" +
                    "      \"originalString\": \"1 1/2 teaspoons chipotle chile powder or ancho chile powder\",\n" +
                    "      \"metaInformation\": []\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 13926,\n" +
                    "      \"aisle\": \"Meat\",\n" +
                    "      \"image\": \"https://spoonacular.com/cdn/ingredients_100x100/beef-tenderloin.jpg\",\n" +
                    "      \"name\": \"beef tenderloin\",\n" +
                    "      \"amount\": 3.5,\n" +
                    "      \"unit\": \"pound\",\n" +
                    "      \"unitShort\": \"lb\",\n" +
                    "      \"unitLong\": \"pounds\",\n" +
                    "      \"originalString\": \"1 3 1/2-pound beef tenderloin\",\n" +
                    "      \"metaInformation\": []\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 1002030,\n" +
                    "      \"aisle\": \"Spices and Seasonings\",\n" +
                    "      \"image\": \"https://spoonacular.com/cdn/ingredients_100x100/pepper.jpg\",\n" +
                    "      \"name\": \"black pepper\",\n" +
                    "      \"amount\": 0.5,\n" +
                    "      \"unit\": \"teaspoon\",\n" +
                    "      \"unitShort\": \"t\",\n" +
                    "      \"unitLong\": \"teaspoons\",\n" +
                    "      \"originalString\": \"1/2 teaspoon freshly ground black pepper\",\n" +
                    "      \"metaInformation\": [\n" +
                    "        \"black\",\n" +
                    "        \"freshly ground\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 1082047,\n" +
                    "      \"aisle\": \"Spices and Seasonings\",\n" +
                    "      \"image\": \"https://spoonacular.com/cdn/ingredients_100x100/salt.jpg\",\n" +
                    "      \"name\": \"coarse kosher salt\",\n" +
                    "      \"amount\": 1,\n" +
                    "      \"unit\": \"tablespoon\",\n" +
                    "      \"unitShort\": \"T\",\n" +
                    "      \"unitLong\": \"tablespoon\",\n" +
                    "      \"originalString\": \"1 tablespoon coarse kosher salt\",\n" +
                    "      \"metaInformation\": []\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 10019334,\n" +
                    "      \"aisle\": \"Baking\",\n" +
                    "      \"image\": \"https://spoonacular.com/cdn/ingredients_100x100/brown-sugar-dark.jpg\",\n" +
                    "      \"name\": \"dark brown sugar\",\n" +
                    "      \"amount\": 2,\n" +
                    "      \"unit\": \"tablespoons\",\n" +
                    "      \"unitShort\": \"T\",\n" +
                    "      \"unitLong\": \"tablespoons\",\n" +
                    "      \"originalString\": \"2 tablespoons dark brown sugar\",\n" +
                    "      \"metaInformation\": [\n" +
                    "        \"dark\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 11165,\n" +
                    "      \"aisle\": \"Produce\",\n" +
                    "      \"image\": \"https://spoonacular.com/cdn/ingredients_100x100/cilantro.png\",\n" +
                    "      \"name\": \"fresh cilantro\",\n" +
                    "      \"amount\": 2,\n" +
                    "      \"unit\": \"cups\",\n" +
                    "      \"unitShort\": \"c\",\n" +
                    "      \"unitLong\": \"cups\",\n" +
                    "      \"originalString\": \"2 cups (packed) stemmed fresh cilantro\",\n" +
                    "      \"metaInformation\": [\n" +
                    "        \"fresh\",\n" +
                    "        \"packed\",\n" +
                    "        \"stemmed\",\n" +
                    "        \"()\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 2064,\n" +
                    "      \"aisle\": \"Produce\",\n" +
                    "      \"image\": \"https://spoonacular.com/cdn/ingredients_100x100/mint.jpg\",\n" +
                    "      \"name\": \"fresh mint\",\n" +
                    "      \"amount\": 1,\n" +
                    "      \"unit\": \"cup\",\n" +
                    "      \"unitShort\": \"c\",\n" +
                    "      \"unitLong\": \"cup\",\n" +
                    "      \"originalString\": \"1 cup (packed) stemmed fresh mint\",\n" +
                    "      \"metaInformation\": [\n" +
                    "        \"fresh\",\n" +
                    "        \"packed\",\n" +
                    "        \"stemmed\",\n" +
                    "        \"()\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 11297,\n" +
                    "      \"aisle\": \"Produce\",\n" +
                    "      \"image\": \"https://spoonacular.com/cdn/ingredients_100x100/parsley.jpg\",\n" +
                    "      \"name\": \"fresh parsley\",\n" +
                    "      \"amount\": 3,\n" +
                    "      \"unit\": \"cups\",\n" +
                    "      \"unitShort\": \"c\",\n" +
                    "      \"unitLong\": \"cups\",\n" +
                    "      \"originalString\": \"3 cups (packed) stemmed fresh parsley\",\n" +
                    "      \"metaInformation\": [\n" +
                    "        \"fresh\",\n" +
                    "        \"packed\",\n" +
                    "        \"stemmed\",\n" +
                    "        \"()\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 11215,\n" +
                    "      \"aisle\": \"Produce\",\n" +
                    "      \"image\": \"https://spoonacular.com/cdn/ingredients_100x100/garlic.jpg\",\n" +
                    "      \"name\": \"garlic cloves\",\n" +
                    "      \"amount\": 3,\n" +
                    "      \"unit\": \"\",\n" +
                    "      \"unitShort\": \"\",\n" +
                    "      \"unitLong\": \"\",\n" +
                    "      \"originalString\": \"3 garlic cloves, peeled\",\n" +
                    "      \"metaInformation\": [\n" +
                    "        \"peeled\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 1002030,\n" +
                    "      \"aisle\": \"Spices and Seasonings\",\n" +
                    "      \"image\": \"https://spoonacular.com/cdn/ingredients_100x100/pepper.jpg\",\n" +
                    "      \"name\": \"ground pepper\",\n" +
                    "      \"amount\": 1,\n" +
                    "      \"unit\": \"teaspoon\",\n" +
                    "      \"unitShort\": \"t\",\n" +
                    "      \"unitLong\": \"teaspoon\",\n" +
                    "      \"originalString\": \"1 teaspoon ground black pepper\",\n" +
                    "      \"metaInformation\": [\n" +
                    "        \"black\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 9152,\n" +
                    "      \"aisle\": \"Produce\",\n" +
                    "      \"image\": \"https://spoonacular.com/cdn/ingredients_100x100/lemon-juice.jpg\",\n" +
                    "      \"name\": \"lemon juice\",\n" +
                    "      \"amount\": 3,\n" +
                    "      \"unit\": \"tablespoons\",\n" +
                    "      \"unitShort\": \"T\",\n" +
                    "      \"unitLong\": \"tablespoons\",\n" +
                    "      \"originalString\": \"3 tablespoons fresh lemon juice\",\n" +
                    "      \"metaInformation\": [\n" +
                    "        \"fresh\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 4053,\n" +
                    "      \"aisle\": \"Oil, Vinegar, Salad Dressing\",\n" +
                    "      \"image\": \"https://spoonacular.com/cdn/ingredients_100x100/olive-oil.jpg\",\n" +
                    "      \"name\": \"olive oil\",\n" +
                    "      \"amount\": 0.75,\n" +
                    "      \"unit\": \"cup\",\n" +
                    "      \"unitShort\": \"c\",\n" +
                    "      \"unitLong\": \"cups\",\n" +
                    "      \"originalString\": \"3/4 cup olive oil\",\n" +
                    "      \"metaInformation\": []\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 4053,\n" +
                    "      \"aisle\": \"Oil, Vinegar, Salad Dressing\",\n" +
                    "      \"image\": \"https://spoonacular.com/cdn/ingredients_100x100/olive-oil.jpg\",\n" +
                    "      \"name\": \"olive oil\",\n" +
                    "      \"amount\": 2,\n" +
                    "      \"unit\": \"tablespoons\",\n" +
                    "      \"unitShort\": \"T\",\n" +
                    "      \"unitLong\": \"tablespoons\",\n" +
                    "      \"originalString\": \"2 tablespoons olive oil\",\n" +
                    "      \"metaInformation\": []\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 11821,\n" +
                    "      \"aisle\": \"Produce\",\n" +
                    "      \"image\": \"https://spoonacular.com/cdn/ingredients_100x100/red-bell-pepper.png\",\n" +
                    "      \"name\": \"red pepper\",\n" +
                    "      \"amount\": 0.5,\n" +
                    "      \"unit\": \"teaspoon\",\n" +
                    "      \"unitShort\": \"t\",\n" +
                    "      \"unitLong\": \"teaspoons\",\n" +
                    "      \"originalString\": \"1/2 teaspoon dried crushed red pepper\",\n" +
                    "      \"metaInformation\": [\n" +
                    "        \"dried\",\n" +
                    "        \"red\",\n" +
                    "        \"crushed\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 1022068,\n" +
                    "      \"aisle\": \"Oil, Vinegar, Salad Dressing\",\n" +
                    "      \"image\": \"https://spoonacular.com/cdn/ingredients_100x100/red-wine-vinegar.jpg\",\n" +
                    "      \"name\": \"red wine vinegar\",\n" +
                    "      \"amount\": 3,\n" +
                    "      \"unit\": \"tablespoons\",\n" +
                    "      \"unitShort\": \"T\",\n" +
                    "      \"unitLong\": \"tablespoons\",\n" +
                    "      \"originalString\": \"3 tablespoons Sherry wine vinegar or red wine vinegar\",\n" +
                    "      \"metaInformation\": [\n" +
                    "        \"red\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 1012047,\n" +
                    "      \"aisle\": \"Spices and Seasonings\",\n" +
                    "      \"image\": \"https://spoonacular.com/cdn/ingredients_100x100/salt.jpg\",\n" +
                    "      \"name\": \"sea salt\",\n" +
                    "      \"amount\": 1,\n" +
                    "      \"unit\": \"teaspoon\",\n" +
                    "      \"unitShort\": \"t\",\n" +
                    "      \"unitLong\": \"teaspoon\",\n" +
                    "      \"originalString\": \"1 teaspoon fine sea salt\",\n" +
                    "      \"metaInformation\": [\n" +
                    "        \"fine\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 11677,\n" +
                    "      \"aisle\": \"Produce\",\n" +
                    "      \"image\": \"https://spoonacular.com/cdn/ingredients_100x100/shallots.jpg\",\n" +
                    "      \"name\": \"shallots\",\n" +
                    "      \"amount\": 2,\n" +
                    "      \"unit\": \"\",\n" +
                    "      \"unitShort\": \"\",\n" +
                    "      \"unitLong\": \"\",\n" +
                    "      \"originalString\": \"2 medium shallots, peeled, quartered\",\n" +
                    "      \"metaInformation\": [\n" +
                    "        \"medium\",\n" +
                    "        \"peeled\",\n" +
                    "        \"quartered\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 1002028,\n" +
                    "      \"aisle\": \"Spices and Seasonings\",\n" +
                    "      \"image\": \"https://spoonacular.com/cdn/ingredients_100x100/paprika.jpg\",\n" +
                    "      \"name\": \"sweet paprika\",\n" +
                    "      \"amount\": 1,\n" +
                    "      \"unit\": \"tablespoon\",\n" +
                    "      \"unitShort\": \"T\",\n" +
                    "      \"unitLong\": \"tablespoon\",\n" +
                    "      \"originalString\": \"1 tablespoon sweet smoked paprika*\",\n" +
                    "      \"metaInformation\": [\n" +
                    "        \"smoked\"\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"id\": 156992,\n" +
                    "  \"title\": \"Char-Grilled Beef Tenderloin with Three-Herb Chimichurri\",\n" +
                    "  \"readyInMinutes\": 45,\n" +
                    "  \"image\": \"https://spoonacular.com/recipeImages/char-grilled-beef-tenderloin-with-three-herb-chimichurri-156992.jpg\",\n" +
                    "  \"imageType\": \"jpg\",\n" +
                    "  \"instructions\": \"PreparationFor spice rub:                                        Combine all ingredients in small bowl.                                                                            Do ahead: Can be made 2 days ahead. Store airtight at room temperature.                                    For chimichurri sauce:                                        Combine first 8 ingredients in blender; blend until almost smooth. Add 1/4 of parsley, 1/4 of cilantro, and 1/4 of mint; blend until incorporated. Add remaining herbs in 3 more additions, pureeing until almost smooth after each addition.                                                                            Do ahead: Can be made 3 hours ahead. Cover; chill.                                    For beef tenderloin:                                        Let beef stand at room temperature 1 hour.                                                                            Prepare barbecue (high heat). Pat beef dry with paper towels; brush with oil. Sprinkle all over with spice rub, using all of mixture (coating will be thick). Place beef on grill; sear 2 minutes on each side. Reduce heat to medium-high. Grill uncovered until instant-read thermometer inserted into thickest part of beef registers 130F for medium-rare, moving beef to cooler part of grill as needed to prevent burning, and turning occasionally, about 40 minutes. Transfer to platter; cover loosely with foil and let rest 15 minutes. Thinly slice beef crosswise. Serve with chimichurri sauce.                                                                            *Available at specialty foods stores and from tienda.com.\"\n" +
                    "}";


            JSONObject response = null;
            try {

                response = new JSONObject(stringResponse);

                Log.d("Recipe", "Does vegan exist: " + response.has("vegan"));

                recipe.readyInMinutes = response.getInt("readyInMinutes");
                recipe.vegetarian = response.getBoolean("vegetarian");
                recipe.vegan = response.getBoolean("vegan");
                recipe.glutenFree = response.getBoolean("glutenFree");
                recipe.dairyFree = response.getBoolean("dairyFree");
                recipe.veryHealthy = response.getBoolean("veryHealthy");
                recipe.cheap = response.getBoolean("cheap");
                recipe.veryPopular = response.getBoolean("veryPopular");
                recipe.servings = response.getInt("servings");
            } catch (JSONException e) {
                Log.d("MainActivity", "Not api_call error: " + e.getMessage());
            }
        }

        Log.d("Recipe", "Recipe readyInMinutes" + recipe.readyInMinutes);
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
