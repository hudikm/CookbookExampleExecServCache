package fri.uniza.sk.cookbookexample;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.LruCache;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import fri.uniza.sk.cookbookexample.model.Recipe;
import fri.uniza.sk.cookbookexample.model.Singleton;
import fri.uniza.sk.cookbookexample.utilities.LoadData;

public class MainActivity extends AppCompatActivity implements RecipeFragment.OnListFragmentInteractionListener {

    private View detailFragmentContainer = null;
    private DetailFragment detailFragmentl = null;
    private int curentPosition;
    private LruCache<String, Bitmap> mMemoryCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            //Load only on application start
            if (Singleton.getInstance().getRecipes() == null) {
                InputStream openFile = getAssets().open("recepty.json");
                List<Recipe> recipes = LoadData.loadRecepies(openFile);
                Singleton.getInstance().setRecipes(recipes);
                Log.d("CookBook", String.valueOf(recipes.size()));
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("CookBook", "Data read error");
        }

        detailFragmentContainer = findViewById(R.id.detailFragment);


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.listFrameLayout, RecipeFragment.newInstance());
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        Fragment detailFragment = getSupportFragmentManager().findFragmentByTag("detailFragment");
        if (savedInstanceState != null && (detailFragmentContainer != null || detailFragment != null)) {
            //
            getSupportFragmentManager().popBackStack("detailFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            int position = savedInstanceState.getInt("detailPosition");
            createFragment(position);
        }

        if (getSupportFragmentManager().findFragmentByTag("retainFragment") == null) {
            getSupportFragmentManager().beginTransaction().add(RetainFragment.getInstance(), "retainFragment").commit();

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("detailPosition", curentPosition);
    }

    @Override
    public void onListFragmentInteraction(Recipe item, int position) {

        createFragment(position);


    }

    private void createFragment(int position) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DetailFragment detailFragment = DetailFragment.newInstance(position);
        curentPosition = position;
        if (detailFragmentContainer == null) {

            fragmentTransaction.replace(R.id.listFrameLayout, detailFragment, "detailFragment");
            fragmentTransaction.addToBackStack("detailFragment");
            fragmentTransaction.commit();

        } else {

            fragmentTransaction.replace(R.id.detailFragment, detailFragment, "detailFragment");
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

    }
}
