package ren.yale.android.basemvplib;

import android.app.Application;
import android.app.Dialog;

import ren.yale.android.retrofitcachelib.RetrofitCache;

/**
 * Created by yale on 2017/9/28.
 */

public abstract class BaseApp extends Application {

    public static BaseApp APP;

    @Override
    public void onCreate() {
        super.onCreate();
        APP  = this;
        RetrofitCache.getInatance().init(this);
    }
    public abstract Dialog getProgressDlg();
    public abstract void showToast(String msg);

    public static Dialog  getProgressDialog(){
        return APP.getProgressDlg();
    }
    public static void showToastStr(String msg){
        APP.showToast(msg);
    }
}
