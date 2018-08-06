package codepath.kaughlinpractice.fridgefone.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.kaughlinpractice.fridgefone.DetailsAdapter;
import codepath.kaughlinpractice.fridgefone.FridgeClient;
import codepath.kaughlinpractice.fridgefone.GlideApp;
import codepath.kaughlinpractice.fridgefone.MainActivity;
import codepath.kaughlinpractice.fridgefone.R;
import cz.msebera.android.httpclient.Header;


public class DetailsFragment extends Fragment {


    @BindView(R.id.ivRecipeImage) public ImageView ivRecipeImage;
    @BindView(R.id.ivFavoriteStar) public ImageView ivFavoriteStar;
    @BindView(R.id.tvDishTitle) public TextView tvDishTitle;
    @BindView(R.id.buttonBack) public Button buttonBack;
    @BindView(R.id.rvDetails) public RecyclerView rvDetails;


    public FridgeClient mClient;
    private HashMap<String, Boolean> user_dict = null;

    private String currentFilters = null;
    private boolean favorited;

    ArrayList<String> mInstructionsList;
    Collection<String> mIngredientsSet;
    DetailsAdapter mDetailsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    // set Transparent background for actionbar
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        AppCompatActivity appCompatActivity = (AppCompatActivity)context;
//        ActionBar bar= appCompatActivity.getSupportActionBar();
//        //bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00acacac")));
//        bar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initialize the client
        mClient = new FridgeClient(getActivity());

        mInstructionsList = new ArrayList<>();
        mIngredientsSet = new HashSet<>();
        favorited = false;

        Bundle args = getArguments();
        String name = args.getString("name");
        int id = args.getInt("id");
        String image = args.getString("image");
        final ArrayList<String> ingredients = args.getStringArrayList("ingredients");
        tvDishTitle.setText(name);

        GlideApp.with(getActivity())
                .load(image)
                .fitCenter()
                .into(ivRecipeImage);

        ivFavoriteStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favorited = !favorited;
                if (favorited) {
                    ivFavoriteStar.setImageResource(R.drawable.white_star_filled);
                } else {
                    ivFavoriteStar.setImageResource(R.drawable.white_star_outline);
                }
            }
        });


        if (FridgeClient.mUseInstructionsAPI) {

            // execute a GET request expecting a JSON object response
            mClient.getInstructions(id, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    try {
                        for (int i = 0; i < response.length(); i += 1) {
                            JSONObject partOfInstructions = response.getJSONObject(i);
                            parseInstructions(partOfInstructions);
                        }

                        ArrayList<String> details = new ArrayList<>();
                        details.add(getString(R.string.ingredients));
                        for (String ingredient: ingredients) {
                            details.add(ingredient);
                        }
                        details.add(getString(R.string.instructions));

                        for(String step: mInstructionsList) {
                            details.add(step);
                        }

                        mDetailsAdapter = new DetailsAdapter(details, ingredients.size());
                        // RecyclerView setup (layout manager, use adapter)
                        rvDetails.setLayoutManager(new LinearLayoutManager(getContext()));
                        rvDetails.setAdapter(mDetailsAdapter);
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
                Log.d("DetailFragment", "Converting string to JSON array error: " + e.getMessage());
            }
            try {
                for (int i = 0; i < response.length(); i += 1) {
                    JSONObject partOfInstructions = response.getJSONObject(i);
                    parseInstructions(partOfInstructions);
                }
                ArrayList<String> details = new ArrayList<>();
                details.add(getString(R.string.ingredients));

                for (String ingredient: ingredients) {
                    details.add(ingredient);
                }
                details.add(getString(R.string.instructions));
                for(String step: mInstructionsList) {
                    details.add(step);
                }

                mDetailsAdapter = new DetailsAdapter(details, ingredients.size());
                // RecyclerView setup (layout manager, use adapter)
                rvDetails.setLayoutManager(new LinearLayoutManager(getContext()));
                rvDetails.setAdapter(mDetailsAdapter);
            } catch (JSONException e) {
                Log.d("DetailFragment", "Error parsing through JSON Array" + e.getMessage());
            }
        }

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getContext()).generateRecipes(user_dict, currentFilters); //basically intent to go back to recipe list screen
            }
        });

    }

    public void parseInstructions(JSONObject partOfInstructions) {
        try {
            JSONArray steps = partOfInstructions.getJSONArray("steps");
            for (int i = 0; i < steps.length(); i += 1) {
                JSONObject step = steps.getJSONObject(i);
                String stepDetails = step.getString("step");
                mInstructionsList.add(stepDetails);
            }
        } catch (JSONException e) {
            Log.d("DetailFragment", "Error in parseInstructions " + e.getMessage());
        }
    }
}