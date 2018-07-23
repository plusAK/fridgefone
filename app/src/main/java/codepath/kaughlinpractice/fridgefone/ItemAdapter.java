package codepath.kaughlinpractice.fridgefone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import codepath.kaughlinpractice.fridgefone.model.Item;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{
    private List<Item> mItems;
    private Context mContext;

    public ItemAdapter(List<Item> mItems, Context mContext) {
        this.mItems = mItems;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder viewHolder, int i) {

        viewHolder.mFoodNameTextView.setText(mItems.get(i).getName());

        GlideApp.with(mContext)
                .load(mItems.get(i).getImageURL())
                .into(viewHolder.mFoodImageView);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener{

        public TextView mFoodNameTextView;
        public ImageView mFoodImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mFoodNameTextView = (TextView) itemView.findViewById(R.id.tvFood_Name);
            mFoodImageView = (ImageView) itemView.findViewById(R.id.ivFood_Image);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            // when a user long clicks on an item, it calls the MainActivity's delete method which handles deletion
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the recipe at the position, this won't work if the class is static
                Item item = mItems.get(position);
                // open up a pop up and send in food_name to ask if they specifically want to delete THIS item
                ((MainActivity) mContext).deleteItem(item);
            }
            return true;
        }

        @Override
        public void onClick(View view) {

        }
    }
    // Clean all elements of the recycler
    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }
}
