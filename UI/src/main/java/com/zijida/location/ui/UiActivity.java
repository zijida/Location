package com.zijida.location.ui;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.zijida.location.service.IcoreLocationService;
import com.zijida.location.service.coreLocationService;

public class UiActivity extends ActionBarActivity {
    private Toast toast;
    private ServerBroadcastReceiver receiver;
    private ServerMsgHandle handle;
    boolean bServiceBind = false;

    /// 远程服务调用
    private IcoreLocationService mLocationService;
    private ServiceConnection mLocationConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            ToastTip("ServiceConnection: onServiceConnected.", true);
            mLocationService = IcoreLocationService.Stub.asInterface(service);
        }

        public void onServiceDisconnected(ComponentName className) {
            mLocationService = null;
        }
    };

    /// 系统广播接收类
    public class ServerBroadcastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v("zijida", "ServerBroadcastReceiver onReceive.");

            if(intent.getAction().equals(String.valueOf(R.string.ACTION_BROADCAST)))
            {
                handle.sendEmptyMessage(intent.getIntExtra("message",0));
            }
        }
    }

    /// 自定义消息处理类
    public class ServerMsgHandle extends Handler
    {
        public ServerMsgHandle(Looper looper){
            super(looper);
        }

        @SuppressLint("NewApi")
        @Override
        public void handleMessage(Message msg) {//处理消息
            switch(msg.what)
            {
                case 0: break;
                case R.id.broadcast_locationChanged:
                {
                    ToastTip("receive broadcast: onLocationChange.", true);
                    try
                    {
                        if(mLocationService==null) break;
                        Location location = mLocationService.get_location();
                        ((TextView)findViewById(R.id.text_tip)).setText("["+location.getLatitude()+","+location.getLongitude()+"]");
                    }
                    catch (RemoteException e)
                    {
                        e.printStackTrace();
                    }
                }
                break;

                case R.id.broadcast_geographyGetted:
                break;
                default:break;
            }
        }
    }

    public void ToastTip(String context,boolean IsShort)
    {
        int duration = (IsShort==true)?Toast.LENGTH_SHORT:Toast.LENGTH_LONG;
        CharSequence textE="";
        textE=context;
        toast=Toast.makeText(this, textE, duration);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui);
        handle = new ServerMsgHandle(getMainLooper());
        receiver = new ServerBroadcastReceiver();

        //注册广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction(String.valueOf(R.string.ACTION_BROADCAST));
        registerReceiver(receiver, filter);

        //BIND后台位置服务
        bindService(new Intent(this, coreLocationService.class), mLocationConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy()
    {
        // 注销广播接收器
        unregisterReceiver(receiver);
        // 解绑后台位置服务
        unbindService(mLocationConnection);

        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ui, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId())
        {
            case R.id.action_unset:
            {
            }
            return true;

            case R.id.action_settings:
            {
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //  <!-- --------------------------------------------------------------------------------  -->
}
