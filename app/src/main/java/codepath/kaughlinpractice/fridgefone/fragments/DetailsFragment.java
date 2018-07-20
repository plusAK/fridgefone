package codepath.kaughlinpractice.fridgefone.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.kaughlinpractice.fridgefone.GlideApp;
import codepath.kaughlinpractice.fridgefone.MainActivity;
import codepath.kaughlinpractice.fridgefone.R;
import codepath.kaughlinpractice.fridgefone.model.Recipe;
import cz.msebera.android.httpclient.Header;


public class DetailsFragment extends Fragment {

    public Recipe recipe;
    @BindView(R.id.ivRecipeImage) public ImageView ivRecipeImage;
    @BindView(R.id.tvDishTitle) public TextView tvDishTitle;
    @BindView(R.id.tvIngredients) public TextView tvIngredients;
    @BindView(R.id.tvInstructions) public TextView tvInstructions;
    @BindView(R.id.buttonBack) public Button buttonBack;

    private boolean use_api = false;
    // the base URL for the API
    public final static String API_BASE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com";
    // the parameter name for the API key
    public final static String API_KEY_PARAM = "X-Mashape-Key";
    public final static String KEY_ACCEPT_PARAM = "Accept";
    // instance field
    AsyncHttpClient client;

    ArrayList<String> ingredientsList;
    String instructionsString;
    String ingredientsString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initialize the client
        client = new AsyncHttpClient();

        Bundle args = getArguments();
        String name = args.getString("name");
        tvDishTitle.setText(name);

        tvInstructions.setText("");
        tvIngredients.setText("");
        instructionsString = "<big><b>Instructions</b></big><br />";
        ingredientsString = "<big><b>Ingredients</b></big>";

        int id = args.getInt("id");
        String image = args.getString("image");

        GlideApp.with(getActivity())
                .load(image)
                .circleCrop()
                .into(ivRecipeImage);


        if (use_api) {
            String url = API_BASE_URL + "/recipes/" + id + "/analyzedInstructions";
            // set the request parameters
            client.addHeader(API_KEY_PARAM, getString(R.string.api_key));
            client.addHeader(KEY_ACCEPT_PARAM, "application/json");
            RequestParams params = new RequestParams();
            params.put("stepBreakdown", false);

            // execute a GET request expecting a JSON object response
            client.get(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    String responseString = response.toString();
                    Log.d("DetailFragment", "JSON Array : " + responseString);
                    try {
                        for (int i = 0; i < response.length(); i += 1) {
                            JSONObject partOfInstructions = response.getJSONObject(i);
                            beautifyInstructions(partOfInstructions);
                            Log.d("DetailFragment", "Step " + Integer.toString(i + 1) + ": " + partOfInstructions.toString());
                        }
                    } catch (JSONException e) {
                        Log.d("DetailFragment", e.getMessage());
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    Log.d("DetailFragment", "Error: " + throwable);
                }
            });
        }
        else {
            // with step breakdown
            String responseString = "[{\"name\":\"\",\"steps\":[{\"number\":1,\"step\":\"Beat the egg in a bowl, add a pinch of salt and the flour and pour in the extra virgin olive oil and the beer: if needed, add a tablespoon of water to make a smooth but not too liquid batter. It is supposed to cover the apples, not to slide off!Peel the apples, core them paying attention not to break them and cut the apples into horizontal slices, 1 cm thick.\",\"ingredients\":[{\"id\":1034053,\"name\":\"extra virgin olive oil\",\"image\":\"olive-oil.jpg\"},{\"id\":9003,\"name\":\"apple\",\"image\":\"apple.jpg\"},{\"id\":20081,\"name\":\"all purpose flour\",\"image\":\"flour.png\"},{\"id\":14003,\"name\":\"beer\",\"image\":\"beer.jpg\"},{\"id\":2047,\"name\":\"salt\",\"image\":\"salt.jpg\"},{\"id\":1123,\"name\":\"egg\",\"image\":\"egg.jpg\"}],\"equipment\":[{\"id\":404783,\"name\":\"bowl\",\"image\":\"bowl.jpg\"}]},{\"number\":2,\"step\":\"Heat the olive oil in a large frying pan. The right moment to fry the apples is when the oil starts to smoke, as grandma says. Dip the apple slices into the batter and deep fry them until cooked through and golden on both sides.\",\"ingredients\":[{\"id\":4053,\"name\":\"olive oil\",\"image\":\"olive-oil.jpg\"},{\"id\":9003,\"name\":\"apple\",\"image\":\"apple.jpg\"}],\"equipment\":[{\"id\":404645,\"name\":\"frying pan\",\"image\":\"pan.png\"}]},{\"number\":3,\"step\":\"Transfer the apples into a plate lined with a paper towel. Sprinkle the fritters with icing sugar and serve them warm.\",\"ingredients\":[{\"id\":9003,\"name\":\"apple\",\"image\":\"apple.jpg\"}],\"equipment\":[{\"id\":405895,\"name\":\"paper towels\",\"image\":\"paper-towels.jpg\"}]}]}]";
            JSONArray response = null;
            try {
                response = new JSONArray(responseString);
            } catch (JSONException e) {
                Log.d("DetailFragment", "Not api_call error: " + e.getMessage());
            }
            try {
                for (int i = 0; i < response.length(); i += 1) {
                    JSONObject partOfInstructions = response.getJSONObject(i);
                    beautifyInstructions(partOfInstructions);
                    Log.d("DetailFragment", "Step " + Integer.toString(i + 1) + ": " + partOfInstructions.toString());
                }
                tvIngredients.setText(Html.fromHtml(ingredientsString));
                tvInstructions.setText(Html.fromHtml(instructionsString + "< br />< br />"));
            } catch (JSONException e) {
                Log.d("DetailFragment", e.getMessage());
            }
        }

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getContext()).generateRecipes(); //basically intent to go back to recipe list screen
            }
        });

    }

    public void beautifyInstructions(JSONObject partOfInstructions) {
        String newText = "";
        try {
            String name = partOfInstructions.getString("name");
            newText = newText + "<u><b>" + name + "</b></u>" +  "<br />";
            JSONArray steps = partOfInstructions.getJSONArray("steps");
            for (int i = 0; i < steps.length(); i += 1) {
                JSONObject step = steps.getJSONObject(i);
                int stepNum = step.getInt("number");
                String stepDetails = step.getString("step");
                newText = newText + "<b>" + stepNum + ".</b> " + stepDetails + "<br /><br />";
                addToIngredientsList(step.getJSONArray("ingredients"));
            }
            instructionsString = instructionsString + newText;
        } catch (JSONException e) {
            Log.d("DetailFragment", e.getMessage());
        }
    }

    public void addToIngredientsList(JSONArray newIngredients) {
        // check to see if it exists
        // if it does not, then add item
        String newText = "";
        try {
            for (int i = 0; i < newIngredients.length(); i += 1) {
                JSONObject ingredientItem = newIngredients.getJSONObject(i);
                String ingredient = ingredientItem.getString("name");
                newText = newText + "<br/>" + ingredient;
            }
            ingredientsString = ingredientsString + newText;
        } catch (JSONException e) {
            Log.d("DetailFragment", e.getMessage());
        }
    }
}
