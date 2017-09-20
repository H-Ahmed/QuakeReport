package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Hesham on 9/19/2017.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>>{

    /** Tag for log messages */
    private static final String LOG_TAG = EarthquakeLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link EarthquakeLoader}
     * @param context of the activity
     * @param url to load data from
     */
    public EarthquakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     * @return
     */
    @Override
    public List<Earthquake> loadInBackground() {
        // Don't perform the request i there are no URLs, or the first URL is null
        if( mUrl == null){
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Earthquake> result = QueryUtils.fetchEarthquakeData(mUrl);
        return result;
    }
}
