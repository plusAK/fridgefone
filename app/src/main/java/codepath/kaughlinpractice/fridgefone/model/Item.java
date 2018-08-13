package codepath.kaughlinpractice.fridgefone.model;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import codepath.kaughlinpractice.fridgefone.AddItemAdapter;

@ParseClassName("Item")
public class Item extends ParseObject {
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE_URL = "imageURL";
    private static final String ingredient_base_URL = "https://spoonacular.com/cdn/ingredients_100x100/";

    public Item() { }

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public String getImageURL() {
        return getString(KEY_IMAGE_URL);
    }

    public void setImageURL(String image) {
        put(KEY_IMAGE_URL, image);
    }

    public static class Query extends ParseQuery<Item> {
        public Query() {
            super(Item.class);
        }

        public Query getFridgeItems() {
            return this;
        }
    }

    // For Adding Items
    public static Item fromJSON(JSONObject jsonObject) throws JSONException {
        final Item item = new Item();

        item.setName(jsonObject.getString("name"));
        String imageName = jsonObject.getString("image");
        String url = ingredient_base_URL + imageName;
        item.setImageURL(url);
        item.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                } else {
                    Log.d("Item", "Error");
                    e.printStackTrace();
                }
            }
        });
        return item;
    }

    // For AutoComplete
    public static Item autoFromJSON(JSONObject jsonObject, AddItemAdapter addItemAdapter) throws JSONException {
        final Item item = new Item();

        String capitalName = capitalizeString(jsonObject.getString("name"));
        item.setName(capitalName);

        String imageName = jsonObject.getString("image");
        String url = ingredient_base_URL + imageName;
        item.setImageURL(url);

        // Update the adapter
        addItemAdapter.add(item);
        addItemAdapter.notifyDataSetChanged();
        return item;
    }

    public static String capitalizeString(String recipeName) {
        String capitalName = "";
        String[] strArray = recipeName.split(" ");
        for (String s : strArray) {
            String word = s.substring(0, 1).toUpperCase() + s.substring(1);
             capitalName += word + " ";
        }
        // Take off the extra space
        capitalName = capitalName.substring(0,capitalName.length()-1);
        return capitalName;
    }

    @Override
    public String toString() {
        return getName();
    }
}
