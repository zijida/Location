package com.zijida.location.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

import com.zijida.location.service.coreLocationService;

import java.util.List;

/**
 * Created by Administrator on 14-3-15.
 */
public class ProviderChangeReceiver extends BroadcastReceiver {

    private static boolean isServiceRun(Context mContext, String className) {
        boolean isRun = false;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(80);
        int size = serviceList.size();
        for (int i = 0; i < size; i++)
        {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRun = true;
                break;
            }
        }
        return isRun;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.v("zijida","BroadcastReceiver.onReceive "+intent.getAction());

        if(intent.getAction().equals(LocationManager.PROVIDERS_CHANGED_ACTION))
        {
            boolean bProviderEnabled = false;
            boolean bServiceRanning = isServiceRun(context, coreLocationService.class.getName());

            if(intent.getExtras() == null)
            {
                LocationManager lm = (LocationManager)context.getSystemService(context.LOCATION_SERVICE);
                if(lm == null) return;
                bProviderEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            }

            if(bProviderEnabled)
            {
                if(!bServiceRanning)
                {
                    Log.v("zijida","BroadcastReceiver. KEY_PROVIDER_ENABLED ");
                    Intent ni = new Intent(context,coreLocationService.class);
                    context.startService(ni);
                }
            }
            else
            {
                if(bServiceRanning)
                {
                    Log.v("zijida","BroadcastReceiver. KEY_PROVIDER_UNENABLED ");
                    Intent ni = new Intent(context,coreLocationService.class);
                    context.stopService(ni);
                }
            }
        }
    }
}
