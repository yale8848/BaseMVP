package ren.yale.android.basemvplib.rx.subscribe;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import ren.yale.android.basemvplib.rx.except.ApiExcetion;
import rx.Subscriber;

/**
 * Created by Yale on 2017/6/7.
 */

abstract class BaseSubscriber<T> extends Subscriber<T> {
    protected  static  final  int MSG_TOAST=0x50345;
    protected  static  final  int MSG_DLG_SHOW=MSG_TOAST+1;
    protected  static  final  int MSG_DLG_CANCEL=MSG_TOAST+2;
    protected  static  final  int MSG_TEXT=MSG_TOAST+5;

    protected  Subscriber _this;


    protected void unSubscribed(){
        if (!_this.isUnsubscribed()){
            _this.unsubscribe();
        }
    }

    protected abstract Handler getHandler();

    protected void sendMsg(int what){
        getHandler().sendEmptyMessage(what);
    }
    protected void sendMsg(int what,String msg){
        if (TextUtils.isEmpty(msg)){
            sendMsg(what);
        }else{
            Message m = Message.obtain();
            m.what = what;
            m.obj = msg;
            getHandler().sendMessage(m);

        }
    }

    @Override
    public void onError(Throwable e) {
        sendMsg(MSG_DLG_CANCEL);

        if (e instanceof SocketTimeoutException) {
            sendMsg(MSG_TEXT,"网络中断，已超时");
        } else if (e instanceof ConnectException) {
            sendMsg(MSG_TEXT,"网络中断，请检查您的网络状态");
        } else if (e instanceof ApiExcetion){
            sendMsg(MSG_TEXT,e.getMessage());
        }else if (e instanceof UnknownHostException){
            sendMsg(MSG_TEXT,"请检查网络链接");
        }else {
            sendMsg(MSG_TEXT,"未知错误");
        }
    }
}
