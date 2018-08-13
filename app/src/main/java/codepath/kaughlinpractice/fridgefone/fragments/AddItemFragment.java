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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import codepath.kaughlinpractice.fridgefone.AddItemAdapter;
import codepath.kaughlinpractice.fridgefone.FridgeClient;
import codepath.kaughlinpractice.fridgefone.MainActivity;
import codepath.kaughlinpractice.fridgefone.R;
import codepath.kaughlinpractice.fridgefone.model.Item;
import cz.msebera.android.httpclient.Header;

public class AddItemFragment extends DialogFragment {

    private Button mAddButton;
    private AutoCompleteTextView mFoodItemAutoCompleteTextView;
    public FridgeClient mClient;
    public AddItemAdapter mAddItemAdapter;

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
        mAddItemAdapter = new AddItemAdapter(getActivity(), R.layout.add_item);

        mAddButton = (Button) view.findViewById(R.id.btnAdd);
        Button cancelButton = (Button) view.findViewById(R.id.btnCancel);
        mFoodItemAutoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.actvFoodItem);
        mFoodItemAutoCompleteTextView.setAdapter(mAddItemAdapter);
        mFoodItemAutoCompleteTextView.setThreshold(1);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scaledown);
                view.startAnimation(anim);
                anim.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        String foodItem = mFoodItemAutoCompleteTextView.getText().toString();
                        Toast.makeText(getActivity(), "Adding: " + foodItem, Toast.LENGTH_LONG).show();
                        ((MainActivity) getContext()).getItem(foodItem);
                        dismiss();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scaledown);
                view.startAnimation(anim);
                anim.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        dismiss();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
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
                        mAddItemAdapter.clear();
                        for (int i = 0; i < response.length(); i++) {
                            Item item = Item.autoFromJSON(response.getJSONObject(i), mAddItemAdapter);
                            mAddItemAdapter.notifyDataSetChanged();
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
                mAddItemAdapter.clear();
                // Sending to the Auto Complete List
                for (int i = 0; i < response.length(); i++) {
                    Item item = Item.autoFromJSON(response.getJSONObject(i), mAddItemAdapter);
                    mAddItemAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                Log.d("MainActivity", "Error: " + e.getMessage());
            }
        }
    }
}
