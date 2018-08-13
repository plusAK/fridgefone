package codepath.kaughlinpractice.fridgefone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import codepath.kaughlinpractice.fridgefone.model.Item;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{
    private List<Item> mItems;
    private Context mContext;
    private OnSelectInterface mOnSelectInterface;
    Singleton mSingleInstance;

    public interface OnSelectInterface{
        void onFirstSelect();
    }

    public ItemAdapter(List<Item> mItems, Context mContext, OnSelectInterface onSelectInterface ) {
        this.mItems = mItems;
        this.mContext = mContext;
        this.mSingleInstance = Singleton.getSingletonInstance();
        this.mOnSelectInterface = onSelectInterface;
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
    public void onBindViewHolder(@NonNull final ItemAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.mFoodNameTextView.setText(mItems.get(position).getName());


        GlideApp.with(mContext)
                .load(mItems.get(position).getImageURL())
                .into(viewHolder.mFoodCircleImageView);

        // check if all are selected
        if (mSingleInstance.ismAllSelected()) {
            viewHolder.mCardLinearLayout.setBackgroundResource(R.drawable.cardview_border);
            //viewHolder.itemView.setAlpha(.65f); // changes opacity of image once clicked
        }
        else if(!mSingleInstance.ismAllSelected()) {
            viewHolder.mCardLinearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }


        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // when a user long clicks on an item, it calls the MainActivity's delete method which handles deletion

                // make sure the position is valid, i.e. actually exists in the view
                if (position != RecyclerView.NO_POSITION) {
                    final Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.anim_scaledownlong);
                    view.startAnimation(anim);
                    anim.setAnimationListener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            // get the recipe at the position, this won't work if the class is static
                            Item item = mItems.get(position);
                            // open up a pop up and send in food_name to ask if they specifically want to delete THIS item

                            ((MainActivity) mContext).askToDeleteItem(item);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
                return true;
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set to true to show user has started clicking items
                mSingleInstance.setmSelectItemsBoolean(true);
                mOnSelectInterface.onFirstSelect();


                // when a user long clicks on an item, it calls the MainActivity's delete method which handles deletion
                // make sure the position is valid, i.e. actually exists in the view
                if (position != RecyclerView.NO_POSITION && mSingleInstance.ismSelectItemsBoolean()) {

                    final Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.anim_scaledown);
                    view.startAnimation(anim);


                    String item_name = viewHolder.mFoodNameTextView.getText().toString();

                    // check if the hash set contains this item if so change the opacity back to regular
                    if (mSingleInstance.getmSelectedNamesSet().contains(item_name)) {
                        //if image is not clicked
                        mSingleInstance.getmSelectedNamesSet().remove(item_name);
                        view.setAlpha(1f); // changes opacity of image no click //TODO  change to dimen later
                        viewHolder.mCardLinearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                    } else {
                        // if image is clicked
                        mSingleInstance.getmSelectedNamesSet().add(item_name);
                        view.setAlpha(.65f); // changes opacity of image once clicked //TODO  change to dimen later
                        //view.setAlpha(R.dimen.selected_view); // changes opacity of image once clicked
                        viewHolder.mCardLinearLayout.setBackgroundResource(R.drawable.cardview_border);

                    }
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mFoodNameTextView;
        public ImageView mFoodCircleImageView;
        public LinearLayout mCardLinearLayout;
        public CardView mCardViewLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mFoodNameTextView = (TextView) itemView.findViewById(R.id.tvFood_Name);
            mFoodCircleImageView = (ImageView) itemView.findViewById(R.id.ivFood_Image);
            mCardLinearLayout = (LinearLayout) itemView.findViewById(R.id.cardLinearLayout);
            mCardViewLayout = (CardView) itemView.findViewById(R.id.cardview_id);

        }
    }
    // Clear all elements of the recycler
    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }
}
