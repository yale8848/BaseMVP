package ren.yale.android.basemvplib.rx;

import android.text.TextUtils;

import java.lang.reflect.Field;

import ren.yale.android.basemvplib.rx.except.ApiExcetion;
import ren.yale.android.basemvplib.rx.lifecycle.RXActivityLifeCycleEnvent;
import ren.yale.android.retrofitcachelib.transformer.CacheTransformer;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by Yale on 2017/6/6.
 */

public class RxSchedulerHelper {


    private static Boolean isSuccess(Object t){

        boolean success = false;
        try {
            Class<?> clazz = t.getClass();
            Field f = clazz.getDeclaredField("success");
            Field.setAccessible(new Field[]{f},true);
            success = (boolean) f.get(t);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return success;
    }
    private static String getMessage(Object t){

        String message="";
        try {
            Class<?> clazz = t.getClass();
            Field f = clazz.getDeclaredField("message");
            Field.setAccessible(new Field[]{f},true);
            message = (String) f.get(t);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return message;
    }
    public static <T> Observable.Transformer<T,T> bindDestoryEvent(PublishSubject publishSubject){

        return bindEvent(publishSubject, RXActivityLifeCycleEnvent.DESTROY);
    }
    public static <T> Observable.Transformer<T,T> bindEvent(final PublishSubject publishSubject, final RXActivityLifeCycleEnvent envent){
        return new Observable.Transformer<T,T>(){
            @Override
            public Observable<T> call(Observable<T> tObservable) {

                Observable observable = publishSubject.takeFirst(new Func1<RXActivityLifeCycleEnvent,Boolean>(){

                    @Override
                    public Boolean call(RXActivityLifeCycleEnvent rxActivityLifeCycleEnvent) {
                        return rxActivityLifeCycleEnvent.equals(envent);
                    }
                });
                return tObservable.takeUntil(observable);
            }
        };
   }

    public static <T> Observable.Transformer<T,T> cacheJustNullIoMain(){
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {

                return tObservable.compose(CacheTransformer.<T>emptyTransformer())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).map(new Func1<T, T>() {
                            @Override
                            public Object call(Object t) {
                                if(t == null){
                                    throw  new ApiExcetion("数据有误");
                                }
                                return t;
                            }
                        });
            }
        };
    }

    public static <T> Observable.Transformer<T,T> cacheIoMain(){
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {

                return tObservable.compose(CacheTransformer.<T>emptyTransformer())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).map(new Func1<T, T>() {
                            @Override
                            public Object call(Object t) {
                                if(t == null){
                                    throw  new ApiExcetion("数据有误");
                                }
                                Boolean success = isSuccess(t);
                                if (success!=null&&!success.booleanValue()){
                                    String message = getMessage(t);
                                    if (TextUtils.isEmpty(message)){
                                        message="处理有误,默认消息";
                                    }
                                    throw  new ApiExcetion(message);
                                }
                                return t;
                            }
                        });
            }
        };
    }
    public static <T> Observable.Transformer<T, T> justIoMain() {

        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {

                return tObservable.compose(CacheTransformer.<T>emptyTransformer())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
    public static <T> Observable.Transformer<T, T> ioMain() {

        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {

                return tObservable.compose(CacheTransformer.<T>emptyTransformer())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).map(new Func1<T, T>() {
                            @Override
                            public Object call(Object t) {
                                   if(t == null){
                                       throw  new ApiExcetion("数据有误");
                                   }
                                Boolean success = isSuccess(t);
                                if (success!=null&&!success.booleanValue()){
                                    String message = getMessage(t);
                                    if (TextUtils.isEmpty(message)){
                                        message="处理有误,默认消息";
                                    }
                                    throw  new ApiExcetion(message);
                                }
                                return t;
                            }
                        });
            }
        };
    }
}
