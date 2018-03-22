package fri.uniza.sk.cookbookexample;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class RetainFragment extends Fragment {


    private static RetainFragment instance;

    private LruCache<String, Bitmap> mMemoryCache;
    private LinkedBlockingQueue<Runnable> mDownloadQueue;
    private ExecutorService executorService;

    public static RetainFragment getInstance() {
        if (instance == null)
            instance = new RetainFragment();
        return instance;
    }

    public static void setInstance(RetainFragment instance) {
        RetainFragment.instance = instance;
    }

    public boolean addBitmapCache(String key, Bitmap bitmap) {
        synchronized (mMemoryCache) {
            if (mMemoryCache.get(key) == null) {
                mMemoryCache.put(key, bitmap);
            }
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

        // Instantiates the queue of Runnables as a LinkedBlockingQueue
        mDownloadQueue = new LinkedBlockingQueue<Runnable>();

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
        createExecutorService();


    }

    private void createExecutorService() {
        //Create new executorService with param:
        // max num of threads = Runtime.getRuntime().availableProcessors()
        // threadFactory -> used for setting thread's priority
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactory() {
            public int threadPriority = Process.THREAD_PRIORITY_BACKGROUND;

            @Override
            public Thread newThread(@NonNull Runnable runnable) {
                Thread thread = new Thread(runnable);
                thread.setPriority(threadPriority);
                return thread;
            }
        });

    }

    void addNewDownloadTask(DownloadTask downloadTask) {

        if (executorService.isShutdown())
            //If executorService was previously shutdown then start new execServis
            createExecutorService();
        //Execute new downloadTask on executor
        executorService.execute(downloadTask);

    }

    void cancelDownloads() {
        executorService.shutdown();
        try {
            if (executorService.awaitTermination(1, TimeUnit.SECONDS)) {
                Log.i("DownloadTask", "task completed");
            } else {
                Log.i("DownloadTask", "Forcing shutdown...");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //Stop all pending downloads
    }
}
