package com.zijida.location.service;

import android.location.Location;

/**
 * Created by Administrator on 14-3-20.
 */
public class locationMath {
    static float VALID_DISTANCE = 20.0f;
    public static float distance(Location a,Location b)
    {
        float[] result = new float[1];
        Location.distanceBetween(a.getLatitude(),a.getLongitude(),b.getLatitude(),b.getLongitude(),result);
        return result[0];
    }

    public static boolean distance_valid(Location a,Location b)
    {
        return (distance(a,b)>VALID_DISTANCE);
    }
}
