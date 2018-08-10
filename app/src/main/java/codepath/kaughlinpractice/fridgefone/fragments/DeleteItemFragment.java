package codepath.kaughlinpractice.fridgefone.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import codepath.kaughlinpractice.fridgefone.MainActivity;
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

        mDeleteItemButton.setOnClickListener(new View.OnClickListener() {
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
                        ((MainActivity) getContext()).deleteItemFromFridge(mItem);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        });
    }
}
