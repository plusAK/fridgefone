package codepath.kaughlinpractice.fridgefone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import codepath.kaughlinpractice.fridgefone.model.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    // private final Handlers mHandler;
    private ArrayList<Recipe> mRecipes;
    private Context context;

    public RecipeAdapter(ArrayList<Recipe> recipes) {
        mRecipes = recipes;
    }

    // creates and inflates a new view
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get the context and create the inflater
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        switch(viewType) {
            case 0:
                // create the view using the layout
                View leftRecipeView = inflater.inflate(R.layout.left_recipe_item, parent, false);
                // return a new ViewHolder
                return new LeftRecipeViewHolder(leftRecipeView);
            case 1:
                // create the view using the layout
                View rightRecipeView = inflater.inflate(R.layout.right_recipe_item, parent, false);
                // return a new ViewHolder
                return new RightRecipeViewHolder(rightRecipeView);
            default:
                return null;
        }
    }

    // bind the values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        // get the data according to position
        Recipe recipe1 = null;
        Recipe recipe2 = null;
        Recipe recipe3 = null;
        final int position1 = position * 3;
        final int position2 = (position * 3) + 1;
        final int position3 = (position * 3) + 2;
        if ((position * 3) < mRecipes.size()) {
            recipe1 = mRecipes.get(position1);
        }
        if (((position * 3) + 1) < mRecipes.size()) {
            recipe2 = mRecipes.get(position2);
        }
        if (((position * 3) + 2) < mRecipes.size()) {
            recipe3 = mRecipes.get(position3);
        }

        switch(holder.getItemViewType()) {
            case 0:
                LeftRecipeViewHolder leftRecipeViewHolder = (LeftRecipeViewHolder) holder;
                if (position1 < mRecipes.size()) {
                    leftRecipeViewHolder.recipe_name1.setText(recipe1.getName());
                    GlideApp.with(context)
                            .load(recipe1.getImage())
                            .fitCenter()
                            .into(leftRecipeViewHolder.recipe_image1);
                }
                if (position2 < mRecipes.size()) {
                    leftRecipeViewHolder.recipe_name2.setText(recipe2.getName());
                    GlideApp.with(context)
                            .load(recipe2.getImage())
                            .fitCenter()
                            .into(leftRecipeViewHolder.recipe_image2);
                }
                if (position3 < mRecipes.size()) {
                    leftRecipeViewHolder.recipe_name3.setText(recipe3.getName());
                    GlideApp.with(context)
                            .load(recipe3.getImage())
                            .fitCenter()
                            .into(leftRecipeViewHolder.recipe_image3);
                }
                leftRecipeViewHolder.recipe_image1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // make sure the position is valid, i.e. actually exists in the view
                        if (position != RecyclerView.NO_POSITION) {
                            // get the recipe at the position, this won't work if the class is static
                            Recipe recipe1 = mRecipes.get(position1);
                            ((MainActivity) context).openDetails(recipe1); // similar to Intent, going through Activity to get to new fragment
                        }
                    }
                });
                leftRecipeViewHolder.recipe_image2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // make sure the position is valid, i.e. actually exists in the view
                        if (position != RecyclerView.NO_POSITION) {
                            // get the recipe at the position, this won't work if the class is static
                            Recipe recipe2 = mRecipes.get(position2);
                            ((MainActivity) context).openDetails(recipe2); // similar to Intent, going through Activity to get to new fragment
                        }
                    }
                });
                leftRecipeViewHolder.recipe_image3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // make sure the position is valid, i.e. actually exists in the view
                        if (position != RecyclerView.NO_POSITION) {
                            // get the recipe at the position, this won't work if the class is static
                            Recipe recipe3 = mRecipes.get(position3);
                            ((MainActivity) context).openDetails(recipe3); // similar to Intent, going through Activity to get to new fragment
                        }
                    }
                });
                break;

            case 1:
                RightRecipeViewHolder rightRecipeViewHolder = (RightRecipeViewHolder) holder;
                if (position1 < mRecipes.size()) {
                    rightRecipeViewHolder.recipe_name1.setText(recipe1.getName());
                    GlideApp.with(context)
                            .load(recipe1.getImage())
                            .fitCenter()
                            .into(rightRecipeViewHolder.recipe_image1);
                }
                if (position2 < mRecipes.size()) {
                    rightRecipeViewHolder.recipe_name2.setText(recipe2.getName());
                    GlideApp.with(context)
                            .load(recipe2.getImage())
                            .fitCenter()
                            .into(rightRecipeViewHolder.recipe_image2);
                }
                if (position3 < mRecipes.size()) {
                    rightRecipeViewHolder.recipe_name3.setText(recipe3.getName());
                    GlideApp.with(context)
                            .load(recipe3.getImage())
                            .fitCenter()
                            .into(rightRecipeViewHolder.recipe_image3);
                }
                rightRecipeViewHolder.recipe_image1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // make sure the position is valid, i.e. actually exists in the view
                        if (position != RecyclerView.NO_POSITION) {
                            // get the recipe at the position, this won't work if the class is static
                            Recipe recipe1 = mRecipes.get(position1);
                            ((MainActivity) context).openDetails(recipe1); // similar to Intent, going through Activity to get to new fragment
                        }
                    }
                });
                rightRecipeViewHolder.recipe_image2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // make sure the position is valid, i.e. actually exists in the view
                        if (position != RecyclerView.NO_POSITION) {
                            // get the recipe at the position, this won't work if the class is static
                            Recipe recipe2 = mRecipes.get(position2);
                            ((MainActivity) context).openDetails(recipe2); // similar to Intent, going through Activity to get to new fragment
                        }
                    }
                });
                rightRecipeViewHolder.recipe_image3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // make sure the position is valid, i.e. actually exists in the view
                        if (position != RecyclerView.NO_POSITION) {
                            // get the recipe at the position, this won't work if the class is static
                            Recipe recipe3 = mRecipes.get(position3);
                            ((MainActivity) context).openDetails(recipe3); // similar to Intent, going through Activity to get to new fragment
                        }
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return (mRecipes.size() / 3);
    }

    @Override
    public int getItemViewType(int position) {
        if ((position % 2) == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public class LeftRecipeViewHolder extends RecyclerView.ViewHolder{

        public ImageView recipe_image1;
        public TextView recipe_name1;
        public ImageView recipe_image2;
        public TextView recipe_name2;
        public ImageView recipe_image3;
        public TextView recipe_name3;

        public LeftRecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            recipe_image1 = (ImageView) itemView.findViewById(R.id.ivImage1);
            recipe_name1 = (TextView) itemView.findViewById(R.id.tvName1);
            recipe_image2 = (ImageView) itemView.findViewById(R.id.ivImage2);
            recipe_name2 = (TextView) itemView.findViewById(R.id.tvName2);
            recipe_image3 = (ImageView) itemView.findViewById(R.id.ivImage3);
            recipe_name3 = (TextView) itemView.findViewById(R.id.tvName3);
        }
    }

    public class RightRecipeViewHolder extends RecyclerView.ViewHolder{

        public ImageView recipe_image1;
        public TextView recipe_name1;
        public ImageView recipe_image2;
        public TextView recipe_name2;
        public ImageView recipe_image3;
        public TextView recipe_name3;

        public RightRecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            recipe_image1 = (ImageView) itemView.findViewById(R.id.ivImage1);
            recipe_name1 = (TextView) itemView.findViewById(R.id.tvName1);
            recipe_image2 = (ImageView) itemView.findViewById(R.id.ivImage2);
            recipe_name2 = (TextView) itemView.findViewById(R.id.tvName2);
            recipe_image3 = (ImageView) itemView.findViewById(R.id.ivImage3);
            recipe_name3 = (TextView) itemView.findViewById(R.id.tvName3);
        }
    }

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
}
