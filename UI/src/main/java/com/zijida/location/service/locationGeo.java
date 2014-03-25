package com.zijida.location.service;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 14-3-17.
 */
public class locationGeo implements Parcelable{
    public String address;

    public locationGeo(){}
    public locationGeo(Parcel parcel)
    {
        address = parcel.readString();
    }

    public static final Parcelable.Creator<locationGeo> CREATOR =
            new Creator<locationGeo>()
            {
                @Override
                public locationGeo createFromParcel(Parcel parcel) {
                    return new locationGeo(parcel);
                }

                @Override
                public locationGeo[] newArray(int i) {
                    return new locationGeo[i];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(address);
    }
}
