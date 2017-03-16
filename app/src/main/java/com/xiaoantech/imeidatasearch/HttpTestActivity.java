package com.xiaoantech.imeidatasearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 73843 on 2017/3/8.
 */

public class HttpTestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_httptest);
        Bundle bundle = getIntent().getExtras();
        final String IMEI = bundle.getString("IMEI");
        getPhonenum(IMEI);

    }

    public void getIMEIRoute(String IMEI){
        if (null != IMEI){
            long curenttime = new Date().getTime();
            curenttime = curenttime/1000;
            String endtime = String.valueOf(curenttime);
            String starttime = String.valueOf(curenttime - 604800);
            /*String endtime = "1489048980";
            String starttime = "148602498";*/
            String url =   "http://api.xiaoan110.com:8083/v1/itinerary/" + IMEI + "?start=" + starttime + "&end=" + endtime;
            HttpManage.getRouteResult(url, HttpManage.getType.GET_TYPE_ROUTE);
            TextView textView = (TextView)findViewById(R.id.txt);
            textView.setText("send seccusse!");
        }else{

        }
    }

    public void getPhonenum(String IMEI){
        if (null != IMEI){
            String url =   "http://api.xiaoan110.com:8083/v1/telephone/" + IMEI;
            HttpManage.getPhoneResult(url, HttpManage.getType.GET_TYPE_PHONE);
            TextView textView = (TextView)findViewById(R.id.txt);
            textView.setText("send get seccusse!");
        }else{

        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        subscribe();
    }

    @Override
    protected void onPause(){
        super.onPause();
        unsubscribe();
    }

    public void subscribe(){
        EventBus.getDefault().register(this);
    }

    public void unsubscribe() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRouteGetEvent(RouteGetEvent event){
        if (event.getResultStr().indexOf("code") != -1) {
            try {
                JSONObject result = new JSONObject(event.getResultStr());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            try {
                JSONObject result = new JSONObject(event.getResultStr());
                TextView textView = (TextView)findViewById(R.id.txt);
                textView.setText(event.getResultStr());
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPhoneGetEvent(PhoneGetEvent event){
        if (event.getResultStr().indexOf("code") != -1) {
            try {
                JSONObject result = new JSONObject(event.getResultStr());
                TextView textView = (TextView)findViewById(R.id.txt);
                textView.setText("error");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            try {
                JSONObject result = new JSONObject(event.getResultStr());
                TextView textView = (TextView)findViewById(R.id.txt);
                textView.setText(event.getResultStr());
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
