package com.songjian.recoface.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ResponseHandlerInterface;

/**
 * Created by Jason Sun on 2015/11/11.
 * 网络交互工具类
 * <p/>
 * 邮箱：1129574214@qq.com
 */
public class HttpUtils {
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, ResponseHandlerInterface responseHandle) {
        client.get(Constant.BASEURL + url, responseHandle);
    }
    public static void getImage(String url,ResponseHandlerInterface responseHandle){
        client.get(url,responseHandle);
    }
    public static boolean isNetWorkConnected(Context context){
        if (context!=null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo!=null){
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
