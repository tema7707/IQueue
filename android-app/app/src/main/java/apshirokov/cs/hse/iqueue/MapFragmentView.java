package apshirokov.cs.hse.iqueue;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.SupportMapFragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapFragmentView {
    private static double myLatitude, myLongitude;
    private PositioningManager posManager;
    private SupportMapFragment m_mapFragment;
    private List<MapMarker> markersQueue = new ArrayList<>();
    private Map m_map;
    private AppCompatActivity m_activity;
    private int zoom;

    public MapFragmentView(SupportMapFragment m_mapFragment, AppCompatActivity activity, int zoom) {
        this.m_mapFragment = m_mapFragment;
        this.zoom = zoom;
        m_activity = activity;
        initMapFragment();
    }

    private void initMapFragment() {
        // Set path of isolated disk cache
        String diskCacheRoot = Environment.getExternalStorageDirectory().getPath()
                + File.separator + ".isolated-here-maps";
        // Retrieve intent name from manifest
        String intentName = "";
        try {
            ApplicationInfo ai = m_activity.getPackageManager().getApplicationInfo(m_activity.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            intentName = bundle.getString("INTENT_NAME");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(this.getClass().toString(), "Failed to find intent name, NameNotFound: " + e.getMessage());
        }

        if (m_mapFragment != null) {
            /* Initialize the SupportMapFragment, results will be given via the called back. */
            m_mapFragment.init(error -> {

                if (error == OnEngineInitListener.Error.NONE) {
                    /*
                     * If no error returned from map fragment initialization, the map will be
                     * rendered on screen at this moment.Further actions on map can be provided
                     * by calling Map APIs.
                     */
                    m_map = m_mapFragment.getMap();
                    m_map.setCenter(new GeoCoordinate(55.751244, 37.618423), Map.Animation.LINEAR);

                    for (int i = 0; i < markersQueue.size(); i++)
                        m_map.addMapObject(markersQueue.get(i));

                    if (posManager == null) {
                        this.posManager = PositioningManager.getInstance();
                        posManager.start(PositioningManager.LocationMethod.GPS_NETWORK);
                    }

                    // Display position indicator
                    m_map.getPositionIndicator().setVisible(true);

                    m_map.setZoomLevel(zoom, Map.Animation.LINEAR);

                    while (posManager.getLastKnownPosition() == null) {}
                    myLatitude = posManager.getLastKnownPosition().getCoordinate().getLatitude();
                    myLongitude = posManager.getLastKnownPosition().getCoordinate().getLongitude();
                    m_map.setCenter(posManager.getLastKnownPosition().getCoordinate(), Map.Animation.LINEAR);
                }
            });
        }
    }

    public float getDist(double longitude, double latitude) {
        if (posManager == null) {
            this.posManager = PositioningManager.getInstance();
            posManager.start(PositioningManager.LocationMethod.GPS_NETWORK);
        }
        Location locationA = new Location("point A");
        locationA.setLatitude(latitude);
        locationA.setLongitude(longitude);

        Location locationB = new Location("point B");

        locationB.setLatitude(myLatitude);
        locationB.setLongitude(myLongitude);

        return Math.round(locationA.distanceTo(locationB));
    }

    /**
     * create a MapMarker and add the MapMarker to active map view.
     */
    public void createMapMarker(double x, double y) {
        // create an image from cafe.png.
        Image marker_img = new Image();
        try {
            marker_img.setImageResource(R.drawable.map_icon);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // create a MapMarker centered at current location with png image.
        MapMarker m_map_marker = new MapMarker(new GeoCoordinate(x, y), marker_img);
        // add a MapMarker to current active map.
        if (m_map != null)
            m_map.addMapObject(m_map_marker);
        else
            markersQueue.add(m_map_marker);
    }
}
