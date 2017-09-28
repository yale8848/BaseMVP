package ren.yale.android.basemvplib.api;

import java.util.Map;

/**
 * Created by yale on 2017/9/28.
 */

public interface IHttpHeader {

    Map getHttpHeader(String url,Map mapParams);
}
