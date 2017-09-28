package ren.yale.android.basemvplib.api;

import android.content.Context;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import ren.yale.android.retrofitcachelib.RetrofitCache;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Yale on 2017/6/6.
 */

public class HttpRequestManger {

    private volatile static HttpRequestManger mHttpRequestApi;
    private Context mContext;
    private Interceptor mInterceptor;
    private Interceptor mNetWorkInterceptor;
    private IHttpHeader mIHttpHeader;

    private OkHttpUtils mOkHttpUtils;
    private OkHttpClient mOkHttpClient;

    private HttpRequestManger(){
        mOkHttpUtils = new OkHttpUtils();
    }

    public HttpRequestManger setIHttpHeader(IHttpHeader iHttpHeader){
        mIHttpHeader = iHttpHeader;
        return this;
    }

    public HttpRequestManger setInterceptor(Interceptor interceptor){
        mInterceptor = interceptor;
        return this;
    }
    public HttpRequestManger setNetWorkInterceptor(Interceptor interceptor){
        mNetWorkInterceptor = interceptor;
        return this;
    }
    public void init(Context context){
        mContext = context;
        mOkHttpClient =mOkHttpUtils.getOkHttpClient(context,mIHttpHeader,mInterceptor,mNetWorkInterceptor);
    }
    public static HttpRequestManger getInstance(){
        if (mHttpRequestApi==null){
            synchronized (HttpRequestManger.class){
                if (mHttpRequestApi == null){
                    mHttpRequestApi = new HttpRequestManger();
                }
            }
        }
        return mHttpRequestApi;
    }
    public  <T> T configRetrofit(Class<T> service,String url ) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        RetrofitCache.getInatance().addRetrofit(retrofit);
        return retrofit.create(service);
    }


}
