package codepath.kaughlinpractice.fridgefone.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Item")
public class Item extends ParseObject {
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE ="image";
    private static final String KEY_ID = "item_id";

    public String getName() {
        return getString(KEY_NAME);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }
    public String getItemId() {
        return getString(KEY_ID);
    }

    public static class Query extends ParseQuery<Item> {
        public Query() {
            super(Item.class);
        }

        public Query getFridgeItems() {
            return this;
        }
    }
}
