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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import codepath.kaughlinpractice.fridgefone.ItemAdapter;
import codepath.kaughlinpractice.fridgefone.MainActivity;
import codepath.kaughlinpractice.fridgefone.R;
import codepath.kaughlinpractice.fridgefone.model.Item;

public class FridgeFragment extends Fragment{

    private final static int mNumVisibleShelves = 12;
    private Context mContext;
    private ImageView mGenerateRecipeListImageView;
    private ImageView mAddItemImageView;
    private ArrayList<Item> mItemList;
    private SwipeRefreshLayout mSwipeContainer;
    private ItemAdapter mItemAdapter;
    private RecyclerView mItemRecyclerView;

    private HashMap<String, Boolean> user_dict = null;
    public ImageView mSelectItemsImageView;
    public Button mCancelSelectButton;
    public Button mSelectAllButton;

    //public ArrayList<View> mSelectedViewsArray;
    public ArrayList<String> mSelectedNamesArray;
    public HashSet<String> mSelectedNamesSet;
    public HashSet<String> mAllItemNamesSet;
    public String mSelectedNamesString = "";
    public String mAllNamesString = "";

    public boolean mSelectItemsBoolean = false;
    public boolean mAllSelected = false;
    private String currentFilters = null;
    public boolean mNoneSelected = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fridge, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mContext = getContext();
        mItemList = new ArrayList<>();
        //mSelectedViewsArray = new ArrayList<>();
        mSelectedNamesSet = new HashSet<>();
        mAllItemNamesSet = new HashSet<>();

        mGenerateRecipeListImageView = (ImageView) view.findViewById(R.id.ivGenerateRecipeList);
        mAddItemImageView = (ImageView) view.findViewById(R.id.ivAddItem);

        mItemRecyclerView = (RecyclerView) view.findViewById(R.id.rvFridgeHomeView);
        mItemAdapter =  new ItemAdapter( mItemList, getActivity(),this);
        mItemRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        //construct adapter from data source
        mItemRecyclerView.setAdapter(mItemAdapter);

        mSelectItemsImageView = (ImageView) view.findViewById(R.id.ivSelectItems);
        mCancelSelectButton = (Button) view.findViewById(R.id.btnCancelSelect);
        mCancelSelectButton.setVisibility(View.INVISIBLE);

        mSelectAllButton = (Button) view.findViewById(R.id.btnSelectAll);
        mSelectAllButton.setVisibility(View.INVISIBLE);

        // give access to MainActivity to adapter
        ((MainActivity) mContext).setItemsAccess(mItemAdapter, mItemList);


        loadItems(); //load items to fridge

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

        mGenerateRecipeListImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("FridgeFragment", "clicked on generate");
                generateRecipes();
            }
        });

        mAddItemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddItemFragment addItemFragment = new AddItemFragment();
                addItemFragment.show(getFragmentManager(), "AddItemFragment");
            }
        });

        mSelectItemsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectItemsBoolean = true;
                //mNoneSelected = false;
                //((MainActivity) getContext()).setSelectTrue();
                mCancelSelectButton.setVisibility(View.VISIBLE);
                mSelectAllButton.setVisibility(View.VISIBLE);
                mAddItemImageView.setVisibility(View.INVISIBLE);
                mSelectItemsImageView.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), "Select your items", Toast.LENGTH_LONG).show();
            }
        });

        mCancelSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectItemsBoolean = false;
                mAllSelected = false;
                mNoneSelected = true;
                mItemAdapter.notifyItemRangeChanged(0, mItemAdapter.getItemCount()); // notify the adapter if select all is changed

                mCancelSelectButton.setVisibility(View.INVISIBLE);
                mSelectAllButton.setVisibility(View.INVISIBLE);
                mSelectItemsImageView.setVisibility(View.VISIBLE);
                mAddItemImageView.setVisibility(View.VISIBLE);

                // clear hashset after cancel button  is clicked
                mSelectedNamesSet.clear();
                Log.d("FridgeFragment", "Selected Items in Fridge Hashset after Cancel: " + mSelectedNamesSet);
            }
        });
        mSelectAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAllSelected = true;
                mNoneSelected = false;
                mItemAdapter.notifyItemRangeChanged(0, mItemAdapter.getItemCount());
                Toast.makeText(getActivity(), "All items selected", Toast.LENGTH_LONG).show();
                Log.d("FridgeFragment", "All Items in mAllItemNamesSet: " + mAllItemNamesSet);
                //mSelectCheckImageView.setVisibility(View.VISIBLE);
            }
        });


    }

    public void generateRecipes() {


        Log.d("FridgeFragment", "Selected Items in Fridge Hashset: " + mSelectedNamesSet);

//        mSelectedNamesArray = new ArrayList<>(mSelectedNamesSet);
//        for(int i = 0; i <mSelectedNamesSet.size();i++){
//            mSelectedItemsString += mSelectedNamesArray.get(i);
//        }

        if(mSelectedNamesSet.isEmpty()){
            mNoneSelected = true;
        }
        else{
            mNoneSelected = false;
        }

        mSelectedNamesString = String.join(",", mSelectedNamesSet);
        mAllNamesString = String.join(",", mAllItemNamesSet);


        // You need to refresh page for item names to load from Parse
        Log.d("FridgeFragment", "Selected Items in Fridge String: " + mSelectedNamesString);
        Log.d("FridgeFragment", "All Items in Fridge String: " + mAllNamesString);
        ((MainActivity) getContext()).setFridgeItems(mAllNamesString, mSelectedNamesString, mAllSelected, mNoneSelected);

        Log.d("FridgeFragment", "should move pages");
        ((MainActivity) mContext).generateRecipes(user_dict, currentFilters);
    }

    public void loadItems() {

        final Item.Query itemsQuery = new Item.Query();

        itemsQuery.findInBackground(new FindCallback<Item>()
        {
            @Override
            public void done(List<Item> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        Log.d("FridgeFragment", "item[" + i + "]= " + objects.get(i).getName()
                                + "\nImageurl =" + objects.get(i).getImageURL());

                        mAllItemNamesSet.add(objects.get(i).getName()); // add item name to hashset for all items in the fridge

                        mItemList.add(0 , objects.get(i)); // add item to zero index
                        mItemAdapter.notifyItemInserted(mItemList.size()-1);
                    }
                    if(mItemList.size() < mNumVisibleShelves){

                        for(int i = mItemList.size();i < mNumVisibleShelves; i ++){
                            mItemList.add(new Item());
                        }
                    }
                    else{
                        while(mItemList.size() % 3 != 0){
                            mItemList.add(new Item());
                        }
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void fetchTimelineAsync(int page) {
        mItemAdapter.clear();
        loadItems();
        mSwipeContainer.setRefreshing(false);
    }
}
