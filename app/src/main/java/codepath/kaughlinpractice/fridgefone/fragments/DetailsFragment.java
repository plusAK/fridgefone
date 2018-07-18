package codepath.kaughlinpractice.fridgefone.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import codepath.kaughlinpractice.fridgefone.MainActivity;
import codepath.kaughlinpractice.fridgefone.R;
import codepath.kaughlinpractice.fridgefone.model.Recipe;


public class DetailsFragment extends Fragment {

    public Recipe recipe;
    /*
    @BindView(R.id.ivRecipeImage) public ImageView ivRecipeImage;
    @BindView(R.id.tvDishTitle) public TextView tvDishTitle;
    @BindView(R.id.tvIngredients) public TextView tvIngredients;
    @BindView(R.id.tvInstructions) public TextView tvInstructions;
    @BindView(R.id.buttonBack) public Button buttonBack;
    */
    public TextView tvDishTitle;
    public Button buttonBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ButterKnife.bind(getActivity());
        // TODO -- fix ButterKnife to work here
        tvDishTitle = view.findViewById(R.id.tvDishTitle);
        buttonBack = view.findViewById(R.id.buttonBack);


        //recipe = (Recipe) Parcels.unwrap(getIntent().getParcelableExtra(Recipe.class.getSimpleName()));
        Bundle args = getArguments();
        String name = args.getString("name");
        tvDishTitle.setText(name);
        //TODO change when bundle recieves object id

        //  TODO: Add the image url into load() once we get it from API
//        GlideApp.with(this)
//                .load()
//                .into(ivRecipeImage);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getContext()).generateRecipe(); //basiacally intent to go back to recipe list screen
            }
        });

    }
}
