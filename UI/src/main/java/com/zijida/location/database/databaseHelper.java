package com.zijida.location.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import com.zijida.location.ui.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 14-3-19.
 */
public class databaseHelper extends SQLiteOpenHelper {
    private Context mContext;

    public databaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    public databaseHelper(Context context, String name) {
        super(context, name, null, 1);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.v("zijida","databaseHelper.onCreate"+mContext.getString(com.zijida.location.ui.R.string.create_table_xingcheng));
        try
        {
            sqLiteDatabase.execSQL(mContext.getString(com.zijida.location.ui.R.string.create_table_xingcheng));
        }
        catch (SQLiteException se)
        {
            print_exceptions(se);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        Log.v("zijida","databaseHelper.onUpgrade");
        if(i == i2) return;

        try
        {
            Cursor c = sqLiteDatabase.rawQuery("select file from xingcheng;", null);
            String sql = "drop table ";
            while (c.moveToNext())
            {
                sqLiteDatabase.execSQL(sql + c.getString(0));
            }
            sqLiteDatabase.execSQL(mContext.getString(R.string.drop_table_xingcheng));
            sqLiteDatabase.execSQL(mContext.getString(com.zijida.location.ui.R.string.create_table_xingcheng));
        }
        catch (SQLiteException se)
        {
            print_exceptions(se);
        }
    }

    private String new_travle_table_file_name()
    {
        String s = "travel_";
        SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
        s += format.format(new Date());
        Log.v("zijida","databaseHelper.new_travle_table_file_name:"+s);
        return s;
    }

    /// 创建一个新的时点记录表，并加入到行程表中
    /// 输入值：为本次时点记录所命的名字
    /// 返回值：新时点记录表的表名。
    public String create_table_new_travel(String travel_name)
    {
        Log.v("zijida","databaseHelper.create_table_new_travel:"+travel_name);

        SQLiteDatabase db = getWritableDatabase();
        if(db!=null)
        {
            Object[] param = new Object[2];
            param[0] = new_travle_table_file_name();
            param[1] = (travel_name==null)?"null":travel_name;

            try
            {
                /// 创建新的时点记录表(travel_nowtime)
                String sql = mContext.getString(R.string.create_table_dbfile);
                String finalsql = sql.replace("filename",param[0].toString());
                db.execSQL(finalsql);

                /// 加入记录到行程表(xingcheng)
                sql = mContext.getString(R.string.insert_table_xingcheng);
                db.execSQL(sql,param);
            }
            catch (SQLiteException se)
            {
                print_exceptions(se);
            }

            return param[0].toString();
        }
        return null;
    }

    /// 为时点记录表添加一条记录
    public void insert_into_travel_table(String tableName,Location l)
    {
        Log.v("zijida","databaseHelper.insert_into_travel_table:"+tableName);

        SQLiteDatabase db = getWritableDatabase();
        if(db == null) return;
        if(l == null) return;

        Object[] param = new Object[3];
        param[0] = (int)(l.getLatitude()*1000000);
        param[1] = (int)(l.getLongitude()*1000000);
        param[2] = l.hasSpeed()?(int)(l.getSpeed()*1000): 0;

        try
        {
            /// 创建新的时点记录表(travel_nowtime)
            String sql = mContext.getString(R.string.insert_table_dbfile);
            String finalsql = sql.replace("filename", tableName);
            db.execSQL(finalsql,param);
        }
        catch (SQLiteException se)
        {
            print_exceptions(se);
        }
    }

    /// 取出时点记录表所有记录
    public void query_travel_table(String tableName)
    {
        SQLiteDatabase db = getWritableDatabase();
        if(db == null) return;

        try
        {
            Cursor c = db.rawQuery("select * from "+tableName+";",null);
            if(c == null) return;

        }
        catch (SQLiteException se)
        {
            print_exceptions(se);
        }
    }

    /// CATCH EXCEPTION TO LOG
    private void print_exceptions(SQLiteException se)
    {
        //se.printStackTrace();
        Log.e("zijida", "==> " + se.getMessage());
        /*
        StackTraceElement[] exception = se.getStackTrace();
        for (StackTraceElement ste:exception)
        {
        }
        */
    }
}
