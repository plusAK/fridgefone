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

        viewHolder.tvFood_Name.setText(mItems.get(i).getName());
        GlideApp.with(mContext)
                .load(mItems.get(i).getImageURL())
                .into(viewHolder.ivFood_Image);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvFood_Name;
        public ImageView ivFood_Image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFood_Name = (TextView) itemView.findViewById(R.id.tvFood_Name);
            ivFood_Image = (ImageView) itemView.findViewById(R.id.ivFood_Image);
        }
    }
    // Clean all elements of the recycler
    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }
}
