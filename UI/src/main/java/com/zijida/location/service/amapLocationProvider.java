package com.zijida.location.service;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;

/**
 * Created by Administrator on 14-3-4.
 */
public class amapLocationProvider extends baseLocationProvider implements AMapLocationListener{
    private LocationManagerProxy gps_manager;
    private LocationManagerProxy wifi_manager;
    private commonLocationListener server_listener;
    private Location location_now;
    private Context mContext;

    public  amapLocationProvider(Context context,commonLocationListener listener)
    {
        mContext = context;
        server_listener = listener;
    }

    @Override
    public void setActive(boolean bActive)
    {
        if(bActive)
        {
            if(gps_manager==null)
            {
                gps_manager = LocationManagerProxy.getInstance(mContext);
                gps_manager.requestLocationUpdates(LocationManagerProxy.GPS_PROVIDER, 2000, 0, this);
                // gps_manager.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 2000, 0, this);
            }
        }
        else
        {
            if(gps_manager!=null)
            {
                gps_manager.removeUpdates(this);
                gps_manager.destory();
                gps_manager = null;
                location_now = null;
            }
        }
    }

    @Override
    public boolean isEnable() {
        return (location_now != null);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if(aMapLocation==null) return;
        location_now = aMapLocation;

        if(server_listener!=null)
        {
            /// 这里直接就是 WGS84
            server_listener.onLocationChange(location_now);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if(server_listener!=null)
        {
            server_listener.onLocationChange(location);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
