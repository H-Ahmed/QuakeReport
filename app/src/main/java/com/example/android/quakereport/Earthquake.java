package com.example.android.quakereport;

import java.net.URL;
import java.util.Date;

/**
 * Created by Hesham on 9/16/2017.
 */

public class Earthquake {

    /** Magnitude of earthquake */
    private Double mMagnitude;

    /** Location of earthquake */
    private String mLocation;

    /** Time of earthquake */
    private Long  mTimeInMilliseconds;

    /** Website URL of the earthquake*/
    private String mUrl;

    /**
     * Constructor a new {@link Earthquake} object
     *
     * @param magnitude is the magnitude (size) of the earthquake
     * @param location is the city location  of the earthquake
     * @param timeInMilliseconds is the time in milliseconds (from the Epoch) when the
     *  earthquake happened
     * @param url is the webdite URL to find more details about the earthquake
     */
    public Earthquake(Double magnitude, String location, Long timeInMilliseconds, String url){

        mMagnitude = magnitude;
        mLocation = location;
        mTimeInMilliseconds = timeInMilliseconds;
        mUrl = url;

    }

    /**
     *
     * @return the magnitude of the earthquake
     */
    public Double getmMagnitude() {
        return mMagnitude;
    }

    /**
     *
     * @return the location of the earthquake
     */
    public String getmLocation() {
        return mLocation;
    }

    /**
     *
     * @return the Time of the earthquake
     */
    public Long getmTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

    /**
     *
     * @return the website URL to find more information about the earthquake
     */
    public String getmUrl() {
        return mUrl;
    }
}
