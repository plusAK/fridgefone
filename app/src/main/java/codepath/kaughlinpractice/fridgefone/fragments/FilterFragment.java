package codepath.kaughlinpractice.fridgefone.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.HashMap;

import codepath.kaughlinpractice.fridgefone.MainActivity;
import codepath.kaughlinpractice.fridgefone.R;
import codepath.kaughlinpractice.fridgefone.model.Recipe;

public class FilterFragment extends DialogFragment {

    private Button mSaveButton;
    private HashMap<String, Boolean> user_dict;
    private Context mContext;
    private String currentFilters = "";

    private CheckBox VegetarianCheck;
    private CheckBox VeganCheck;
    private CheckBox GlutenFreeCheck;
    private CheckBox DairyFreeCheck;
    private CheckBox VeryHealthyCheck;
    private CheckBox VeryPopularCheck;
    private CheckBox CheapCheck;
    private CheckBox FastCheck;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();
        user_dict = new HashMap<>();
        mSaveButton = (Button) view.findViewById(R.id.saveButton);
        VegetarianCheck = (CheckBox) view.findViewById(R.id.VegetarianCheck);
        VeganCheck = (CheckBox) view.findViewById(R.id.VeganCheck);
        GlutenFreeCheck = (CheckBox) view.findViewById(R.id.GlutenFreeCheck);
        DairyFreeCheck = (CheckBox) view.findViewById(R.id.DairyFreeCheck);
        VeryHealthyCheck = (CheckBox) view.findViewById(R.id.VeryHealthyCheck);
        VeryPopularCheck = (CheckBox) view.findViewById(R.id.VeryPopularCheck);
        CheapCheck = (CheckBox) view.findViewById(R.id.CheapCheck);
        FastCheck = (CheckBox) view.findViewById(R.id.FastCheck);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<CheckBox> checkBoxes = new ArrayList<>();

                checkBoxes.add(VegetarianCheck);
                checkBoxes.add(VeganCheck);
                checkBoxes.add(GlutenFreeCheck);
                checkBoxes.add(DairyFreeCheck);
                checkBoxes.add(VeryHealthyCheck);
                checkBoxes.add(VeryPopularCheck);
                checkBoxes.add(CheapCheck);
                checkBoxes.add(FastCheck);

                // TODO - Account for the FastCheck
                for(int i = 0; i < checkBoxes.size() - 1; i++) {
                    user_dict.put(Recipe.recipe_traits[i], checkBoxes.get(i).isChecked());
                }

                for(int i = 0; i < checkBoxes.size(); i++) {
                    if (checkBoxes.get(i).isChecked()) {
                        currentFilters += checkBoxes.get(i).getText().toString() + ", ";
                    }
                }
                currentFilters = currentFilters.substring(0, currentFilters.length() - 2);

                dismiss();
                ((MainActivity) mContext).generateRecipes(user_dict, currentFilters);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
