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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.kaughlinpractice.fridgefone.DetailsAdapter;
import codepath.kaughlinpractice.fridgefone.FridgeClient;
import codepath.kaughlinpractice.fridgefone.GlideApp;
import codepath.kaughlinpractice.fridgefone.R;
import codepath.kaughlinpractice.fridgefone.model.Recipe;
import cz.msebera.android.httpclient.Header;


public class DetailsFragment extends Fragment {


    @BindView(R.id.ivRecipeImage) public ImageView mRecipeImageView;
    @BindView(R.id.ivFavoriteStar) public ImageView mFavoriteStarImageView;
    @BindView(R.id.tvDishTitle) public TextView mDishTitleTextView;
    @BindView(R.id.rvDetails) public RecyclerView mDetailsRecyclerView;

    public FridgeClient mClient;
    private boolean mIsFavorited;

    ArrayList<String> mInstructionsList;
    DetailsAdapter mDetailsAdapter;
    HashSet<String> uniqueIngredients;

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
        mClient = new FridgeClient(getActivity());

        mInstructionsList = new ArrayList<>();
        mIsFavorited = false;

        Bundle args = getArguments();
        String name = args.getString("name");
        int id = args.getInt("id");
        String image = args.getString("image");
        uniqueIngredients = new HashSet<>(args.getStringArrayList("ingredients"));

        mDishTitleTextView.setText(name);

        GlideApp.with(getActivity())
                .load(image)
                .fitCenter()
                .into(mRecipeImageView);

        mFavoriteStarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsFavorited = !mIsFavorited;
                if (mIsFavorited) {
                    mFavoriteStarImageView.setImageResource(R.drawable.white_star_filled);
                    Toast.makeText(getActivity(),mDishTitleTextView.getText().toString() + " added to recipe favorites.", Toast.LENGTH_SHORT).show();
                } else {
                    mFavoriteStarImageView.setImageResource(R.drawable.white_star_outline);
                }
            }
        });

        final Recipe serverRecipe = Recipe.findRecipeInServer(id);

        if (serverRecipe == null || serverRecipe.getInstructions() == null) {
            // execute a GET request expecting a JSON object response
            mClient.getInstructions(id, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    if (serverRecipe != null) {
                        serverRecipe.setInstructions(response.toString());
                        serverRecipe.saveToServer();
                    }

                    parseInstructionResponse(response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    Log.d("DetailFragment", "Error: " + throwable);
                }
            });
        } else {
            try {
                JSONArray response = new JSONArray(serverRecipe.getInstructions());
                parseInstructionResponse(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseInstructionResponse(JSONArray instructionResponse) {
        // iterate through every step in the instruction response
        for (int i = 0; i < instructionResponse.length(); i += 1) {
            JSONObject instructionSteps = null;
            try {
                instructionSteps = instructionResponse.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            parseInstructionSteps(instructionSteps);
        }

        ArrayList<String> details = new ArrayList<>();
        details.add(getString(R.string.ingredients));

        ArrayList<String> allIngredients = new ArrayList<>(uniqueIngredients);
        for (String ingredient: allIngredients) {
            details.add(ingredient);
        }
        details.add(getString(R.string.instructions));

        for(String step: mInstructionsList) {
            details.add(step);
        }

        mDetailsAdapter = new DetailsAdapter(details, allIngredients.size());
        // RecyclerView setup (layout manager, use adapter)
        mDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mDetailsRecyclerView.setAdapter(mDetailsAdapter);
    }



    private void parseInstructionSteps(JSONObject instructionSteps) {
        try {
            JSONArray steps = instructionSteps.getJSONArray("steps");
            for (int i = 0; i < steps.length(); i += 1) {
                JSONObject step = steps.getJSONObject(i);
                String stepDetails = step.getString("step");
                mInstructionsList.add(stepDetails);
                addToIngredientsList(step.getJSONArray("ingredients"));
            }
        } catch (JSONException e) {
            Log.d("DetailFragment", "Error " + e.getMessage());
        }
    }

    public void addToIngredientsList(JSONArray newIngredients) {
        // check to see if it exists
        // if it does not, then add item
        try {
            for (int i = 0; i < newIngredients.length(); i += 1) {
                JSONObject ingredientItem = newIngredients.getJSONObject(i);
                String ingredientName = ingredientItem.getString("name");
                if (!uniqueIngredients.contains(ingredientName)) {
                    uniqueIngredients.add(ingredientName);
                }
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
    }

}