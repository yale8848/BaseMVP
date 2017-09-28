package ren.yale.android.basemvplib.rx.subscribe;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import ren.yale.android.basemvplib.BaseApp;


/**
 * Created by Yale on 2016/12/19.
 * http://gank.io/post/56e80c2c677659311bed9841?hmsr=toutiao.io&utm_medium=toutiao.io&utm_source=toutiao.io
 */

public class ProgressSubscriber<T> extends BaseSubscriber<T> {

    Dialog mProgressDialog;
    Handler mHandler;


    public ProgressSubscriber(){
        mProgressDialog = BaseApp.getProgressDialog();
        if (mProgressDialog!=null){
            mProgressDialog.setCancelable(true);
        }

    }

    private void init(){
        _this = this;
        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case MSG_DLG_CANCEL:
                        if (mProgressDialog!=null){
                            mProgressDialog.dismiss();
                        }

                        break;
                    case MSG_DLG_SHOW:
                        if (mProgressDialog!=null){
                            mProgressDialog.show();
                        }
                        break;
                    case MSG_TEXT:{
                        String mg = (String) msg.obj;
                        BaseApp.showToastStr(mg);
                        break;
                    }
                }
            }
        };
        if (mProgressDialog!=null){
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    sendMsg(MSG_TEXT,"取消");
                    unSubscribed();
                }
            });
        }

    }




    @Override
    public void onStart() {
        init();
   /*    if (!UIUtils.isConnectInternet(
                mLftProgressDlg.getDialog().getContext().getApplicationContext())){
            unSubscribed();
            sendMsg(MSG_TEXT,"请检查网络链接");
        }else{
            sendMsg(MSG_DLG_SHOW);
        }*/
        sendMsg(MSG_DLG_SHOW);
    }

    @Override
    public void onCompleted() {
        sendMsg(MSG_DLG_CANCEL);

    }

    @Override
    public void onError(Throwable e) {
       super.onError(e);

    }

    @Override
    public void onNext(T t) {

    }

    @Override
    protected Handler getHandler() {
        return mHandler;
    }
}
