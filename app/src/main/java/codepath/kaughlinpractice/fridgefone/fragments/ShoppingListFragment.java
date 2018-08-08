package codepath.kaughlinpractice.fridgefone.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import codepath.kaughlinpractice.fridgefone.R;
import codepath.kaughlinpractice.fridgefone.ShoppingAdapter;
import codepath.kaughlinpractice.fridgefone.model.ShoppingItem;


public class ShoppingListFragment extends Fragment {
    private Context mContext;
    private ArrayList<ShoppingItem> mShoppingItemList;
    private SwipeRefreshLayout mSwipeContainer;
    private ShoppingAdapter mShoppingAdapter;
    private RecyclerView mShoppingItemRecyclerView;
    private ImageView mAddItemImageView;
    private EditText mNewItemEditText;




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
        mNewItemEditText = (EditText) view.findViewById(R.id.etNewItem);


        mAddItemImageView = (ImageView) view.findViewById(R.id.ivAddShopItem);
        mAddItemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // initialize animation
                final Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.anim_scaledown);
                // start animation
                view.startAnimation(anim);
                //add item to shopping list
                addShoppingItem();
            }
        });

        //load items
        loadItems();
        swipeToDelete();

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
    private void swipeToDelete(){
        //.left means swiping towards the left
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            public static final float AlPHA_FULL = 1.0f;

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                //if action state is in swipe mode
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    //initialize itemview
                    View itemView = viewHolder.itemView;

                    Paint paint = new Paint();
                    Bitmap icon;

                    //Color pull from right side swiping towards left
                    paint.setARGB(255,255,255,255);
                    c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                            (float) itemView.getRight(), (float) itemView.getBottom(), paint);

                    //icon left side swiping towards right
                    icon = BitmapFactory.decodeResource(getResources(),R.mipmap.trash);

                    c.drawBitmap(icon,
                            (float) itemView.getRight() - icon.getWidth(),
                            (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight())/2,paint);
                    Log.d("ShoppingListFragment", "onChildDraw: swiping right to left");


                    //Fade out the view when it is swiped out of the parent
                    final float alpha = AlPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                    viewHolder.itemView.setAlpha(alpha);
                    viewHolder.itemView.setTranslationX(dX);

                } else{
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();// swiped position

                if(direction == ItemTouchHelper.LEFT ){ // swipe right to left
                    //get the recipe at the position, this won't work if the class is static
                    ShoppingItem shoppingItem = mShoppingItemList.get(position);
                    // delete item from parse server
                    shoppingItem.deleteInBackground();
                    // remove position
                    mShoppingItemList.remove(position);
                    // notify adapter item was removed
                    mShoppingAdapter.notifyItemRemoved(position);

                    Toast.makeText(mContext, "swiped left", Toast.LENGTH_SHORT).show();
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mShoppingItemRecyclerView);


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

    public void addShoppingItem(){
        ShoppingItem shopItem = new ShoppingItem();
        // grab the EditText's content as a String
        String itemText = mNewItemEditText.getText().toString();
        //set text for shopping item
        shopItem.setName(itemText);
        // add the item to the parseServer
        shopItem.saveInBackground();
        // clear the EditText by setting it to an empty String
        mNewItemEditText.setText("");
        // display a notification to the user
        Toast.makeText(getActivity(), "Item:" + itemText +" added to list", Toast.LENGTH_SHORT).show();
        //reload items to screen
        fetchTimelineAsync(0);
    }

    public void fetchTimelineAsync(int page) {
        mShoppingAdapter.clear();
        loadItems();
        mSwipeContainer.setRefreshing(false);
    }
}
