package codepath.kaughlinpractice.fridgefone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import codepath.kaughlinpractice.fridgefone.model.ShoppingItem;

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ViewHolder>{
    private List<ShoppingItem> mShoppingItems;
    private Context mContext;

    public ShoppingAdapter(List<ShoppingItem> mShoppingItems, Context mContext){
        this.mShoppingItems = mShoppingItems;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ShoppingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // get the context and create the inflater
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.shopping_item, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ShoppingAdapter.ViewHolder viewHolder, final int position) {

//        viewHolder.mShopFoodNameTextView.setText(mShoppingItems.get(position).getName());
        viewHolder.mShopFoodNameTextView.setText(mShoppingItems.get(position).getName());

//        GlideApp.with(mContext)
//                .load(mShoppingItems.get(position).getImageURL())
//                .into(viewHolder.mShopFoodImageView);



//        //long click to delete an item
//        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//
//                // make sure the position is valid, i.e. actually exists in the view
//                if (position != RecyclerView.NO_POSITION) {
//                    // get the recipe at the position, this won't work if the class is static
//                    ShoppingItem shoppingItem = mShoppingItems.get(position);
//                    // open up a pop up and send in food_name to ask if they specifically want to delete THIS item
//
//                    //TODO -- delete item
//                    //((MainActivity) mContext).askToDeleteItem(ShoppingItem);
//                }
//                return true;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mShoppingItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mShopFoodNameTextView;
        //public ImageView mShopFoodImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mShopFoodNameTextView = (TextView) itemView.findViewById(R.id.tvShop_Food_Name);
            //mShopFoodImageView = (ImageView) itemView.findViewById(R.id.ivShop_Food_Image);
        }
    }
    // Clean all elements of the recycler
    public void clear() {
        mShoppingItems.clear();
        notifyDataSetChanged();
    }
}
