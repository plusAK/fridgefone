package codepath.kaughlinpractice.fridgefone.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import codepath.kaughlinpractice.fridgefone.FridgeClient;
import codepath.kaughlinpractice.fridgefone.MainActivity;
import codepath.kaughlinpractice.fridgefone.R;
import cz.msebera.android.httpclient.Header;

public class AddItemFragment extends DialogFragment {



    private Button mAddButton;
    private AutoCompleteTextView mFoodItemAutoCompleteTextView;
    public FridgeClient mClient;
    public ArrayList<String> autoCompleteItems = new ArrayList<String>();
    public ArrayAdapter<String> addItemAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //intialize client
        mClient = new FridgeClient(getActivity());
        addItemAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, autoCompleteItems);

        mAddButton = (Button) view.findViewById(R.id.btnAdd);
        Button cancelButton = (Button) view.findViewById(R.id.btnCancel);
        mFoodItemAutoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.actvFoodItem);
        mFoodItemAutoCompleteTextView.setAdapter(addItemAdapter);
        mFoodItemAutoCompleteTextView.setThreshold(1);
        //mFoodItemAutoCompleteTextView.setDropDownHeight(3);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String foodItem = mFoodItemAutoCompleteTextView.getText().toString();
                Toast.makeText(getActivity(), "Adding: " + foodItem, Toast.LENGTH_LONG).show();
                ((MainActivity) getContext()).getItem(foodItem);
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mFoodItemAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                getItemForAutoComplete(editable.toString());
            }
        });
    }

    public void getItemForAutoComplete(String foodItem) {
        if (FridgeClient.mUseAutocompleteAPI) {
            // execute a GET request expecting a JSON object response
            mClient.getAutoComplete(foodItem, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            String name = response.getJSONObject(i).getString("name");
                            autoCompleteItems.add(name);
                            mFoodItemAutoCompleteTextView.setAdapter(new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_dropdown_item_1line, autoCompleteItems));
                            mFoodItemAutoCompleteTextView.showDropDown();
                        }
                    } catch (JSONException e) {
                        Log.d("MainActivity", "Error: " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    Log.d("MainActivity", "Error: " + throwable);
                }
            });
        } else {
            String stringResponse = "[\n" +
                    "  {\n" +
                    "    \"name\": \"apple\",\n" +
                    "    \"image\": \"apple.jpg\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"name\": \"apples\",\n" +
                    "    \"image\": \"apple.jpg\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"name\": \"almond\",\n" +
                    "    \"image\": \"apple.jpg\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"name\": \"avocado\",\n" +
                    "    \"image\": \"apple.jpg\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"name\": \"arugula\",\n" +
                    "    \"image\": \"apple.jpg\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"name\": \"beef\",\n" +
                    "    \"image\": \"apple.jpg\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"name\": \"bacon\",\n" +
                    "    \"image\": \"apple.jpg\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"name\": \"bread\",\n" +
                    "    \"image\": \"apple.jpg\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"name\": \"cucumber\",\n" +
                    "    \"image\": \"apple.jpg\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"name\": \"cabbage\",\n" +
                    "    \"image\": \"apple.jpg\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"name\": \"applesauce\",\n" +
                    "    \"image\": \"applesauce.jpg\"\n" +
                    "  }\n" +
                    "]";
            JSONArray response = null;
            try {
                response = new JSONArray(stringResponse);
                // Sending to the Auto Complete List
                for (int i = 0; i < response.length(); i++) {
                    String name = response.getJSONObject(i).getString("name");
                    autoCompleteItems.add(name);
                    addItemAdapter.notifyDataSetChanged();
                }
                mFoodItemAutoCompleteTextView.showDropDown();
            } catch (JSONException e) {
                Log.d("MainActivity", "Error: " + e.getMessage());
            }
        }
    }
}
