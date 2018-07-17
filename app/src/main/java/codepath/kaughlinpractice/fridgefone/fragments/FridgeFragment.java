package codepath.kaughlinpractice.fridgefone.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import codepath.kaughlinpractice.fridgefone.R;

public class FridgeFragment extends Fragment {


    private ImageView ivGenerateRecipeList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fridge, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);






        ivGenerateRecipeList = (ImageView) view.findViewById(R.id.ivGenerateRecipeList);
        ivGenerateRecipeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateRecipe();
            }
        });
    }


    // TODO: Change from intent to bundle
    public void generateRecipe(){
//        Intent i = new Intent(this, RecipeListActivity.class);
//        startActivity(i);
    }
}
