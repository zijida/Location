package com.zijida.location.service;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 14-3-9.
 */
public class PointSet implements Parcelable{

    private float valid_range;
    private List<locationPoint> points;

    public static final Parcelable.Creator<PointSet> CREATOR = new Creator<PointSet>()
    {
        @Override
        public PointSet createFromParcel(Parcel parcel) {
            return new PointSet(parcel);
        }

        @Override
        public PointSet[] newArray(int i) {
            return new PointSet[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(valid_range);
        parcel.writeTypedList(points);
    }

    public PointSet()
    {
        valid_range = 200f;
        points = new ArrayList<locationPoint>();
        points.clear();
    }

    public PointSet(Parcel parcel)
    {
        valid_range = parcel.readFloat();
        points = new ArrayList<locationPoint>();
        parcel.readTypedList(points,locationPoint.CREATOR);
    }

    public void cleanSet()
    {
        points.clear();
    }

    public void setValid_range(float a) { valid_range = a; }

    public void add(double lat,double lon)
    {
        locationPoint nlp = new locationPoint(lat,lon);
        if(isAccectablePoint(nlp))
        {
            points.add(nlp);
        }
    }

    public List<locationPoint> getAllPoints()
    {
        return points;
        /*
        List<LatLng> lll = new ArrayList<LatLng>();
        for(locationPoint lp : points)
        {
            LatLng nll = new LatLng(lp.lat,lp.lon);
            lll.add(nll);
        }
        return lll;
        */
    }

    private boolean isAccectablePoint(locationPoint lp)
    {
        if(lp.lat==0f || lp.lon==0f) return false;
        if(points.isEmpty()) return true;

        locationPoint lastlp = points.get(points.size()-1);
        if(lastlp==null) return true;

        try
        {
            float[] distance = new float[1];
            Location.distanceBetween(lastlp.lat,lastlp.lon,lp.lat,lp.lon,distance);
            return (distance[0]>=valid_range);
        }
       catch (IllegalArgumentException iae)
       {
           Log.e("zijida","Location.distanceBetween throw IllegalArgumentException");
           return false;
       }
    }
}
