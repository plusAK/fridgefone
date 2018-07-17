package codepath.kaughlinpractice.fridgefone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.kaughlinpractice.fridgefone.model.Recipe;

public class RecipeDetailsActivity extends AppCompatActivity {

    public Recipe recipe;
    @BindView(R.id.ivRecipeImage) public ImageView ivRecipeImage;
    @BindView(R.id.tvDishTitle) public TextView tvDishTitle;
    @BindView(R.id.tvIngredients) public TextView tvIngredients;
    @BindView(R.id.tvInstructions) public TextView tvInstructions;
    @BindView(R.id.buttonBack) public Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);

        recipe = (Recipe) Parcels.unwrap(getIntent().getParcelableExtra(Recipe.class.getSimpleName()));

//  TODO: Add the image url into load() once we get it from API
//        GlideApp.with(this)
//                .load()
//                .into(ivRecipeImage);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeDetailsActivity.this, RecipeListActivity.class);
                startActivity(intent);
            }
        });
    }
}
