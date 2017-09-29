package ren.yale.android.basemvp;

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
}
