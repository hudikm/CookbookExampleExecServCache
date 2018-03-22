package fri.uniza.sk.cookbookexample;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import fri.uniza.sk.cookbookexample.model.Singleton;


public class DetailFragment extends Fragment {

    private static final String ARG_PARAM1 = "recipiePos";
    private int position;
    private int newDate;

    public DetailFragment() {
        // Required empty public constructor
    }


    public static DetailFragment newInstance(int position) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, position);
        fragment.setArguments(args);
        return fragment;
    }

    public void addPositonAgrument(int position){
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, position);
        this.setArguments(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_recipe, container, false);
        initData(view);

        return view;
    }

    private void initData(View view) {
        ((ImageView) view.findViewById(R.id.recipeImage)).setImageBitmap(Singleton.getInstance().getRecipes().get(position).getBitmapFromAsset(getContext()));
        ((TextView) view.findViewById(R.id.detailRecipe)).setText(Singleton.getInstance().getRecipes().get(position).detail);
        ListView listView = (ListView) view.findViewById(R.id.ingredientsList);
        listView.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, Singleton.getInstance().getRecipes().get(position).ingredients));

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = Singleton.getInstance().getRecipes().get(position).ingredients.size() * 100;

        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
