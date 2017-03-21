package com.xiaoantech.imeidatasearch;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;
import com.xiaoantech.imeidatasearch.HttpGetEvent;

import com.google.zxing.common.StringUtils;
import com.xiaoantech.imeidatasearch.StreamToStringUtil;
import com.xiaoantech.imeidatasearch.StringUtil;
import org.greenrobot.eventbus.EventBus;
/**
 * Created by 73843 on 2017/3/6.
 */

public class HttpManage {
    public enum getType{
        GET_TYPE_IMEIDATA,
        GET_TYPE_RECORDE,
        GET_TYPE_ROUTE,
        GET_TYPE_PHONE
    }
    public enum putType{
        PUT_TYPE_PHONEALRAM
    }
    public enum postType{
        POST_TYPE_SETPHONENUM
    }
    public enum deleteType{
        DELETE_TYPE_PHONENUM
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
    public static void getRecordResult(final String url, final getType getType){
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
                    EventBus.getDefault().post(new RecordGetEvent(getType, StringUtil.decodeUnicode(result), true));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static void getRouteResult(final String url, final getType getType){
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
                    EventBus.getDefault().post(new RouteGetEvent(getType, StringUtil.decodeUnicode(result), true));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static void getPhoneResult(final String url, final getType getType){
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
                    EventBus.getDefault().post(new PhoneGetEvent(getType, StringUtil.decodeUnicode(result), true));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static void putPhoneAlarm(final String url, final String body, final putType putType){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection;
                try{
                    URL getURL = new URL(url);
                    connection = (HttpURLConnection) getURL.openConnection();
                    connection.setRequestMethod("PUT");
                    connection.setDoOutput(true);
                    connection.setConnectTimeout(5*1000);
                    connection.setRequestProperty("Content-Type", "application/json");
                    OutputStream os = connection.getOutputStream();
                    os.write(body.getBytes());
                    os.flush();
                    os.close();
                    String result = StreamToStringUtil.StreamToString(connection.getInputStream());
                    EventBus.getDefault().post(new PhoneAlramEvent(putType, StringUtil.decodeUnicode(result), true));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static void setPhoneAlarm(final String url, final String body, final postType postType){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection;
                try{
                    URL getURL = new URL(url);
                    connection = (HttpURLConnection) getURL.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setConnectTimeout(5*1000);
                    connection.setRequestProperty("Content-Type", "application/json");
                    OutputStream os = connection.getOutputStream();
                    os.write(body.getBytes());
                    os.flush();
                    os.close();
                    String result = StreamToStringUtil.StreamToString(connection.getInputStream());
                    EventBus.getDefault().post(new PhoneNumSetEvent(postType, StringUtil.decodeUnicode(result), true));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static void delPhoneNum(final String url, final String body, final deleteType deleteType){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection;
                try{
                    URL getURL = new URL(url);
                    connection = (HttpURLConnection) getURL.openConnection();
                    connection.setRequestMethod("DELETE");
                    connection.setDoOutput(true);
                    connection.setConnectTimeout(5*1000);
                    connection.setRequestProperty("Content-Type", "application/json");
                    OutputStream os = connection.getOutputStream();
                    os.write(body.getBytes());
                    os.flush();
                    os.close();
                    String result = StreamToStringUtil.StreamToString(connection.getInputStream());
                    EventBus.getDefault().post(new PhoneNumDelEvent(deleteType, StringUtil.decodeUnicode(result), true));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
