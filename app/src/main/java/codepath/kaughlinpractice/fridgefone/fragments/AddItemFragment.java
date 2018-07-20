package codepath.kaughlinpractice.fridgefone.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import codepath.kaughlinpractice.fridgefone.MainActivity;
import codepath.kaughlinpractice.fridgefone.R;

public class AddItemFragment extends DialogFragment {

    private Button addButton;
    private AutoCompleteTextView actvFoodItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, items);

        addButton = (Button) view.findViewById(R.id.btnAdd);
        actvFoodItem = (AutoCompleteTextView) view.findViewById(R.id.actvFoodItem);
        actvFoodItem.setAdapter(adapter);
        actvFoodItem.setThreshold(1);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String foodItem = actvFoodItem.getText().toString();
                Log.d("AddItemFragment", "Adding: " + foodItem);
                Toast.makeText(getActivity(), "Adding: " + foodItem, Toast.LENGTH_LONG).show();
                ((MainActivity) getContext()).addFoodItem(foodItem);
                ((MainActivity) getContext()).getItem(foodItem);
                dismiss();
            }
        });
    }
    private static final String[] items = new String[]{"Apple", "Banana"};
}
