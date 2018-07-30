package codepath.kaughlinpractice.fridgefone;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    // list of ingredients
    ArrayList<String> ingredients;
    // context for rendering
    Context context;

    // initialize with list
    public IngredientAdapter(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    // creates and inflates a new view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get the context and create the inflater
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // create the view using the recipe_item layout
        View ingredientView = inflater.inflate(R.layout.ingredient_item, parent, false);
        // return a new ViewHolder
        return new ViewHolder(ingredientView);
    }

    // binds an inflated view to a new item
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get the ingredient name at the specified position
        String ingredient = ingredients.get(position);
        // populate the view with the ingredient's name
        if (holder.tvIngredient != null) {
            Log.d("IngredientAdapter", "Not Null");
            holder.tvIngredient.setText(ingredient);
        }

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#f2f5f9"));
        }
    }

    // returns the total number of items in the list
    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    // create the view holder as a static inner class
    public class ViewHolder extends RecyclerView.ViewHolder {

        // track view objects
        TextView tvIngredient;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // lookup view objects by id
            tvIngredient = (TextView) itemView.findViewById(R.id.tvIngredient);
        }
    }
}
