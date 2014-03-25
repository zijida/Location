package com.zijida.location.service;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.zijida.location.Util.EvilTransform;

/**
 * Created by Administrator on 14-3-4.
 */
public class genLocationProvider extends baseLocationProvider implements LocationListener {
    private LocationManager locationManager;
    private commonLocationListener mLocationListener;
    private Location location_now;

    public void genLocationProvider(Context context,commonLocationListener listener)
    {
        locationManager = (LocationManager)context.getSystemService(context.LOCATION_SERVICE);
        mLocationListener = listener;
    }

    @Override
    public void setActive(boolean bActive)
    {
        if(bActive)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, this);
        else
        {
            locationManager.removeUpdates(this);
            location_now = null;
        }
    }

    @Override
    public boolean isEnable() {
        return (location_now!=null);
    }

    @Override
    public void onLocationChanged(Location location) {

        if(location==null) return;

        /// PURE GPS坐标变换成 WGS84,与高德坐标保持一致
        location_now = EvilTransform.transform(location);

        if(mLocationListener!=null)
        {
            mLocationListener.onLocationChange(location_now);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        if(mLocationListener!=null)
        {
            mLocationListener.onGpstatusChanged(s,i,bundle);
        }
    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
