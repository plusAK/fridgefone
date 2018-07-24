package codepath.kaughlinpractice.fridgefone.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import codepath.kaughlinpractice.fridgefone.R;
import codepath.kaughlinpractice.fridgefone.model.Item;


public class DeleteItemFragment extends DialogFragment {
    private Button mDeleteItemButton;
    private Button mCancelButton;
    private Item mItem;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delete_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCancelButton = (Button) view.findViewById(R.id.btnCancel);
        mDeleteItemButton = (Button) view.findViewById(R.id.btnDeleteItem);
        Bundle args = getArguments(); // getting the bundle response
        mItem = args.getParcelable("Item");
        TextView deleteQuestionTextView = view.findViewById(R.id.tvDeleteQuestion);
        deleteQuestionTextView.setText("Are you sure you want to remove this " + mItem.getName() + " from your fridge?");

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mDeleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mItem.deleteInBackground();

                dismiss();
                Toast.makeText(getActivity(), "Deleted: " + mItem.getName(), Toast.LENGTH_LONG).show();

            }
        });
    }
}
