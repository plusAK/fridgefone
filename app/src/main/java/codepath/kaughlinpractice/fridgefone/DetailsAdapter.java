package codepath.kaughlinpractice.fridgefone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

public class DetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // list of strings on details page
    ArrayList<String> mDetails;
    // number of ingredients
    int mNumIngredients;
    // context for rendering
    Context mContext;
    // Save HashSet of ingredients you have (get from Singleton class)
    HashSet<String> mAllItemNamesSet;

    final static int HEADER_VIEW = 0;
    final static int INGREDIENT_VIEW = 1;
    final static int INSTRUCTION_VIEW = 2;


    // initialize with list
    public DetailsAdapter(ArrayList<String> details, int numIngredients) {
        this.mDetails = details;
        this.mNumIngredients = numIngredients;
        this.mAllItemNamesSet = Singleton.getSingletonInstance().getmAllItemNamesSet();
    }

    // creates and inflates a new view
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get the context and create the inflater
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        switch(viewType) {
            case HEADER_VIEW:
                // create the view using the recipe_item layout
                View headerView = inflater.inflate(R.layout.details_header_item, parent, false);
                // return a new ViewHolder
                return new HeaderViewHolder(headerView);
            case INGREDIENT_VIEW:
                // create the view using the recipe_item layout
                View ingredientView = inflater.inflate(R.layout.ingredient_item, parent, false);
                // return a new ViewHolder
                return new IngredientViewHolder(ingredientView);
            case INSTRUCTION_VIEW:
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
            case HEADER_VIEW:
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                // populate the view with the correct header title
                if (position == 0) {
                    headerViewHolder.tvHeader.setText(R.string.ingredients);
                } else {
                    headerViewHolder.tvHeader.setText(R.string.instructions);
                }
                break;
            case INGREDIENT_VIEW:
                final IngredientViewHolder ingredientViewHolder = (IngredientViewHolder) holder;
                // get the ingredient name at the specified position
                final String ingredient = mDetails.get(position);
                // populate the view with the ingredient's name
                if (ingredientViewHolder.tvIngredient != null) {
                    ingredientViewHolder.tvIngredient.setText(ingredient);
                }

                // if you have the ingredient in your fridge, change icon to green check box
                if (mAllItemNamesSet.contains(ingredient)) {
                    ingredientViewHolder.ivIngredientIcon.setImageResource(R.drawable.green_checked);
                }

                if (position % 2 == 1) {
                    ingredientViewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.alternating_gray));
                }

                ingredientViewHolder.ivIngredientIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // add to shopping list if user clicks on plus
                        // take out from shopping list if user clicks again
                        if(!ingredientViewHolder.containsIngredient) {
                            ingredientViewHolder.addedToShoppingList = !ingredientViewHolder.addedToShoppingList;
                            if (ingredientViewHolder.addedToShoppingList) {
                                ingredientViewHolder.ivIngredientIcon.setImageResource(R.drawable.grayed_plus);
                                String add_shopping_list = mContext.getString(R.string.add_shopping_list_toast);
                                Toast.makeText(mContext,ingredient + " " + add_shopping_list , Toast.LENGTH_SHORT).show();
                            } else {
                                ingredientViewHolder.ivIngredientIcon.setImageResource(R.drawable.white_plus);
                                String remove_shopping_list = mContext.getString(R.string.remove_shopping_list_toast);
                                Toast.makeText(mContext, ingredient + " " + remove_shopping_list, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                break;
            case INSTRUCTION_VIEW:
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
                    instructionViewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.alternating_gray));
                    break;

                }
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
            return HEADER_VIEW;
        } else if (position > 0 && position <= mNumIngredients) {
            return INGREDIENT_VIEW;
        } else {
            return INSTRUCTION_VIEW;
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
        ImageView ivIngredientIcon;
        boolean containsIngredient;
        boolean addedToShoppingList;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            // lookup view objects by id
            tvIngredient = (TextView) itemView.findViewById(R.id.tvIngredient);
            ivIngredientIcon = (ImageView) itemView.findViewById(R.id.ivIngredientIcon);
            containsIngredient = false;
            addedToShoppingList = false;
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
