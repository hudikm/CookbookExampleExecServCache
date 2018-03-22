package fri.uniza.sk.cookbookexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.Callable;

import fri.uniza.sk.cookbookexample.model.Recipe;

/**
 * Created by hudik1 on 21. 3. 2018.
 */

public class DownloadTask implements Runnable {

    MyRecepyRecyclerViewAdapter.ViewHolder viewHolder;
    int position;
    Context appContext;
    Recipe recipe;
    MyRecepyRecyclerViewAdapter myRecepyRecyclerViewAdapter;


    public DownloadTask(MyRecepyRecyclerViewAdapter.ViewHolder viewHolder, int position, Context appContext) {
        this.viewHolder = viewHolder;
        this.recipe = viewHolder.mItem;
        this.position = position;
        this.appContext = appContext;

    }

    @Override
    public void run() {
        //Load image and change image on UI thread
        final Bitmap bitmapCacheFinal;
        try {


                bitmapCacheFinal = recipe.getBitmapFromAsset(appContext);
                RetainFragment.getInstance().addBitmapCache(recipe.imageUrl, bitmapCacheFinal);
                setImage(bitmapCacheFinal);

        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.i("DownloadTask", "Interrupt");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("DownloadTask", "Interrupt download");
        }
    }

    //Set new bitmap to imageView on UI thread
    public void setImage(final Bitmap bitmapCacheFinal) {
        synchronized (viewHolder) {
            if (viewHolder.position == position) {
                viewHolder.mImageView.post(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder.mImageView.setImageBitmap(bitmapCacheFinal);
                    }
                });
            }else{

            }
        }
    }

    public Bitmap getBitmapFromCache() {
        Bitmap bitmapCache = RetainFragment.getInstance().getBitmapCache(String.valueOf(this.viewHolder.mItem.imageUrl));
        return bitmapCache;
    }
}
