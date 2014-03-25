package com.zijida.location.service;

import android.location.Location;
import android.os.Bundle;

/**
 * Created by Administrator on 14-3-4.
 */
public interface commonLocationListener {
    public void onLocationChange(Location location);
    public void onGpstatusChanged(String s, int i, Bundle bundle);
    public void onReGeoDataSearched(locationGeo lg);
}
