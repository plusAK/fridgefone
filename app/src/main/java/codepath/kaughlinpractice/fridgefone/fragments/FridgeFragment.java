package codepath.kaughlinpractice.fridgefone.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import codepath.kaughlinpractice.fridgefone.MainActivity;
import codepath.kaughlinpractice.fridgefone.R;

public class FridgeFragment extends Fragment{


    Context context;
    private ImageView ivGenerateRecipeList;
    private ImageView ivAddItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fridge, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();

        ivGenerateRecipeList = (ImageView) view.findViewById(R.id.ivGenerateRecipeList);
        ivAddItem = (ImageView) view.findViewById(R.id.ivAddItem);

        ivGenerateRecipeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("FridgeFragment", "clicked on generate");
                generateRecipe();
            }
        });

        ivAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddItemFragment addItemFragment = new AddItemFragment();
                addItemFragment.show(getFragmentManager(), "AddItemFragment");
            }
        });
    }


    // TODO: Change from intent to bundle
    public void generateRecipe() {
        Log.d("FridgeFragment", "should move pages");
        ((MainActivity) context).generateRecipe(); // similar to Intent, going through Activity to get to new fragment
    }

}
