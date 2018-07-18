package codepath.kaughlinpractice.fridgefone;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import codepath.kaughlinpractice.fridgefone.fragments.DetailsFragment;
import codepath.kaughlinpractice.fridgefone.fragments.FridgeFragment;
import codepath.kaughlinpractice.fridgefone.fragments.ListFragment;
import codepath.kaughlinpractice.fridgefone.model.Recipe;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    Context context;

    final Fragment fridgeFrag = new FridgeFragment();
    final Fragment listFrag = new ListFragment();
    final Fragment detailsFrag = new DetailsFragment();

    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView=(NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.my_fragment, fridgeFrag).commit();
    }

    // allows for you to click the menu button and pull out the navigation drawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void generateRecipe() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.my_fragment, listFrag).commit();
    }

    public void openDetails(Recipe recipe) {
        // bundle communication between activity and fradment
        Bundle args = new Bundle();
        args.putString("name", recipe.getName());
        // TODO -- will have to change to get object ID
        detailsFrag.setArguments(args);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.my_fragment, detailsFrag).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if(id == R.id.nav_Fridge){
            Toast.makeText(this, "Welcome Home", Toast.LENGTH_LONG).show();
            goToMyFridge(); // similar to Intent, going through Activity to get to new fragment


        }
        return false;
    }

    public void goToMyFridge(){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.my_fragment, fridgeFrag).commit();

    }
}
