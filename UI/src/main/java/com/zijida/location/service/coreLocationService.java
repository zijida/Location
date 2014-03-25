package com.zijida.location.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.zijida.location.database.databaseHelper;
import com.zijida.location.ui.R;

/**
 * Created by Administrator on 14-3-14.
 */
public class coreLocationService extends Service implements commonLocationListener {
    private baseLocationProvider locator;
    private amapReGeoProvider reGeoProvider;
    private Location nowLocation;
    private locationGeo nowReGeography;
    private PointSet ps;    // 记录运动轨迹的对象
    private databaseHelper dbhelper; // 记录运动轨迹的DB辅助类
    private String travelTableName;

    private IcoreLocationService.Stub iServier = new IcoreLocationService.Stub()
    {
        @Override
        public void quest_location() throws RemoteException {
        }

        @Override
        public void quest_regeography() throws RemoteException {
            if(reGeoProvider==null)
            {
                reGeoProvider = new amapReGeoProvider(coreLocationService.this,coreLocationService.this);
            }
            reGeoProvider.startGeoLocationSearch(nowLocation);
        }

        public Location get_location()  throws RemoteException
        {
            return nowLocation;
        }

        public PointSet get_pointset() throws RemoteException
        {
            return ps;
        }

        public locationGeo get_geography() throws RemoteException
        {
            return nowReGeography;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        Log.v("zijida","coreLocationService onBind.");

        if(locator!=null)
        {
            locator.setActive(true);
        }
        return iServier;
    }

    @Override
    public void onRebind(android.content.Intent intent)
    {
        Log.v("zijida","coreLocationService onReBind.");

        if(locator!=null)
        {
            locator.setActive(true);
        }
    }

    @Override
    public boolean onUnbind(android.content.Intent intent)
    {
        Log.v("zijida","coreLocationService onUnbind.");

        if(locator!=null)
        {
            locator.setActive(false);
        }
        super.onUnbind(intent);
        return true;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        reGeoProvider = new amapReGeoProvider(this,this);
        locator = new amapLocationProvider(this,this);
        dbhelper = new databaseHelper(this,"location");
        ps = new PointSet();

        Log.v("zijida","coreLocationService onCreate.");
    }

    @Override
    public int onStartCommand(android.content.Intent intent, int flags, int startId)
    {
        Log.v("zijida","coreLocationService onStartCommand.");

        if(locator!=null)
        {
            locator.setActive(true);
        }

        travelTableName = dbhelper.create_table_new_travel("happy travel");
        return 0;
    }

    @Override
    public void onDestroy()
    {
        Log.v("zijida","coreLocationService onDestroy.");

        if(locator!=null)
        {
            locator.setActive(false);
        }
        super.onDestroy();
    }

    public void sendABroadcast(int id)
    {
        Intent ni = new Intent(coreLocationService.class.getName());
        ni.setAction(String.valueOf(R.string.ACTION_BROADCAST));
        ni.putExtra("message", id);
        sendBroadcast(ni);

        Log.v("zijida","sendBroadcast:"+ id);
    }

    @Override
    public void onLocationChange(Location location) {
        if(nowLocation==null || locationMath.distance_valid(nowLocation,location))
        {
            dbhelper.insert_into_travel_table(travelTableName,location);
            ps.add(location.getLatitude(),location.getLongitude());
        }
        nowLocation = location;
        sendABroadcast(R.id.broadcast_locationChanged);

    }

    @Override
    public void onGpstatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onReGeoDataSearched(locationGeo s) {
        nowReGeography = s;
        sendABroadcast(R.id.broadcast_geographyGetted);
    }
}
