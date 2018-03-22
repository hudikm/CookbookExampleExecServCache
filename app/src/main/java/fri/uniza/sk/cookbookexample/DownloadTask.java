package fri.uniza.sk.cookbookexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

/**
 * Created by hudik1 on 21. 3. 2018.
 */

public class DownloadTask extends AsyncTask<Void, Void, Bitmap> {

    MyRecepyRecyclerViewAdapter.ViewHolder viewHolder;
    int position;
    Context appContext;

    public DownloadTask(MyRecepyRecyclerViewAdapter.ViewHolder viewHolder, int position, Context appContext) {
        this.viewHolder = viewHolder;
        this.position = position;
        this.appContext = appContext;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        Bitmap bitmapCache = RetainFragment.getInstance().getBitmapCache(String.valueOf(this.viewHolder.mItem.hashCode()));
        if (bitmapCache == null) {
            bitmapCache = viewHolder.mItem.getBitmapFromAsset(appContext);
            RetainFragment.getInstance().addBitmapCache(String.valueOf(this.viewHolder.mItem.hashCode()), bitmapCache);
        }
        return bitmapCache;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (viewHolder.position == position) {
            viewHolder.mImageView.setImageBitmap(bitmap);
        }
    }
}
