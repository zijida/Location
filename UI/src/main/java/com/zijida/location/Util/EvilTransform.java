package com.zijida.location.Util;

import android.location.Location;

/**
 * Created by Administrator on 14-2-17.
 */
public class EvilTransform {
    private static final double pi=3.14159265358979324;
    private static final double a = 6378245.0;
    private static final double ee = 0.00669342162296594323;

    // World Geodetic System ==> Mars Geodetic System
    public static Location transform(Location in)
    {
        Location out = in;
        if(outOfChina(in.getLatitude(),in.getLongitude()))
        {
            out.setLatitude(in.getLatitude());
            out.setLongitude(in.getLongitude());
            return out;
        }

        double dLat = transformLat(in.getLongitude()-105.0,in.getLatitude()-35.0);
        double dLon = transformLon(in.getLongitude()-105.0,in.getLatitude()-35.0);
        double radLat = in.getLatitude()/180.0*pi;
        double magic = Math.sin(radLat);
        magic = 1-ee*magic*magic;
        double sqrtMagic = Math.sqrt(magic);

        dLat = (dLat*180.0)/((a*(1-ee))/(magic*sqrtMagic)*pi);
        dLon = (dLon*180.0)/(a/sqrtMagic*Math.cos(radLat)*pi);
        out.setLatitude(in.getLatitude() + dLat);
        out.setLongitude(in.getLongitude() + dLon);

        return out;
    }

    static boolean outOfChina(double lat,double lon)
    {
        if(lon < 72.004 || lon > 137.8347) return true;
        if(lat < 0.8293 || lat > 55.8271) return true;
        return false;
    }

    static double transformLat(double x,double y)
    {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    static double transformLon(double x, double y)
    {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }
}
