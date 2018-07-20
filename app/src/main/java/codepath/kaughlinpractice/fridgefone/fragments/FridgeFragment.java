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

    private ArrayList<String> fridge_items;
    private ArrayList<Item> lsItem;

    //private ArrayList<String> fridge_items;

    //List<Item> lsItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fridge, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fridge_items = new ArrayList<>();
        lsItem = new ArrayList<>();

        //Bundle args = getArguments();
        //fridge_items = args.getStringArrayList("fridge_items");

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
        loadItems();

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

    private void loadItems() {

        //get data from parse server
        final Item.Query postsQuery = new Item.Query();

        postsQuery.findInBackground(new FindCallback<Item>()

        {
            @Override
            public void done(List<Item> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        Log.d("FridgeFragment", "item[" + i + "]= " + objects.get(i).getName() + "\nImageurl =" + objects.get(i).getImageURL());
                        lsItem.add(0 , objects.get(i)); // add item to zero index
                        itemAdapter.notifyItemInserted(lsItem.size()-1);
                    }

                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void fetchTimelineAsync(int page) {
        itemAdapter.clear();
        loadItems();
        swipeContainer.setRefreshing(false);
    }

}
