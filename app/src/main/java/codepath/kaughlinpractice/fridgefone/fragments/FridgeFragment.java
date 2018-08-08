package codepath.kaughlinpractice.fridgefone.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import codepath.kaughlinpractice.fridgefone.ItemAdapter;
import codepath.kaughlinpractice.fridgefone.MainActivity;
import codepath.kaughlinpractice.fridgefone.R;
import codepath.kaughlinpractice.fridgefone.Singleton;
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

    public boolean mFirstClick = false;
    public Singleton mSingleInstance;
    public MenuItem mSelectAllMenuBtn;
    public MenuItem mCancelMenuBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fridge, container, false);
    }

    // allows to use specific action bar
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    // inflates specific action bar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflate menu that adds items to the action bar
        inflater.inflate(R.menu.fridge_home_menu, menu);

        mSelectAllMenuBtn = menu.findItem(R.id.SelectAllMenubtn);
        mCancelMenuBtn = menu.findItem(R.id.CancelMenubtn);
        //set initial visibility of menu items
        mSelectAllMenuBtn.setVisible(false);
        mCancelMenuBtn.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handles presses on the action bar
        switch (item.getItemId()){
            case R.id.SelectAllMenubtn:
                mSingleInstance.setmAllSelected(true); // set All selected boolean to true
                mSingleInstance.setmNoneSelected(false);
                mItemAdapter.notifyItemRangeChanged(0, mItemAdapter.getItemCount());
                Toast.makeText(getActivity(), "All items selected", Toast.LENGTH_SHORT).show();

                return true;

            case R.id.CancelMenubtn:
                //set visibility of menu items to false
                mSelectAllMenuBtn.setVisible(false);
                mCancelMenuBtn.setVisible(false);

                mSingleInstance.setmSelectItemsBoolean(false);
                mSingleInstance.setmAllSelected(false); // set All selected boolean to false
                mSingleInstance.setmNoneSelected(true);

                mItemAdapter.notifyItemRangeChanged(0, mItemAdapter.getItemCount()); // notify the adapter if select all is changed

                mAddItemImageView.setVisibility(View.VISIBLE);
                mGenerateRecipeListImageView.setVisibility(View.INVISIBLE);

                // clear hashset after cancel button  is clicked
                mSingleInstance.getmSelectedNamesSet().clear();
                mFirstClick = false;
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();
        mItemList = new ArrayList<>();

        //get the instance of the singleton class
        mSingleInstance = Singleton.getSingletonInstance();
        mSingleInstance.getmSelectedNamesSet();
        mSingleInstance.getmAllItemNamesSet();


        mGenerateRecipeListImageView = (ImageView) view.findViewById(R.id.ivGenerateRecipeList);
        mGenerateRecipeListImageView.setVisibility(View.INVISIBLE);
        mAddItemImageView = (ImageView) view.findViewById(R.id.ivAddItem);

        mItemRecyclerView = (RecyclerView) view.findViewById(R.id.rvFridgeHomeView);

        // intialize itemadapter with interface for on select
        mItemAdapter =  new ItemAdapter(mItemList, getActivity(), new ItemAdapter.OnSelectInterface() {
            @Override
            public void onFirstSelect() {
                if(!mFirstClick) {
                    mAddItemImageView.setVisibility(View.INVISIBLE);
                    mGenerateRecipeListImageView.setVisibility(View.VISIBLE);
                    mSelectAllMenuBtn.setVisible(true);
                    mCancelMenuBtn.setVisible(true);
                    Toast.makeText(getActivity(), "Select your items", Toast.LENGTH_SHORT).show();
                    mFirstClick = true;
                }
            }
        });


        mItemRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        //construct adapter from data source
        mItemRecyclerView.setAdapter(mItemAdapter);

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
                final Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.anim_shake);
                view.startAnimation(anim);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        generateRecipes();
                        mSingleInstance.setmAllSelected(false); // set All selected boolean to false
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }
        });

        mAddItemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.anim_scaledown);
                view.startAnimation(anim);
                anim.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        AddItemFragment addItemFragment = new AddItemFragment();
                        addItemFragment.show(getFragmentManager(), "AddItemFragment");
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
    }

    public void generateRecipes() {

        if(mSingleInstance.getmSelectedNamesSet().isEmpty()){
            mSingleInstance.setmNoneSelected(true);
        }
        else{
            mSingleInstance.setmNoneSelected(false);
        }
        mSingleInstance.setmSelectedItemsString(String.join(",", mSingleInstance.getmSelectedNamesSet()));
        mSingleInstance.setmAllFridgeItemsString(String.join(",", mSingleInstance.getmAllItemNamesSet()));

        ((MainActivity) mContext).generateRecipes();
    }

    public void loadItems() {

        final Item.Query itemsQuery = new Item.Query();

        itemsQuery.findInBackground(new FindCallback<Item>()
        {
            @Override
            public void done(List<Item> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        mSingleInstance.getmAllItemNamesSet().add(objects.get(i).getName()); // add item name to hashset for all items in the fridge

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
