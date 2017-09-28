package ren.yale.android.basemvplib.rx.except;

/**
 * Created by Yale on 2017/6/7.
 */

public class ApiExcetion extends RuntimeException {
    public ApiExcetion(String msg){
           super(msg);
    }
}
