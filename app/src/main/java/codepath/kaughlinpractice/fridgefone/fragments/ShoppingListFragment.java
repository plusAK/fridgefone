package codepath.kaughlinpractice.fridgefone.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import codepath.kaughlinpractice.fridgefone.R;
import codepath.kaughlinpractice.fridgefone.ShoppingAdapter;
import codepath.kaughlinpractice.fridgefone.model.ShoppingItem;


public class ShoppingListFragment extends Fragment {
    private Context mContext;
    private ImageView mAddItemImageView;
    private ArrayList<ShoppingItem> mShoppingItemList;
    private SwipeRefreshLayout mSwipeContainer;
    private ShoppingAdapter mShoppingAdapter;
    private RecyclerView mShoppingItemRecyclerView;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();
        mShoppingItemList = new ArrayList<>();
        mShoppingAdapter =  new ShoppingAdapter(mShoppingItemList, getActivity());



        mShoppingItemRecyclerView = (RecyclerView) view.findViewById(R.id.rvShoppingListView);

        //construct adapter from data source
        mShoppingItemRecyclerView.setAdapter(mShoppingAdapter);
        mShoppingItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        //load items
        loadItems();

        mSwipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        mSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }
    public void loadItems() {

        final ShoppingItem.Query shoppingItemQuery = new ShoppingItem.Query();

        shoppingItemQuery.findInBackground(new FindCallback<ShoppingItem>() {
            @Override
            public void done(List<ShoppingItem> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {

                        mShoppingItemList.add(0 , objects.get(i)); // add item to zero index
                        mShoppingAdapter.notifyItemInserted(mShoppingItemList.size()-1);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void fetchTimelineAsync(int page) {
        mShoppingAdapter.clear();
        loadItems();
        mSwipeContainer.setRefreshing(false);
    }
}
