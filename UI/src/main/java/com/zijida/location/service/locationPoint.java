package com.zijida.location.service;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 14-3-14.
 */
public class locationPoint implements Parcelable {
    public double lat;
    public double lon;

    public locationPoint(double a,double b)
    {
        lat = a;
        lon = b;
    }
    public locationPoint(Parcel parcel)
    {
        lat = parcel.readDouble();
        lon = parcel.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(lat);
        parcel.writeDouble(lon);
    }

    public static final Parcelable.Creator<locationPoint> CREATOR = new Creator<locationPoint>()
    {
        @Override
        public locationPoint createFromParcel(Parcel parcel) {
            return new locationPoint(parcel);
        }

        @Override
        public locationPoint[] newArray(int i) {
            return new locationPoint[i];
        }
    };
}
