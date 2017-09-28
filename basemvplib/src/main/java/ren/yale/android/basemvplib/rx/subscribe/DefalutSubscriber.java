package ren.yale.android.basemvplib.rx.subscribe;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import ren.yale.android.basemvplib.BaseApp;

/**
 * Created by Yale on 2017/6/7.
 */

public class DefalutSubscriber<T> extends BaseSubscriber<T>{

    Handler mHandler;
    public DefalutSubscriber(){
    }
    private void init(){
        _this = this;
        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case MSG_TEXT:{
                        String mg = (String) msg.obj;
                        BaseApp.showToastStr(mg);
                        break;
                    }
                }
            }
        };
    }
    @Override
    public void onStart() {
        init();
      /*  if (!UIUtils.isConnectInternet(mContext)){
            unSubscribed();
            sendMsg(MSG_TEXT,"请检查网络链接");
        }*/

    }

    @Override
    public void onCompleted() {

    }

    @Override
    protected Handler getHandler() {
        return mHandler;
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
    }

    @Override
    public void onNext(T t) {

    }
}
