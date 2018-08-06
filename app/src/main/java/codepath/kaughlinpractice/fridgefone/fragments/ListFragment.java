package codepath.kaughlinpractice.fridgefone.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import codepath.kaughlinpractice.fridgefone.FilterAdapter;
import codepath.kaughlinpractice.fridgefone.MainActivity;
import codepath.kaughlinpractice.fridgefone.R;
import codepath.kaughlinpractice.fridgefone.RecipeAdapter;
import codepath.kaughlinpractice.fridgefone.model.Recipe;

public class ListFragment extends Fragment {

    private RecyclerView mFilterRecyclerView;
    private FilterAdapter mFilterAdapter;
    private ArrayList<String> mFilters;

    private RecipeAdapter mRecipeAdapter;
    private ArrayList<Recipe> mRecipeArrayList;
    private RecyclerView mRecipeRecyclerView;
    private ImageView mFilterImageView;
    private TextView mCurrentFilters;
    private Button mButtonBack;

    public String[] mFilterTitles = {"Vegetarian", "Vegan", "Gluten Free", "Dairy Free", "Very Healthy", "Very Popular", "Cheap"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFilters = new ArrayList<>();
        for (int i = 0; i < mFilterTitles.length; i++)
        {
            mFilters.add(mFilterTitles[i]);
        }

        mFilterRecyclerView = view.findViewById(R.id.HorizontalListRecyclerView);
        // add a divider after each item for more clarity
        mFilterRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));
        mFilterAdapter = new FilterAdapter(mFilters);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mFilterRecyclerView.setLayoutManager(horizontalLayoutManager);
        mFilterRecyclerView.setAdapter(mFilterAdapter);

        mCurrentFilters = (TextView) view.findViewById(R.id.currentFilters);
        // find RecyclerView
        mRecipeRecyclerView = (RecyclerView) view.findViewById(R.id.rvRecipes);
        // init the array list (data source)
        mRecipeArrayList = new ArrayList<>();

        mFilterImageView = (ImageView) view.findViewById(R.id.filterImageView);
        mButtonBack = (Button) view.findViewById(R.id.buttonBack);

        // construct the adapter from this data source
        mRecipeAdapter = new RecipeAdapter(mRecipeArrayList);

        // RecyclerView setup (layout manager, user adapter)
        mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // set the adapter
        mRecipeRecyclerView.setAdapter(mRecipeAdapter);

        //get bundle contents
        Bundle args = getArguments();
        String responseForBundle = args.getString("responseForBundle");
        JSONArray response = null;
        try {
            // change string into JSONArray
            response = new JSONArray(responseForBundle);
        } catch (JSONException e) {
            Log.d("ListFragment", "Error: " + e.getMessage());
        }
        try {
            for (int i = 0; i < response.length(); i += 1) {
                Recipe recipe = Recipe.fromJSON(response.getJSONObject(i), getActivity(), args, mRecipeArrayList, mRecipeAdapter);
            }
        } catch (JSONException e) {
            Log.d("ListFragment", "Error: " + e.getMessage());
        }

        String currentFilters = args.getString("currentFilters");
        if (currentFilters != null) {
            mCurrentFilters.setText(currentFilters);
        }

        mFilterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterFragment filterFragment = new FilterFragment();
                filterFragment.show(getFragmentManager(), "FilterFragment");
            }
        });

        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getContext()).goToMyFridge();
            }
        });
    }
}
