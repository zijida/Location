package com.zijida.location.service;

import android.content.Context;
import android.location.Location;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.zijida.location.ui.R;

/**
 * Created by Administrator on 14-3-5.
 */
public class amapReGeoProvider {

    private GeocodeSearch mGeoSearch;
    private LatLonPoint center_point;
    private RegeocodeQuery reGeocodeSearcher;
    private commonLocationListener mLocationListener;

    public amapReGeoProvider(final Context context,commonLocationListener listener)
    {
        mGeoSearch = new GeocodeSearch(context);
        mLocationListener = listener;

        GeocodeSearch.OnGeocodeSearchListener geo_search_listener = new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult rresult, int iCode)
            {
                locationGeo geo = new locationGeo();
                geo.address = context.getString(R.string.geo_no_result);

                if(mLocationListener == null)
                {
                    mLocationListener.onReGeoDataSearched(geo);
                    return;
                }

                switch (iCode) {
                    case 0:
                        if (rresult != null && rresult.getRegeocodeAddress() != null)
                        {
                            RegeocodeAddress ra = rresult.getRegeocodeAddress();
                            geo.address = ra.getFormatAddress();
                            /*
                            String poi_key_pre = "POI_";
                            int index = 0;
                            for(PoiItem pi:ra.getPois())
                            {
                                String poi_key = poi_key_pre + index;
                                bundle.putString(poi_key,getPoiInfo(pi));
                            }
                            */
                        }
                        else geo.address=context.getString(R.string.geo_no_data);
                        break;

                    case 22: geo.address = context.getString(R.string.geo_ret_22);  break;
                    case 23: geo.address = context.getString(R.string.geo_ret_23);  break;
                    case 24: geo.address = context.getString(R.string.geo_ret_24);  break;
                    case 25: geo.address = context.getString(R.string.geo_ret_25);  break;
                    case 26: geo.address = context.getString(R.string.geo_ret_26);  break;
                    case 27: geo.address = context.getString(R.string.geo_ret_27);  break;
                    case 28: geo.address = context.getString(R.string.geo_ret_28);  break;
                    case 29: geo.address = context.getString(R.string.geo_ret_29);  break;
                    case 30: geo.address = context.getString(R.string.geo_ret_30);  break;
                    case 32: geo.address = context.getString(R.string.geo_ret_32);  break;
                    default: geo.address = context.getString(R.string.geo_ret_default);  break;
                }

                mLocationListener.onReGeoDataSearched(geo);
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        };

        mGeoSearch.setOnGeocodeSearchListener(geo_search_listener);
    }

    protected void startGeoLocationSearch(Location point)
    {
        if(point==null) return;

        if(center_point == null)
        {
            center_point = new LatLonPoint(point.getLatitude(),point.getLongitude());
        }
        else {
            center_point.setLongitude(point.getLongitude());
            center_point.setLatitude(point.getLatitude());
        }

        if(reGeocodeSearcher == null)
        {
            reGeocodeSearcher = new RegeocodeQuery(center_point,50.0f,GeocodeSearch.GPS);
        }
        else
        {
            reGeocodeSearcher.setPoint(center_point);
            reGeocodeSearcher.setRadius(50.0f);
            reGeocodeSearcher.setLatLonType(GeocodeSearch.AMAP);
        }
        mGeoSearch.getFromLocationAsyn(reGeocodeSearcher);
    }
}
