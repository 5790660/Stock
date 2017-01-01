package com.wallstreet.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by 创宇 on 6/29/2016.
 */
public class HttpUtils {

    private static final int TIMEOUT_IN_MILLIONS = 5000;

    public static final String URL_STOCK_REAL = "https://bao.wallstreetcn.com/q/quote/v1/real";

    /**
     * @param strUrl    URL地址
     * @return  服务器返回的数据
     */
    public static String doGet(String strUrl) throws Exception {
        if (strUrl == ""){
            return null;
        }
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
            conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                int len = -1;
                byte[] buf = new byte[128];

                while ((len = is.read(buf)) != -1)
                {
                    baos.write(buf, 0, len);
                }
                baos.flush();
                return baos.toString();
            } else {
                throw new RuntimeException(" responseCode is not 200 ... ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
            } try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
            } try {
                if (conn != null)
                    conn.disconnect();
            } catch (Exception e) {
            }
        }
        return null ;
    }

    /**
     * 当前是否有网络连接
     * @return True or False
     */
    public static boolean IsNetAvailable(Context context){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }
}
