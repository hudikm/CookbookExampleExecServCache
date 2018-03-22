package fri.uniza.sk.cookbookexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import fri.uniza.sk.cookbookexample.RecipeFragment.OnListFragmentInteractionListener;
import fri.uniza.sk.cookbookexample.model.Recipe;

import java.util.List;


public class MyRecepyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecepyRecyclerViewAdapter.ViewHolder> {

    private final List<Recipe> recipes;
    private final OnListFragmentInteractionListener mListener;
    private Context context;

    public MyRecepyRecyclerViewAdapter(List<Recipe> items, OnListFragmentInteractionListener listener) {
        recipes = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = recipes.get(position);
        synchronized (holder) {
            holder.position = position;

            //Create new DownloadTask
            DownloadTask downloadTask = new DownloadTask(holder, position, context);

            //If possible get bitmapFromCache
            Bitmap bitmapFromCache = downloadTask.getBitmapFromCache();

            //Check if bitmap was found in cache
            if (bitmapFromCache != null)
                holder.mImageView.setImageBitmap(bitmapFromCache);
            else {
                holder.mImageView.setImageBitmap(Recipe.getBitmapOfSpinner(context));
                //Image was not found in cache -> Download image
                RetainFragment.getInstance().addNewDownloadTask(downloadTask);
            }

            holder.mContentView.setText(recipes.get(position).title);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListFragmentInteraction(holder.mItem, position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mContentView;
        public Recipe mItem;
        public int position;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.smallImage);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
