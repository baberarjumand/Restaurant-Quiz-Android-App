package ca.ubc.cs.cpsc210.quiz.activity;

import android.graphics.Color;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pcarter on 14-11-06.
 *
 * Manager for markers plotted on map
 */
public class MarkerManager {
    private GoogleMap map;
    private float f;
    private List<Marker> markers;
    private Marker marker;

    /**
     * Constructor initializes manager with map for which markers are to be managed.
     * @param map  the map for which markers are to be managed
     */
    public MarkerManager(GoogleMap map) {
        this.map = map;
        markers = new ArrayList<Marker>();
    }

    /**
     * Get last marker added to map
     * @return  last marker added
     */
    public Marker getLastMarker() {
        return markers.get(markers.size() - 1);
    }

    /**
     * Add green marker to show position of restaurant
     * @param point   the point at which to add the marker
     * @param title   the marker's title
     */
    public void addRestaurantMarker(LatLng point, String title) {
        marker = map.addMarker(new MarkerOptions()
                .position(point)
                .title(title)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
    }

    /**
     * Add a marker to mark latest guess from user.  Only the most recent two positions selected
     * by the user are marked.  The distance from the restaurant is used to create the marker's title
     * of the form "xxxx m away" where xxxx is the distance from the restaurant in metres (truncated to
     * an integer).
     *
     * The colour of the marker is based on the distance from the restaurant:
     * - red, if the distance is 3km or more
     * - somewhere between red (at 3km) and green (at 0m) (on a linear scale) for other distances
     *
     * @param latLng
     * @param distance
     */
    public void addMarker(LatLng latLng, double distance) {
        Integer distanceInt = (int) distance;
        String distanceString = String.valueOf(distanceInt);
        float d = getColour(distance);

        marker = map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(distanceString + " " + "m" + " " + "away")
                    .icon(BitmapDescriptorFactory.defaultMarker(d)));
        markers.add(marker);

        if(markers.size() > 2){
            for(int i=0; i < markers.size()-2; i++){
                Marker removeMarker = markers.get(i);
                removeMarker.remove();
            }
        }

    }

    /**
     * Remove markers that mark user guesses from the map
     */
    public void removeMarkers() {
        for (int i=0; i < markers.size(); i++) {
            Marker removeMarker = markers.get(i);
            removeMarker.remove();
        }
    }

    /**
     * Produce a colour on a linear scale between red and green based on distance:
     *
     * - red, if distance is 3km or more
     * - somewhere between red (at 3km) and green (at 0m) (on a linear scale) for other distances
     * @param distance  distance from restaurant
     * @return  colour of marker
     */
    private float getColour(double distance) {

        if (distance >= 3000) {
            f = 0.0f;
        } else if ((2500 <= distance) && (distance < 3000)) {
            f = 15.0f;
        } else if ((2000 <= distance) && (distance < 2500)) {
            f = 30.0f;
        } else if ((1500 <= distance) && (distance < 2000)) {
            f = 60.0f;
        } else if ((1000 <= distance) && (distance < 1500)) {
            f = 65.0f;
        } else if ((500 <= distance) && (distance < 1000)) {
            f = 70.0f;
        } else if ((50 < distance) && (distance < 500)) {
            f = 75.0f;
        } else if (distance <= 50) {
            f = 0.0f;
        }
        return f;
    }


}
