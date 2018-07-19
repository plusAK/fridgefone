package codepath.kaughlinpractice.fridgefone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import codepath.kaughlinpractice.fridgefone.model.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder>{

    // private final Handlers mHandler;
    private List<Recipe> mRecipes;
    private Context context;

    // AsyncHttpResponseHandler handler = new JsonHttpResponseHandler();
   // TwitterClient client = TwitterApp.getRestClient(context);

    /**
    interface Handlers {
        void onItemClicked(Tweet t, Context c);
    }

     */

    // pass in the Tweets array in the constructor
    public RecipeAdapter(List<Recipe> recipes) {
        mRecipes = recipes;
    }

    // for each row, inflate the layout and cache reference into ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View recipeView = inflater.inflate(R.layout.recipe_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(recipeView);
        return viewHolder;
    }

    // bind the values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        // get the data according to position
        Recipe recipe = mRecipes.get(position);

        // populate the views according to this data
        holder.recipe_name.setText(recipe.getName());




        GlideApp.with(context)
                .load(recipe.getImage())
                .circleCrop()
                .into(holder.recipe_image);

    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    /* Within the RecyclerView.Adapter class */

    // Clean all elements of the recycler
    public void clear() {
        mRecipes.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Recipe> list) {
        mRecipes.addAll(list);
        notifyDataSetChanged();
    }

    // create ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView recipe_image;
        public TextView recipe_name;

        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById lookups
            recipe_image = (ImageView) itemView.findViewById(R.id.ivImage);
            recipe_name = (TextView) itemView.findViewById(R.id.tvName);

            itemView.setOnClickListener(this);
        }

        // when the user clicks on a row, show TweetDetailViewActivity for the selected tweet
        @Override
        public void onClick(View view) {
            // gets item position
            Log.d("RecipeAdapter", String.format("A recipe was clicked"));
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the recipe at the position, this won't work if the class is static
                Recipe recipe = mRecipes.get(position);
                ((MainActivity) context).openDetails(recipe); // similar to Intent, going through Activity to get to new fragment
                Log.d("RecipeAdapter", String.format("Got this recipe: " + recipe.getName()));
            }
        }
    }
}
