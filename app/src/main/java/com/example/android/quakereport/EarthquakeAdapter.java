package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Hesham on 9/16/2017.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    /**
     * Constractor a new {@link EarthquakeAdapter}
     *
     * @param context of the app
     * @param earthquakes is the list of earthquakes, which is the data source of the adapter
     */

    public EarthquakeAdapter(Activity context, ArrayList<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    /**
     * Return a list item view that displays information about the earthquake at the given position
     * in the list of earthquakes.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwize, if convertView is null, then inflate a new list item layout.
        View listViewItem = convertView;
        if(listViewItem == null){
            listViewItem = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item, parent, false);
        }

        // Find the earthquake at the given position in the list of eatrhquakes
        Earthquake currentearthquake = getItem(position);

        // Find the TextView with view ID magnitude
        TextView magnitudeView  = (TextView) listViewItem.findViewById(R.id.magnitude);
        // Formatted magnitude String showing 1 decimal palce (i.e "3.2")
        DecimalFormat formatter = new DecimalFormat("0.0");
        // from a decimal magnitude value
        String magnitude = formatter.format(currentearthquake.getmMagnitude());
        // Display the magnitude  of the current earthquake in thar TextView
        magnitudeView.setText(magnitude);

        // Set the proper background color on the magnitude circle.
        // Fetsh the backgrounf from the textView, which is a GradientDrawable
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();
        // Get the approprite background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentearthquake.getmMagnitude());
        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        // Get the original lication string from the Earthquake object,
        String originalLocation  = currentearthquake.getmLocation();

        // If the original location string (i.e. "5km N of Cairo, Egypt") contains
        // a primary location (Cairo, Egypt) and a location offset (5km N of that city)
        // then store the primary location separately from the location offset in 2 Strings,
        // so they can be displayed in 2 TextViews.
        String locationOffset;
        String primaryLocation;

        // check weather the original location string contains the " of " text
        if(originalLocation.contains(" of ")) {
            // divide the string base on the "of" text, locationOffset will be from biginner to "of"
            locationOffset = originalLocation.substring(0, originalLocation.indexOf(" of") + 3);
            // divide the string base on the "of" text, primaryLocation will be from "of" to end
            primaryLocation = originalLocation.substring(originalLocation.indexOf("of ") + 3, originalLocation.length());
        } else {
            //otherwise, there id no "of" text in the originalLocation string,
            // Hence, set the default location offset to say "Near the".
            locationOffset = getContext().getString(R.string.near_the);
            // the primary location will be the full location string
            primaryLocation = originalLocation;
        }

        // Find the TextView with view ID primary_location
        TextView primaryLocationView  = (TextView) listViewItem.findViewById(R.id.primary_location);
        // Display the location  of the current earthquake in thar TextView
        primaryLocationView .setText(primaryLocation);

        // Find the TextView with view ID location_offset
        TextView locationOffsetView  = (TextView) listViewItem.findViewById(R.id.location_offset);
        // Display the location offset  of the current earthquake in thar TextView
        locationOffsetView .setText(locationOffset);

        // Create a new Date object from the time in millisecond of the earthquake
        Date dateObject = new Date(currentearthquake.getmTimeInMilliseconds());
        // formatted date string (i.e. "Mar 3, 1984") from a Date object
        SimpleDateFormat dateFormate = new SimpleDateFormat("MMM DD, yyyy");
        // formatted date string (i.e. "4:30 PM") from a Date object.
        SimpleDateFormat timeFormate = new SimpleDateFormat("HH:mm aaa");



        // Find the TextView with view ID date
        TextView dateView  = (TextView) listViewItem.findViewById(R.id.date);
        // Formate the date String (i.e. "Mar 3, 1984")
        String dateToDisplay = dateFormate.format(dateObject);
        // Display the date  of the current earthquake in thar TextView
        dateView .setText(dateToDisplay);

        // Find the TextView with view ID date
        TextView timeView  = (TextView) listViewItem.findViewById(R.id.time);
        // Format the time string (i.e. "4:30PM")
        String timeToDisplay = timeFormate.format(dateObject);
        // Display the date  of the current earthquake in thar TextView
        timeView .setText(timeToDisplay);

        // Return the list item view that is now showing the appropriate date
        return listViewItem;
    }

    /**
     *
     * @param magnitude of the earthquake
     * @return the color for the magnitude circle based on the intensity of the earthquake.
     */
    private int getMagnitudeColor(Double magnitude){
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor){
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
