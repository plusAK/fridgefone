package codepath.kaughlinpractice.fridgefone.model;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

@ParseClassName("ShoppingList")
public class ShoppingItem extends ParseObject {
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE_URL = "imageURL";
    private static final String ingredient_base_URL = "https://spoonacular.com/cdn/ingredients_100x100/";

    public ShoppingItem() { }

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

    public static class Query extends ParseQuery<ShoppingItem> {
        public Query() {
            super(ShoppingItem.class);
        }

        public Query getShoppingListItems() {
            return this;
        }
    }

    public static ShoppingItem fromJSON(JSONObject jsonObject) throws JSONException {

        final ShoppingItem shopitem = new ShoppingItem();
        shopitem.setName(jsonObject.getString("name"));
        String imageName = jsonObject.getString("image");
        String url = ingredient_base_URL + imageName;
        shopitem.setImageURL(url);
        shopitem.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                } else {
                    Log.d("ShoppingItem", "Error");
                    e.printStackTrace();
                }
            }
        });
        return shopitem;
    }

}
