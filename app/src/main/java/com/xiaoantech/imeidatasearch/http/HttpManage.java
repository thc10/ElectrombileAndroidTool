package com.xiaoantech.imeidatasearch.http;

import android.util.Log;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.xiaoantech.imeidatasearch.event.HttpGetEvent;
import com.xiaoantech.imeidatasearch.event.HttpPostEvent;
import com.xiaoantech.imeidatasearch.event.HttpDeleteEvent;
import com.xiaoantech.imeidatasearch.event.RecordGetEvent;
import com.xiaoantech.imeidatasearch.utils.StreamToStringUtil;
import com.xiaoantech.imeidatasearch.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;
/**
 * Created by 73843 on 2017/3/6.
 */

public class HttpManage {
    public enum getType{
        GET_TYPE_IMEIDATA,
        GET_TYPE_BINDING,
        GET_TYPE_RECORD,

    }
    public enum RecordType{
        GET_RECORD
    }
    public enum postType{
        POST_TYPE_BINDING,
        POST_TYPE_TELEPHONE
    }
    public enum deleteType{
        DELETE_TYPE_BINDING,

    }
    public static void getHttpResult(final String url, final getType getType){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection;
                try{
                    URL getURL = new URL(url);
                    connection = (HttpURLConnection) getURL.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5*1000);
                    String result = StreamToStringUtil.StreamToString(connection.getInputStream());
                    EventBus.getDefault().post(new HttpGetEvent(getType, StringUtil.decodeUnicode(result), true));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static void postHttpResult(final String url, final postType postType, final String body){
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] bytes = body.getBytes();
                HttpURLConnection connection;
                try{
                    Log.e("Post", "send");
                    URL postURL = new URL(url);
                    connection = (HttpURLConnection) postURL.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);
                    connection.setRequestProperty("Content-Type","application/json");
                    connection.setRequestProperty("Content-Length",String.valueOf(bytes.length));
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(bytes);
                    outputStream.flush();
                    outputStream.close();
                    int response = connection.getResponseCode();
                    if (response == HttpURLConnection.HTTP_OK){
                        String result = StreamToStringUtil.StreamToString(connection.getInputStream());
                        Log.e("HttpResponse", "OK");
                        EventBus.getDefault().post(new HttpPostEvent(postType, StringUtil.decodeUnicode(result), true));
                    }else {
                        Log.e("HttpResponse", "ERROR");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static void deleteHttpResult(final String url, final deleteType deletetype) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection;
                try {
                    URL getURL = new URL(url);
                    Log.e("HttpDelete","OK");
                    connection = (HttpURLConnection) getURL.openConnection();
                    connection.setDoInput(true);
                    connection.setRequestMethod("DELETE");
                    connection.setConnectTimeout(5000);
                    connection.setUseCaches(false);        //设置不进行缓存
                    connection.setInstanceFollowRedirects(true);
                    connection.setRequestProperty("Content-Type", "application/json");
                    int response = connection.getResponseCode();
                    if (response == HttpURLConnection.HTTP_OK){
                        String result = StreamToStringUtil.StreamToString(connection.getInputStream());
                        EventBus.getDefault().post(new HttpDeleteEvent(deletetype,StringUtil.decodeUnicode(result),true));
                        Log.e("Response","OK");
                    }else {
                        connection.disconnect();
                        Log.e("Response","ERROR");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
