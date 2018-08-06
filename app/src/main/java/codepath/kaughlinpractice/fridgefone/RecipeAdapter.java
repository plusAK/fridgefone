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
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

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

        // Inflate the view and return the viewHolder based on the viewType
        switch(viewType) {
            case 0:
                View leftRecipeView = inflater.inflate(R.layout.left_recipe_item, parent, false);
                return new LeftRecipeViewHolder(leftRecipeView);
            case 1:
                View rightRecipeView = inflater.inflate(R.layout.right_recipe_item, parent, false);
                return new RightRecipeViewHolder(rightRecipeView);
            case 2:
                View oneRecipeView = inflater.inflate(R.layout.one_recipe_item, parent, false);
                return new OneRecipeViewHolder(oneRecipeView);
            case 3:
                View twoRecipeView = inflater.inflate(R.layout.two_recipe_item, parent, false);
                return new TwoRecipeViewHolder(twoRecipeView);
            default:
                return null;
        }
    }

    public void setRecipes(final int position, TextView recipe_name, ImageView recipe_image) {
            Recipe recipe = mRecipes.get(position);
            recipe_name.setText(recipe.getName());
            GlideApp.with(context)
                    .load(recipe.getImage())
                    .fitCenter()
                    .transform(new RoundedCornersTransformation(50, 0))
                    .into(recipe_image);
    }

    // position = position of ViewHolder, recipePosition = position on individual recipe
    public void setRecipeListener(TextView recipe_text, ImageView recipe_image, final int position, final int recipePosition) {
        recipe_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // make sure the position is valid, i.e. actually exists in the view
                if (position != RecyclerView.NO_POSITION) {
                    // get the recipe at the position, this won't work if the class is static
                    Recipe recipe = mRecipes.get(recipePosition);
                    ((MainActivity) context).openDetails(recipe); // similar to Intent, going through Activity to get to new fragment
                }
            }
        });
        recipe_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // make sure the position is valid, i.e. actually exists in the view
                if (position != RecyclerView.NO_POSITION) {
                    // get the recipe at the position, this won't work if the class is static
                    Recipe recipe = mRecipes.get(recipePosition);
                    ((MainActivity) context).openDetails(recipe); // similar to Intent, going through Activity to get to new fragment
                }
            }
        });
    }

    // bind the values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        final int position1 = position * 3;
        final int position2 = position1 + 1;
        final int position3 = position1 + 2;

        switch(holder.getItemViewType()) {
            case 0:
                LeftRecipeViewHolder leftRecipeViewHolder = (LeftRecipeViewHolder) holder;

                setRecipes(position1, leftRecipeViewHolder.mRecipeText1, leftRecipeViewHolder.mRecipeImage1);
                setRecipes(position2, leftRecipeViewHolder.mRecipeText2, leftRecipeViewHolder.mRecipeImage2);
                setRecipes(position3, leftRecipeViewHolder.mRecipeText3, leftRecipeViewHolder.mRecipeImage3);

                setRecipeListener(leftRecipeViewHolder.mRecipeText1, leftRecipeViewHolder.mRecipeImage1, position, position1);
                setRecipeListener(leftRecipeViewHolder.mRecipeText2, leftRecipeViewHolder.mRecipeImage2, position, position2);
                setRecipeListener(leftRecipeViewHolder.mRecipeText3, leftRecipeViewHolder.mRecipeImage3, position, position3);
                break;

            case 1:
                RightRecipeViewHolder rightRecipeViewHolder = (RightRecipeViewHolder) holder;

                setRecipes(position1, rightRecipeViewHolder.mRecipeText1, rightRecipeViewHolder.mRecipeImage1);
                setRecipes(position2, rightRecipeViewHolder.mRecipeText2, rightRecipeViewHolder.mRecipeImage2);
                setRecipes(position3, rightRecipeViewHolder.mRecipeText3, rightRecipeViewHolder.mRecipeImage3);

                setRecipeListener(rightRecipeViewHolder.mRecipeText1, rightRecipeViewHolder.mRecipeImage1, position, position1);
                setRecipeListener(rightRecipeViewHolder.mRecipeText2, rightRecipeViewHolder.mRecipeImage2, position, position2);
                setRecipeListener(rightRecipeViewHolder.mRecipeText3, rightRecipeViewHolder.mRecipeImage3, position, position3);
                break;
            case 2:
                OneRecipeViewHolder oneRecipeViewHolder = (OneRecipeViewHolder) holder;

                setRecipes(position1, oneRecipeViewHolder.mRecipeText1, oneRecipeViewHolder.mRecipeImage1);

                setRecipeListener(oneRecipeViewHolder.mRecipeText1, oneRecipeViewHolder.mRecipeImage1, position, position1);

                break;
            case 3:
                TwoRecipeViewHolder twoRecipeViewHolder = (TwoRecipeViewHolder) holder;

                setRecipes(position1, twoRecipeViewHolder.mRecipeText1, twoRecipeViewHolder.mRecipeImage1);
                setRecipes(position2, twoRecipeViewHolder.mRecipeText2, twoRecipeViewHolder.mRecipeImage2);

                setRecipeListener(twoRecipeViewHolder.mRecipeText1, twoRecipeViewHolder.mRecipeImage1, position, position1);
                setRecipeListener(twoRecipeViewHolder.mRecipeText2, twoRecipeViewHolder.mRecipeImage2, position, position2);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (mRecipes.size() % 3 == 0) {
            return (mRecipes.size() / 3);
        } else {
            return ((mRecipes.size() / 3) + 1);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // One Item Leftover
        if (((mRecipes.size() % 3) == 1) && (position == (mRecipes.size() / 3))) {
            return 2;
        // Two Items Leftover
        } else if (((mRecipes.size() % 3) == 2) && (position == (mRecipes.size() / 3))) {
            return 3;
        // Three Items (Left ViewHolder)
        } else if ((position % 2) == 0) {
            return 0;
        // Three Items (Right ViewHolder)
        } else {
            return 1;
        }
    }

    public class LeftRecipeViewHolder extends RecyclerView.ViewHolder {

        public ImageView mRecipeImage1;
        public TextView mRecipeText1;
        public ImageView mRecipeImage2;
        public TextView mRecipeText2;
        public ImageView mRecipeImage3;
        public TextView mRecipeText3;

        public LeftRecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            mRecipeImage1 = (ImageView) itemView.findViewById(R.id.ivImage1);
            mRecipeText1 = (TextView) itemView.findViewById(R.id.tvName1);
            mRecipeImage2 = (ImageView) itemView.findViewById(R.id.ivImage2);
            mRecipeText2 = (TextView) itemView.findViewById(R.id.tvName2);
            mRecipeImage3 = (ImageView) itemView.findViewById(R.id.ivImage3);
            mRecipeText3 = (TextView) itemView.findViewById(R.id.tvName3);
        }
    }

    public class RightRecipeViewHolder extends RecyclerView.ViewHolder {

        public ImageView mRecipeImage1;
        public TextView mRecipeText1;
        public ImageView mRecipeImage2;
        public TextView mRecipeText2;
        public ImageView mRecipeImage3;
        public TextView mRecipeText3;

        public RightRecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            mRecipeImage1 = (ImageView) itemView.findViewById(R.id.ivImage1);
            mRecipeText1 = (TextView) itemView.findViewById(R.id.tvName1);
            mRecipeImage2 = (ImageView) itemView.findViewById(R.id.ivImage2);
            mRecipeText2 = (TextView) itemView.findViewById(R.id.tvName2);
            mRecipeImage3 = (ImageView) itemView.findViewById(R.id.ivImage3);
            mRecipeText3 = (TextView) itemView.findViewById(R.id.tvName3);
        }
    }

    public class OneRecipeViewHolder extends RecyclerView.ViewHolder {

        public ImageView mRecipeImage1;
        public TextView mRecipeText1;

        public OneRecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            mRecipeImage1 = (ImageView) itemView.findViewById(R.id.ivImage1);
            mRecipeText1 = (TextView) itemView.findViewById(R.id.tvName1);
        }
    }

    public class TwoRecipeViewHolder extends RecyclerView.ViewHolder {

        public ImageView mRecipeImage1;
        public TextView mRecipeText1;
        public ImageView mRecipeImage2;
        public TextView mRecipeText2;

        public TwoRecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            mRecipeImage1 = (ImageView) itemView.findViewById(R.id.ivImage1);
            mRecipeText1 = (TextView) itemView.findViewById(R.id.tvName1);
            mRecipeImage2 = (ImageView) itemView.findViewById(R.id.ivImage2);
            mRecipeText2 = (TextView) itemView.findViewById(R.id.tvName2);
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
