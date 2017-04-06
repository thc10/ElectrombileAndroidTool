package com.xiaoantech.imeidatasearch.http;

import java.net.HttpURLConnection;
import java.net.URL;

import com.xiaoantech.imeidatasearch.event.HttpGetEvent;
import com.xiaoantech.imeidatasearch.event.RecordGetEvent;
import com.xiaoantech.imeidatasearch.utils.StreamToStringUtil;
import com.xiaoantech.imeidatasearch.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;
/**
 * Created by 73843 on 2017/3/6.
 */

public class HttpManage {
    public enum getType{
        GET_TYPE_IMEIDATA
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
    public enum RecordType{
        GET_RECORD
    }
    public static void getRecordResult(final String url, final RecordType recordType){
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
                    EventBus.getDefault().post(new RecordGetEvent(recordType, StringUtil.decodeUnicode(result), true));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
