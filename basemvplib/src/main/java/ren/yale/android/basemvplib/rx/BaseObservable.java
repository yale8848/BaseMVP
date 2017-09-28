package ren.yale.android.basemvplib.rx;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by yale on 2017/9/28.
 */

public class BaseObservable<T> {

    private Observable mObservable;

    public static BaseObservable create(Observable observable){

        return new BaseObservable(observable);
    }
    public Observable observable(){
        return mObservable;
    }
    protected BaseObservable(Observable observable) {
     mObservable = observable;
    }

    public BaseObservable cacheJustNullIoMain (){
        mObservable =mObservable.compose(RxSchedulerHelper.cacheJustNullIoMain());
        return this;
    }
    public BaseObservable<T> cacheIoMain (){
        mObservable = mObservable.compose(RxSchedulerHelper.cacheIoMain());
        return this;
    }
    public BaseObservable ioMain (){
        mObservable =mObservable.compose(RxSchedulerHelper.ioMain());
        return this;
    }
    public BaseObservable justIoMain (){
        mObservable =mObservable.compose(RxSchedulerHelper.justIoMain());
        return this;
    }
    public Observable bindDestoryEvent(PublishSubject publishSubject){
        return mObservable.compose(RxSchedulerHelper.bindDestoryEvent(publishSubject));
    }
}
