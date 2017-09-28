package ren.yale.android.basemvp;

import android.app.Dialog;
import android.widget.Toast;

import ren.yale.android.basemvp.api.ApiManager;
import ren.yale.android.basemvplib.BaseApp;

/**
 * Created by yale on 2017/9/28.
 */

public class App extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();

        ApiManager.getInstance().init(this);
    }

    @Override
    public Dialog getProgressDlg() {
        return null;
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }
}
