package fri.uniza.sk.cookbookexample;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fri.uniza.sk.cookbookexample.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RetainFragment extends Fragment {

    private static RetainFragment instance;

    private LruCache<String, Bitmap> mMemoryCache;

    public static RetainFragment getInstance() {
        if (instance == null)
            instance =  new RetainFragment();
        return instance;
    }

    public static void setInstance(RetainFragment instance) {
        RetainFragment.instance = instance;
    }

    public boolean addBitmapCache(String key, Bitmap bitmap) {
        if (mMemoryCache.get(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
        return false;
    }

    public Bitmap getBitmapCache(String key) {
        return mMemoryCache.get(key);
    }

    public RetainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return null;

    }

}
