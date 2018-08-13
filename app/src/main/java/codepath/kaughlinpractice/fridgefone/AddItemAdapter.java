package codepath.kaughlinpractice.fridgefone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import codepath.kaughlinpractice.fridgefone.model.Item;

public class AddItemAdapter extends ArrayAdapter<Item> {

    private ArrayList<Item> mAddItems;
    private Context mContext;

    public AddItemAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.mAddItems = new ArrayList<>();
        this.mContext = context;
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.add_item, parent, false);
        }

        Item item = mAddItems.get(position);
        TextView mAddItemTextView = (TextView) view.findViewById(R.id.addItemTextView);
        ImageView mAddItemImageView = (ImageView) view.findViewById(R.id.addItemImageView);

        mAddItemTextView.setText(item.getName());
        GlideApp.with(mContext)
                .load(item.getImageURL())
                .into(mAddItemImageView);

        return view;
    }

    public void add(Item addItem) {
        mAddItems.add(addItem);
        addAll(mAddItems);
    }

    @Override
    public void clear() {
        mAddItems.clear();
    }

    @Override
    public int getCount() {
        return mAddItems.size();
    }

    @Override
    public Item getItem(int position) {
        return mAddItems.get(position);
    }
}
