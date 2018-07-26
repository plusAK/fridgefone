package codepath.kaughlinpractice.fridgefone;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import codepath.kaughlinpractice.fridgefone.fragments.DeleteItemFragment;
import codepath.kaughlinpractice.fridgefone.fragments.DetailsFragment;
import codepath.kaughlinpractice.fridgefone.fragments.FridgeFragment;
import codepath.kaughlinpractice.fridgefone.fragments.ListFragment;
import codepath.kaughlinpractice.fridgefone.model.Item;
import codepath.kaughlinpractice.fridgefone.model.Recipe;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public boolean use_api = false;

    // the base URL for the API
    public final static String API_BASE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com";
    // the parameter name for the API key
    public final static String API_KEY_PARAM = "X-Mashape-Key";
    public final static String KEY_ACCEPT_PARAM = "Accept";
    public String fridgeItems;
    public String mSelectedItemsString;
    public boolean mAllSelected;
    public boolean mNoneSelected;

    public ItemAdapter mItemAdapter;
    public ArrayList<Item> mItemsList;

    // instance field
    AsyncHttpClient mClient;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Context mContext;
    //public FridgeFragment mFridgeFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open,R.string.close);

        // initialize the client
        mClient = new AsyncHttpClient();

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView=(NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null) { // allows to save the fragment you are in when the screen rotates
            goToMyFridge();
            navigationView.setCheckedItem(R.id.nav_Fridge);
        }
    }

    // allows for you to click the menu button and pull out the navigation drawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openDetails(Recipe recipe) {
        Fragment detailsFrag = new DetailsFragment();

        // bundle communication between activity and fragment
        Bundle args = new Bundle();
        args.putString("name", recipe.getName());
        args.putInt("id", recipe.getId());
        args.putString("image", recipe.getImage());

        detailsFrag.setArguments(args);
        fragmentTransition(detailsFrag);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if(id == R.id.nav_Fridge){
            Toast.makeText(this, "Welcome Home", Toast.LENGTH_LONG).show();
            goToMyFridge(); // similar to Intent, going through Activity to get to new fragment
        }
        else if(id == R.id.nav_Favorites){
            Toast.makeText(this, "Welcome to Favorites", Toast.LENGTH_LONG).show();
        }
        else if(id == R.id.nav_GroceryStores){
            Toast.makeText(this, "Welcome to Grocery Stores", Toast.LENGTH_LONG).show();
        }
        else if(id == R.id.nav_ShoppingList){
            Toast.makeText(this, "Welcome to Shopping List", Toast.LENGTH_LONG).show();
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    public void goToMyFridge() {
        Fragment fridgeFrag = new FridgeFragment();
        fragmentTransition(fridgeFrag);
    }

    public void getItem(String foodItem) {

        if (use_api) {

            String url = API_BASE_URL + "/food/ingredients/autocomplete";
            RequestParams params = new RequestParams();
            mClient.addHeader(API_KEY_PARAM, getString(R.string.api_key));
            mClient.addHeader(KEY_ACCEPT_PARAM, "application/json");
            params.put("query", foodItem);

            // execute a GET request expecting a JSON object response
            mClient.get(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    try {
                        //String r = response.toString();
                        //Log.d("MainActivity", "Response: " + r);
                        Item item = Item.fromJSON(response.getJSONObject(0));
                        Log.d("MainActivity", "Item: " + item.getName());
                    } catch (JSONException e) {
                        Log.d("MainActivity", e.getMessage());
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    Log.d("MainActivity", "Error: " + throwable);
                }
            });
        } else {
                String stringResponse = "[\n" +
            "  {\n" +
            "    \"name\": \"apple\",\n" +
            "    \"image\": \"apple.jpg\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"apples\",\n" +
            "    \"image\": \"apple.jpg\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"applesauce\",\n" +
            "    \"image\": \"applesauce.jpg\"\n" +
            "  }\n" +
             "]";
            Log.d("MainActivity", "Mock API Works");
            JSONArray response = null;
            try {
                // Creating items to send to parse server
                response = new JSONArray(stringResponse);
                Item item = Item.fromJSON(response.getJSONObject(0));
                Log.d("MainActivity", "Item: " + item.getName());
            } catch (JSONException e) {
                Log.d("MainActivity", "Error after API call: " + e.getMessage());
            }
        }
        goToMyFridge();
    }

    public void askToDeleteItem(Item item) {
        // there should be a pop up asking whether the user wants to delete THIS item
        Bundle args = new Bundle();
        args.putParcelable("Item", item);

        // OnClick listener here, so you have access to item
        DeleteItemFragment deleteItemFragment = new DeleteItemFragment();
        deleteItemFragment.setArguments(args);// connects bundle to fragment
        deleteItemFragment.show(getSupportFragmentManager(), "DeleteItemFragment");
        Log.d("ItemAdapter", String.format("Deleting this item from the fridge: " + item.getName()));
    }

    public void deleteItemFromFridge(Item item) {
        item.deleteInBackground();
        mItemsList.remove(item);
        mItemAdapter.notifyDataSetChanged();
        //mFridgeFragment.loadItems();
        Toast.makeText(this, "Deleted: " + item.getName(), Toast.LENGTH_LONG).show();
    }


    // get the list of currently playing movies from the API
    public void generateRecipes(final HashMap<String, Boolean> user_dict, String currentFilters) {
        // create the url
        if (use_api) {
            Log.d("MainActivity", "In API zone");
            String url = API_BASE_URL + "/recipes/findByIngredients";
            // set the request parameters
            RequestParams params = new RequestParams();
            mClient.addHeader(API_KEY_PARAM, getString(R.string.api_key));
            mClient.addHeader(KEY_ACCEPT_PARAM, "application/json");

            if (mAllSelected == false && mNoneSelected == false){
                Log.d("MainActivity", " Other Selected Fridge Items String: " + mSelectedItemsString);
                params.put("ingredients",mSelectedItemsString);
            }
            else {
                Log.d("MainActivity", "In All Selected Fridge Items: " + fridgeItems);
                params.put("ingredients", fridgeItems);
            }

            params.put("number", 5);
            // other parameters we could use later
            // params.put("fillIngredients", false);
            // params.put("limitLicense", false);
            // params.put("ranking", 1);

            // execute a GET request expecting a JSON object response
            mClient.get(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d("MainActivity", "Before response to string");
                    String responseForBundle = response.toString();
                    Log.d("MainActivity", "ResponseForBundle: " + responseForBundle);

                    Fragment listFrag = new ListFragment();

                    //bundles recipe arguments
                    Bundle args = new Bundle();
                    if (user_dict != null) {
                        for (String trait : Recipe.recipe_traits) {
                            args.putBoolean(trait, user_dict.get(trait));
                        }
                    }
                    args.putString("responseForBundle", responseForBundle);
                    listFrag.setArguments(args);

                    fragmentTransition(listFrag);
                }
            });
        }
        else {
            String responseForBundle =
                    "[{\"id\":556470,\"title\":\"Mango Smoothies\",\"image\":\"https:\\/\\/spoonacular.com\\/recipeImages\\/161181-312x231.jpg\",\"imageType\":\"jpg\",\"usedIngredientCount\":3,\"missedIngredientCount\":0,\"likes\":243}," +
                            "{\"id\":556470,\"title\":\"Mango Smoothies\",\"image\":\"https:\\/\\/spoonacular.com\\/recipeImages\\/161181-312x231.jpg\",\"imageType\":\"jpg\",\"usedIngredientCount\":3,\"missedIngredientCount\":0,\"likes\":243}, " +
                            "{\"id\":556470,\"title\":\"Mango Smoothies\",\"image\":\"https:\\/\\/spoonacular.com\\/recipeImages\\/161181-312x231.jpg\",\"imageType\":\"jpg\",\"usedIngredientCount\":3,\"missedIngredientCount\":0,\"likes\":243}, " +
                            "{\"id\":556470,\"title\":\"Mango Smoothies\",\"image\":\"https:\\/\\/spoonacular.com\\/recipeImages\\/161181-312x231.jpg\",\"imageType\":\"jpg\",\"usedIngredientCount\":3,\"missedIngredientCount\":0,\"likes\":243}]";

            Fragment listFrag = new ListFragment();

            //bundles recipe arguments
            Bundle args = new Bundle();
            if (user_dict != null) {
                for (String trait : Recipe.recipe_traits) {
                    args.putBoolean(trait, user_dict.get(trait));
                }
            }
                args.putString("currentFilters", currentFilters);
            args.putString("responseForBundle", responseForBundle);
            listFrag.setArguments(args);

            fragmentTransition(listFrag);
        }

    }


    public void fragmentTransition(Fragment nextFrag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.my_fragment, nextFrag).commit();
    }


    public void setFridgeItems(String fridge_items, String selectedItemsString, Boolean allSelected, Boolean noneSelected) {
        fridgeItems = fridge_items;
        mSelectedItemsString = selectedItemsString;
        mAllSelected = allSelected;
        mNoneSelected = noneSelected;
    }

    public void setItemsAccess(ItemAdapter setter, ArrayList<Item> itemArrayList) {
        mItemAdapter = setter;
        mItemsList = itemArrayList;
    }
}
