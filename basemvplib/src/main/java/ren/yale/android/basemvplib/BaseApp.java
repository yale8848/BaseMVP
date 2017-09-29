package ren.yale.android.basemvplib;

import android.app.Application;
import android.widget.Toast;

import ren.yale.android.retrofitcachelib.RetrofitCache;

/**
 * Created by yale on 2017/9/28.
 */

public class BaseApp extends Application {

    public static BaseApp APP;

    @Override
    public void onCreate() {
        super.onCreate();
        APP  = this;
        RetrofitCache.getInatance().init(this);
    }

    public static void showToastStr(String msg){
        Toast.makeText(APP.getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }
}
