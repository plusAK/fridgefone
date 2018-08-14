package codepath.kaughlinpractice.fridgefone;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class FridgeClient {

    public final static Integer NUMBER_OF_RECIPES = 12;
    // the base URL for the API
    public final static String API_BASE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com";
    // the parameter name for the API key
    public final static String API_KEY_PARAM = "X-Mashape-Key";
    public final static String KEY_ACCEPT_PARAM = "Accept";

    // initialize Singleton instance
    public Singleton mSingleInstance = Singleton.getSingletonInstance();
    public AsyncHttpClient mClient;
    public Context mContext;

    public static boolean mUseInstructionsAPI = false;
    public static boolean mUseAutocompleteAPI = true;
    public static boolean mUseGenerateRecipeAPI = true;

    public FridgeClient(Context context) {
        this.mContext = context;
        mClient = new AsyncHttpClient();
    }

    /* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
     * 2. Define the parameters to pass to the request (query or body)
     *    i.e RequestParams params = new RequestParams("foo", "bar");
     * 3. Define the request method and make a call to the client
     *    i.e client.get(apiUrl, params, handler);
     *    i.e client.post(apiUrl, params, handler);
     */

    // DEFINE METHODS for different API endpoints here
    public void getRecipeList(AsyncHttpResponseHandler handler) {

        String apiUrl = getApiUrl("recipes/findByIngredients");
        mClient.addHeader(API_KEY_PARAM, mContext.getResources().getString(R.string.api_key));
        mClient.addHeader(KEY_ACCEPT_PARAM, "application/json");

        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("number", NUMBER_OF_RECIPES);
        if (mSingleInstance.ismAllSelected() == false && mSingleInstance.ismNoneSelected() == false) {
            params.put("ingredients", mSingleInstance.getmSelectedItemsString());
        } else {
            params.put("ingredients", mSingleInstance.getmAllFridgeItemsString());
        }

        mClient.get(apiUrl, params, handler);
    }

    public void getAutoComplete(String foodItem, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("food/ingredients/autocomplete");
        mClient.addHeader(API_KEY_PARAM,mContext.getResources().getString(R.string.api_key));
        mClient.addHeader(KEY_ACCEPT_PARAM, "application/json");
        RequestParams params = new RequestParams();
        params.put("query", foodItem);
        mClient.get(apiUrl,params,handler);
    }

    public void getInstructions(int id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("/recipes/" + id + "/analyzedInstructions");
        mClient.addHeader(API_KEY_PARAM, mContext.getResources().getString(R.string.api_key));
        mClient.addHeader(KEY_ACCEPT_PARAM, "application/json");
        // set the request parameters
        RequestParams params = new RequestParams();
        params.put("stepBreakdown", false);
        mClient.get(apiUrl,params,handler);
    }

    public void getInformation(int id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("/recipes/" + id + "/information");
        mClient.addHeader(API_KEY_PARAM, mContext.getResources().getString(R.string.api_key));
        mClient.addHeader(KEY_ACCEPT_PARAM, "application/json");
        // set the request parameters
        RequestParams params = new RequestParams();
        mClient.get(apiUrl,params,handler);
    }


    private String getApiUrl(String path) {
            return this.API_BASE_URL + "/" + path;
    }
}
