package ren.yale.android.basemvplib.api;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import ren.yale.android.retrofitcachelib.intercept.CacheForceInterceptorNoNet;
import ren.yale.android.retrofitcachelib.intercept.CacheInterceptorOnNet;


/**
 * Created by Yale on 2017/6/6.
 */

public class OkHttpUtils {
    public final static String TAG = "OkHttpUtils";

    private IHttpHeader mIHttpHeader;


    public OkHttpClient getOkHttpClient(Context context,IHttpHeader iHttpHeader,Interceptor interceptor,Interceptor netWorkInterceptor){
        mIHttpHeader = iHttpHeader;


        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.readTimeout(20, TimeUnit.SECONDS);
        clientBuilder.connectTimeout(20, TimeUnit.SECONDS);
        clientBuilder.writeTimeout(20, TimeUnit.SECONDS);

        clientBuilder.addInterceptor(new LogInterceptor());
        clientBuilder.addInterceptor(new HeaderInterceptor());
        clientBuilder.addInterceptor(new CacheForceInterceptorNoNet());
        if (interceptor!=null){
            clientBuilder.addInterceptor(interceptor);
        }
        if (netWorkInterceptor!=null){
            clientBuilder.addNetworkInterceptor(netWorkInterceptor);
        }
        clientBuilder.addNetworkInterceptor(new CacheInterceptorOnNet());
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            clientBuilder.sslSocketFactory(sslSocketFactory);
            clientBuilder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }

        int cacheSize = 300 * 1024 * 1024;
        File cacheDirectory = new File(context.getCacheDir(), "httpcache");
        Cache cache = new Cache(cacheDirectory, cacheSize);
        return clientBuilder.cache(cache).build();


    }
    private synchronized void showLog(String str) {

        int index = 0;
        int maxLength = 1800;
        String finalString = "";
        str = str.trim();

        while (index < str.length()) {
            if (str.length() <= index + maxLength) {
                finalString = str.substring(index);
            } else {
                finalString = str.substring(index, index + maxLength);
            }
            index += maxLength;
            Log.d(TAG, finalString.trim());
        }
    }

    private class LogInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            StringBuffer sb = new StringBuffer();
            Response response = chain.proceed(chain.request());
            okhttp3.MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            sb.append("======== request: " + request.toString() +  "\r\n======= response header:" + response.headers().toString() + "\r\n---------- response body:\r\n");
            Log.d(TAG, sb.toString());
            try {
                showLog(content);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response.newBuilder()
                    .body(okhttp3.ResponseBody.create(mediaType, content))
                    .build();
        }
    }


    private String getValue(String reg, String data) {

        Pattern p = Pattern.compile(reg);
        Matcher match = p.matcher(data);
        if (match.find()) {
            return match.group(1);
        }
        return "";
    }

    private Map getNameAndFileName(Headers headers) {

        Map map = new HashMap();
        List<String> listName = headers.values("Content-Disposition");

        for (String data : listName) {
            if (data.indexOf("form-data") >= 0) {
                String reg = "\\sname=\"(.*?)\"";
                String v = getValue(reg, data);
                if (v.length() > 0) {
                    map.put("name", v);
                }
                reg = "\\sfilename=\"(.*?)\"";
                v = getValue(reg, data);
                if (v.length() > 0) {
                    map.put("filename", v);
                }
            }

        }
        return map;
    }

    private class HeaderInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {

            Request original = chain.request();
            Map map = new HashMap<String, String>();
            if (original.method().equals("GET")) {

                HttpUrl httpUrl = original.url();

                for (int i = 0; i < httpUrl.querySize(); i++) {
                    map.put(httpUrl.queryParameterName(i), httpUrl.queryParameterValue(i));
                }

            } else if (original.method().equals("POST")) {

                if (original.body() instanceof FormBody) {
                    FormBody.Builder newFormBody = new FormBody.Builder();
                    FormBody oidFormBody = (FormBody) original.body();
                    for (int i = 0; i < oidFormBody.size(); i++) {
                        newFormBody.addEncoded(oidFormBody.encodedName(i), oidFormBody.encodedValue(i));
                        map.put(oidFormBody.name(i), oidFormBody.value(i));
                    }
                } else if (original.body() instanceof MultipartBody) {
                    MultipartBody body = (MultipartBody) original.body();
                    for (MultipartBody.Part part : body.parts()) {

                        try {
                            Class<?> cls = MultipartBody.Part.class;
                            Field requestBody = cls.getDeclaredField("body");
                            Field headers = cls.getDeclaredField("headers");
                            requestBody.setAccessible(true);
                            headers.setAccessible(true);
                            RequestBody requestBodyObj = (RequestBody) requestBody.get(part);
                            if (requestBodyObj.contentType() == null || requestBodyObj.contentType().toString().indexOf("text") > 0) {//text
                                Buffer buffer = new Buffer();
                                requestBodyObj.writeTo(buffer);
                                String str = buffer.readUtf8();

                                Headers headersObj = (Headers) headers.get(part);
                                Map m = getNameAndFileName(headersObj);
                                map.put((String) m.get("name"), str);
                            } else {//file
                                Headers headersObj = (Headers) headers.get(part);
                                Map m = getNameAndFileName(headersObj);
                                map.put((String) m.get("name"), m.get("filename"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
            Request request = original;
            if (mIHttpHeader!=null){
                Map<String, String> headerMap =mIHttpHeader.getHttpHeader(original.url().toString(),map);
                if (headerMap!=null){
                    Request.Builder builder = original.newBuilder();
                    for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                        builder.addHeader(entry.getKey(), entry.getValue());
                    }
                    Log.d(TAG, "headers: "+headerMap.toString());

                    request = builder
                            .method(original.method(), original.body())
                            .build();
                }
            }
            return chain.proceed(request);
        }
    }
}
