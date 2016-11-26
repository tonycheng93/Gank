package com.sky.gank.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by tonycheng on 2016/11/26.
 */

public class NetworkUtil {

    private static ConnectivityManager sConnectivityManager = null;

    public static ConnectivityManager getConnectivityManager(Context context) {
        if (sConnectivityManager == null) {
            sConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        return sConnectivityManager;
    }

    /**
     * 判断是否具有网络连接
     *
     * @param context
     * @return
     */
    public static final boolean hasNetworkConnection(Context context) {
        NetworkInfo activeNetworkInfo = getConnectivityManager(context).getActiveNetworkInfo();
        return (activeNetworkInfo != null && activeNetworkInfo.isAvailable());
    }

    /**
     * 当前网络是不是WiFi
     *
     * @param context
     * @return
     */
    public static boolean isWiFiConnected(Context context) {
        if (context != null) {
            NetworkInfo wifiNetworkInfo = getConnectivityManager(context).
                    getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiNetworkInfo != null) {
                return wifiNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
