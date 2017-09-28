package ren.yale.android.basemvp.api;

import android.content.Context;

import java.util.Map;

import ren.yale.android.basemvplib.api.HttpRequestManger;
import ren.yale.android.basemvplib.api.IHttpHeader;

/**
 * Created by yale on 2017/9/28.
 */

public class ApiManager {

    private static TestApi testApi;
    private static final String URL ="https://mock.daoxuehao.com/mock/59cc6c3991895710a06deeef/test/";

    private static class Holder{
        public static volatile  ApiManager apiManager = new ApiManager();
    }

    public static ApiManager getInstance(){
        return Holder.apiManager;
    }

    public void init(Context context){
        HttpRequestManger.getInstance().setIHttpHeader(new IHttpHeader() {
            @Override
            public Map getHttpHeader(String url, Map mapParams) {
                return null;
            }
        }).setInterceptor(null).setNetWorkInterceptor(null).init(context);
    }

    public TestApi getTestApi(){
        if (testApi==null){
            testApi = HttpRequestManger.getInstance().configRetrofit(TestApi.class,URL);
        }
        return testApi;
    }
}
