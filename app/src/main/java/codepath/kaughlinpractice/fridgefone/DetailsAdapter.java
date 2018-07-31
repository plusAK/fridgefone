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

public class DetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // list of strings on details page
    ArrayList<String> mDetails;
    // number of ingredients
    int mNumIngredients;
    // context for rendering
    Context mContext;
    // Singleton class used to get access to selected ingredients


    // initialize with list
    public DetailsAdapter(ArrayList<String> details, int numIngredients) {
        this.mDetails = details;
        this.mNumIngredients = numIngredients;
    }

    // creates and inflates a new view
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // TODO -- given the viewType I can distinguish between instructions and ingredients
        // get the context and create the inflater
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        switch(viewType) {
            case 0:
                // create the view using the recipe_item layout
                View headerView = inflater.inflate(R.layout.details_header_item, parent, false);
                // return a new ViewHolder
                return new HeaderViewHolder(headerView);
            case 1:
                // create the view using the recipe_item layout
                View ingredientView = inflater.inflate(R.layout.ingredient_item, parent, false);
                // return a new ViewHolder
                return new IngredientViewHolder(ingredientView);
            case 2:
                // create the view using the recipe_item layout
                View instructionView = inflater.inflate(R.layout.instructions_item, parent, false);
                // return a new ViewHolder
                return new InstructionViewHolder(instructionView);
            default:
                return null;
        }
    }

    // binds an inflated view to a new item
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case 0:
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                // populate the view with the correct header title
                if (position == 0) {
                    headerViewHolder.tvHeader.setText("Ingredients");
                } else {
                    headerViewHolder.tvHeader.setText("Instructions");
                }
                break;
            case 1:
                IngredientViewHolder ingredientViewHolder = (IngredientViewHolder) holder;
                // get the ingredient name at the specified position
                String ingredient = mDetails.get(position);
                // populate the view with the ingredient's name
                if (ingredientViewHolder.tvIngredient != null) {
                    Log.d("IngredientAdapter", "Not Null");
                    ingredientViewHolder.tvIngredient.setText(ingredient);
                }

                if (position % 2 == 1) {
                    ingredientViewHolder.itemView.setBackgroundColor(Color.parseColor("#f2f5f9"));
                }
                break;
            case 2:
                InstructionViewHolder instructionViewHolder = (InstructionViewHolder) holder;
                // get the instruction step at the specified position
                String instruction = mDetails.get(position);
                // populate the view with the instruction step
                if (instructionViewHolder.tvInstruction != null) {
                    instructionViewHolder.tvInstruction.setText(instruction);
                }
                if (instructionViewHolder.tvStepCount != null) {
                    int stepNum = position - mNumIngredients - 1;
                    instructionViewHolder.tvStepCount.setText(stepNum + "");
                }

                if (position % 2 == 1) {
                    instructionViewHolder.itemView.setBackgroundColor(Color.parseColor("#f2f5f9"));
                }
                break;
        }
    }

    // returns the total number of items in the list
    @Override
    public int getItemCount() {
        return mDetails.size();
    }

    @Override
    public int getItemViewType(int position) {
        // case 0, if position corresponds to Ingredients or Instructions Text Headers
        // case 1, position corresponds to ingredient needed
        // case 2, position corresponds to instruction step
        if (position == 0 || position == mNumIngredients + 1) {
            return 0;
        } else if (position > 0 && position <= mNumIngredients) {
            return 1;
        } else {
            return 2;
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView tvHeader;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            // lookup view objects by id
            tvHeader = (TextView) itemView.findViewById(R.id.tvHeader);

        }
    }

    // create the view holder as a static inner class
    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        // track view objects
        TextView tvIngredient;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            // lookup view objects by id
            tvIngredient = (TextView) itemView.findViewById(R.id.tvIngredient);
        }
    }

    public class InstructionViewHolder extends RecyclerView.ViewHolder {

        TextView tvInstruction;
        TextView tvStepCount;

        public InstructionViewHolder(@NonNull View itemView) {
            super(itemView);
            // lookup view objects by id
            tvInstruction = (TextView) itemView.findViewById(R.id.tvInstruction);
            tvStepCount = (TextView) itemView.findViewById(R.id.tvStepCount);
        }
    }
}
