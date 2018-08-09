package codepath.kaughlinpractice.fridgefone;

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

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import codepath.kaughlinpractice.fridgefone.fragments.DeleteItemFragment;
import codepath.kaughlinpractice.fridgefone.fragments.DetailsFragment;
import codepath.kaughlinpractice.fridgefone.fragments.FridgeFragment;
import codepath.kaughlinpractice.fridgefone.fragments.ListFragment;
import codepath.kaughlinpractice.fridgefone.fragments.ShoppingListFragment;
import codepath.kaughlinpractice.fridgefone.model.Item;
import codepath.kaughlinpractice.fridgefone.model.Recipe;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    private FridgeClient mClient;

    public Singleton mSingleInstance;

    public ItemAdapter mItemAdapter;
    public ArrayList<Item> mItemsList;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private android.support.v7.widget.Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.nav_actionbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        // initialize Singleton instance
        mSingleInstance = Singleton.getSingletonInstance();
        // initialize the client
        mClient = new FridgeClient(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView=(NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // allows to save the fragment you are in when the screen rotates
        if(savedInstanceState == null) {
            goToMyFridge();
            navigationView.setCheckedItem(R.id.nav_Fridge);
        }
    }


    // allows for you to click the menu button and pull out the navigation drawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // allows for click on the menu toggle
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
        ArrayList<String> ingredients = new ArrayList<>(recipe.getIngredients());
        args.putStringArrayList("ingredients", ingredients);

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
            goToShoppingList();
        }
        else if(id == R.id.nav_DailyRecipe){
            Toast.makeText(this, "Welcome to Recipe of the Day", Toast.LENGTH_LONG).show();
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    public void goToMyFridge() {
        mSingleInstance.getmSelectedNamesSet().clear();
        Fragment fridgeFrag = new FridgeFragment();
        fragmentTransition(fridgeFrag);
    }
    public void goToShoppingList() {
        Fragment shoppingFrag = new ShoppingListFragment();
        fragmentTransition(shoppingFrag);
    }



    public void getItem(String foodItem) {

        if (FridgeClient.mUseAutocompleteAPI) {

            // execute a GET request expecting a JSON object response
            mClient.getAutoComplete(foodItem, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    try {
                        Item item = Item.fromJSON(response.getJSONObject(0));
                        goToMyFridge();
                    } catch (JSONException e) {
                        Log.d("MainActivity", "Error: " + e.getMessage());
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
            JSONArray response = null;
            try {
                // Creating items to send to parse server
                response = new JSONArray(stringResponse);
                Item item = Item.fromJSON(response.getJSONObject(0));
                goToMyFridge();
            } catch (JSONException e) {
                Log.d("MainActivity", "Error: " + e.getMessage());
            }
        }
        //goToMyFridge();
    }

    public void askToDeleteItem(Item item) {
        // there should be a pop up asking whether the user wants to delete THIS item
        Bundle args = new Bundle();
        args.putParcelable("Item", item);

        // OnClick listener here, so you have access to item
        DeleteItemFragment deleteItemFragment = new DeleteItemFragment();
        deleteItemFragment.setArguments(args);// connects bundle to fragment
        deleteItemFragment.show(getSupportFragmentManager(), "DeleteItemFragment");
    }

    public void deleteItemFromFridge(Item item) {
        item.deleteInBackground();
        mItemsList.remove(item);
        //mItemAdapter.notifyItemRemoved(mItemAdapter.getItemCount());
        mItemAdapter.notifyDataSetChanged();
        //mFridgeFragment.loadItems();
        mSingleInstance.getmAllItemNamesSet().remove(item.getName());
        mSingleInstance.getmSelectedNamesSet().remove(item.getName());
        Toast.makeText(this, getString(R.string.delete_item_toast) + " " + item.getName(), Toast.LENGTH_LONG).show();
    }

    public void generateRecipes() {
        // create the url
        if (FridgeClient.mUseGenerateRecipeAPI) {

            // execute a GET request expecting a JSON object response
            mClient.getRecipeList(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                    String responseForBundle = response.toString();

                    Fragment listFrag = new ListFragment();

                    //bundles recipe arguments
                    Bundle args = new Bundle();
                    args.putString("responseForBundle", responseForBundle);
                    listFrag.setArguments(args);

                    fragmentTransition(listFrag);
                }
            });
        } else {
            String responseForBundle =
                    "[{\"id\":556470,\"title\":\"Veggie & Chicken Kebab\",\"image\":\"https:\\/\\/spoonacular.com\\/recipeImages\\/544976-312x231.jpg\",\"imageType\":\"jpg\",\"usedIngredientCount\":3,\"missedIngredientCount\":0,\"likes\":243}," +
                            "{\"id\":556470,\"title\":\"Curried Chicken Pitas\",\"image\":\"https:\\/\\/spoonacular.com\\/recipeImages\\/421176-312x231.jpg\",\"imageType\":\"jpg\",\"usedIngredientCount\":3,\"missedIngredientCount\":0,\"likes\":243}, " +
                            "{\"id\":556470,\"title\":\"Curry Chicken and Grape Salad\",\"image\":\"https:\\/\\/spoonacular.com\\/recipeImages\\/1010550-312x231.jpg\",\"imageType\":\"jpg\",\"usedIngredientCount\":3,\"missedIngredientCount\":0,\"likes\":243}, " +
                            "{\"id\":65597,\"title\":\"Cinnamon Streusel Muffins\",\"image\":\"https:\\/\\/spoonacular.com\\/recipeImages\\/65597-312x231.jpg\",\"imageType\":\"jpg\",\"usedIngredientCount\":1,\"missedIngredientCount\":2,\"likes\":0}, " +
                            "{\"id\":155863,\"title\":\"Israeli Falafel\",\"image\":\"https:\\/\\/spoonacular.com\\/recipeImages\\/155863-312x231.jpg\",\"imageType\":\"jpg\",\"usedIngredientCount\":2,\"missedIngredientCount\":17,\"likes\":0}, " +
                            "{\"id\":556470,\"title\":\"Mango Smoothies\",\"image\":\"https:\\/\\/spoonacular.com\\/recipeImages\\/161181-312x231.jpg\",\"imageType\":\"jpg\",\"usedIngredientCount\":3,\"missedIngredientCount\":0,\"likes\":243}, " +
                            "{\"id\":556470,\"title\":\"King Cake with Pecan Praline Filling\",\"image\":\"https:\\/\\/spoonacular.com\\/recipeImages\\/855114-312x231.jpg\",\"imageType\":\"jpg\",\"usedIngredientCount\":3,\"missedIngredientCount\":0,\"likes\":243}, " +
                            "{\"id\":173003,\"title\":\"Grilled Marinated Steak Sandwiches\",\"image\":\"https:\\/\\/spoonacular.com\\/recipeImages\\/173003-312x231.jpg\",\"imageType\":\"jpg\",\"usedIngredientCount\":3,\"missedIngredientCount\":4,\"likes\":0}]";

            Fragment listFrag = new ListFragment();

            //bundles recipe arguments
            Bundle args = new Bundle();
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

    public void setItemsAccess(ItemAdapter setter, ArrayList<Item> itemArrayList) {
        mItemAdapter = setter;
        mItemsList = itemArrayList;
    }
}
