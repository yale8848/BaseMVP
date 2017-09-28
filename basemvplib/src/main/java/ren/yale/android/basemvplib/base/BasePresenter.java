package ren.yale.android.basemvplib.base;


import ren.yale.android.basemvplib.rx.lifecycle.RXActivityLifeCycleEnvent;
import rx.subjects.PublishSubject;

/**
 * Created by Yale on 2017/6/6.
 */

public class BasePresenter<M,V> {
    protected M mModel;
    protected V mView;

    private PublishSubject<RXActivityLifeCycleEnvent> mPublishSubject;


    public void attachMV(M m,V v){
         this.mModel = m;
         this.mView =v;
    }
    public void detachMV(){
        this.mModel = null;
        this.mView = null;
    }

    public PublishSubject<RXActivityLifeCycleEnvent> getPublishSubject() {
        return mPublishSubject;
    }

    public void setPublishSubject(PublishSubject<RXActivityLifeCycleEnvent> publishSubject) {
        mPublishSubject = publishSubject;
    }
}
