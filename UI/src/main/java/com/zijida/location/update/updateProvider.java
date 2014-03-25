package com.zijida.location.update;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by Administrator on 14-3-25.
 */
public class updateProvider {
    private boolean hasAvaliableNewVersion(Context context)
    {
        try
        {
            int curVer = context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionCode;
        }
        catch (PackageManager.NameNotFoundException nfe)
        {
            Log.e("zijida",nfe.getMessage());
        }
        return false;
    }
}
