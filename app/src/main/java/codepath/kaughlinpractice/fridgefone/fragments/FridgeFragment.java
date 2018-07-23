package codepath.kaughlinpractice.fridgefone.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import codepath.kaughlinpractice.fridgefone.ItemAdapter;
import codepath.kaughlinpractice.fridgefone.MainActivity;
import codepath.kaughlinpractice.fridgefone.R;
import codepath.kaughlinpractice.fridgefone.model.Item;

public class FridgeFragment extends Fragment{

    private SwipeRefreshLayout swipeContainer;

    Context context;
    private ImageView ivGenerateRecipeList;
    private ImageView ivAddItem;
    private TextView tvFridgeItems;

    ItemAdapter itemAdapter;
    RecyclerView itemRecyclerView;

    private String fridge_items = "";
    private ArrayList<Item> lsItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fridge, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lsItem = new ArrayList<>();

        context = getContext();

        ivGenerateRecipeList = (ImageView) view.findViewById(R.id.ivGenerateRecipeList);
        ivAddItem = (ImageView) view.findViewById(R.id.ivAddItem);
        tvFridgeItems = (TextView) view.findViewById(R.id.tvFridgeItems);
        tvFridgeItems.setText("");


        itemRecyclerView = (RecyclerView) view.findViewById(R.id.rvFridgeHomeView);
        itemAdapter =  new ItemAdapter( lsItem, getActivity());
        itemRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        //construct adapter from data source
        itemRecyclerView.setAdapter(itemAdapter);

        //lsItem.add(new Item());
        loadTopItems();

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);




        /*

                // bundle communication between activity and fragment
        fridge_items.add(foodItem);

        TextView tvFridgeItems = findViewById(R.id.tvFridgeItems);
        tvFridgeItems.setText("");

        for (String item: fridge_items) {
            tvFridgeItems.setText(tvFridgeItems.getText().toString() + ", " + item);
        }
         */



        /*
        for (String item: fridge_items) {

            Log.d("FridgeFragment", "Items in fridge: " + item);
            lsItem.add(new Item());

            //tvFridgeItems.setText(tvFridgeItems.getText().toString() + ", " + item);
        }
        */

        ivGenerateRecipeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("FridgeFragment", "clicked on generate");
                generateRecipes();
            }
        });

        ivAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddItemFragment addItemFragment = new AddItemFragment();
                addItemFragment.show(getFragmentManager(), "AddItemFragment");
            }
        });
    }


    // TODO: Change from intent to bundle
    public void generateRecipes() {
        Log.d("FridgeFragment", "should move pages");
        ((MainActivity) context).generateRecipes(); // similar to Intent, going through Activity to get to new fragment
    }

    private void loadTopItems() {

        final Item.Query itemsQuery = new Item.Query();

        itemsQuery.findInBackground(new FindCallback<Item>()
        {
            @Override
            public void done(List<Item> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        Log.d("FridgeFragment", "item[" + i + "]= " + objects.get(i).getName()
                                + "\nImageurl =" + objects.get(i).getImageURL());
                        lsItem.add(0 , objects.get(i));
                        fridge_items = fridge_items + objects.get(i).getName();
                        if (i != objects.size()-1) {
                            fridge_items = fridge_items + ",";
                        }
                        itemAdapter.notifyItemInserted(lsItem.size()-1);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });

        // You need to refresh page for item names to load from Parse
        Log.d("FridgeFragment", "Items in Fridge: " + fridge_items);
        ((MainActivity) getContext()).setFridgeItems(fridge_items);
    }

    public void fetchTimelineAsync(int page) {
        itemAdapter.clear();
        loadTopItems();
        swipeContainer.setRefreshing(false);
    }

}
