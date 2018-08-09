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
        final ViewHolder shoppingItemsViewHolder = (ViewHolder) viewHolder;

//        viewHolder.mShopFoodNameTextView.setText(mShoppingItems.get(position).getName());
        viewHolder.mShopFoodNameTextView.setText(mShoppingItems.get(position).getName());

//        //change color depending on the row
        if (position % 2 == 1) {
            shoppingItemsViewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.alternating_gray));
        }
        else{
            shoppingItemsViewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.theme_white));
        }


//      TODO-- if you want to use image for item
//        GlideApp.with(mContext)
//                .load(mShoppingItems.get(position).getImageURL())
//                .into(viewHolder.mShopFoodImageView);

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
            //      TODO-- if you want to use image for item
            //mShopFoodImageView = (ImageView) itemView.findViewById(R.id.ivShop_Food_Image);
        }
    }
    // Clean all elements of the recycler
    public void clear() {
        mShoppingItems.clear();
        notifyDataSetChanged();
    }
}
