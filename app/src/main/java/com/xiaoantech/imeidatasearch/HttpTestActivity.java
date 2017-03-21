package com.xiaoantech.imeidatasearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
        Button btn_gettelnum =(Button)findViewById(R.id.btn_get_telnum);
        btn_gettelnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhonenum(IMEI);
            }
        });
        Button btn_phoneAlarm = (Button)findViewById(R.id.btn_test);
        btn_phoneAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAlarm(IMEI);
            }
        });
        Button btn_setTelNum = (Button)findViewById(R.id.btn_setTelNum);
        btn_setTelNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText)findViewById(R.id.input_tel);
                String tel = editText.getText().toString();
                setPhoneAlarm(IMEI, tel);
            }
        });
        Button btn_delPhoneNum = (Button)findViewById(R.id.btn_delTelNum);
        btn_delPhoneNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tel = "15973324498";
                delPhoneNum(IMEI, tel);
            }
        });
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

    public void PhoneAlarm(String IMEI){
        if (null != IMEI){
            String url =   "http://api.xiaoan110.com:8083/v1/telephone/" + IMEI;
            String body = "{\"caller\":\"0\"}";
            HttpManage.putPhoneAlarm(url, body, HttpManage.putType.PUT_TYPE_PHONEALRAM);
            TextView textView = (TextView)findViewById(R.id.txt_state);
            textView.setText("send put seccusse!");
        }

    }

    public void setPhoneAlarm(String IMEI, String tel){
        if (null != IMEI){
            String url =   "http://api.xiaoan110.com:8083/v1/telephone/" + IMEI;
            String body = "{\"telephone\":\"" + tel + "\"}";
            HttpManage.setPhoneAlarm(url, body, HttpManage.postType.POST_TYPE_SETPHONENUM);
            TextView textView = (TextView)findViewById(R.id.txt_state);
            textView.setText("send post seccusse!");
        }
    }

    public void delPhoneNum(String IMEI, String tel){
        if (null != IMEI){
            String url =   "http://api.xiaoan110.com:8083/v1/telephone/" + IMEI;
//            String body = "{\"telephone \":\"" + tel + "\"}";
            String body = "{\"telephone\":\"15973324498\"}";
            /*TextView textView1 = (TextView)findViewById(R.id.txt);
            textView1.setText(body);*/
            HttpManage.delPhoneNum(url, body, HttpManage.deleteType.DELETE_TYPE_PHONENUM);
            TextView textView = (TextView)findViewById(R.id.txt_state);
            textView.setText("send delete seccusse!"+body);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPhoneAlramEvent(PhoneAlramEvent event){
        if (event.getResultStr().indexOf("code") != -1) {
            try {
                JSONObject result = new JSONObject(event.getResultStr());
                TextView textView = (TextView)findViewById(R.id.txt);
                textView.setText(event.getResultStr());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{

        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPhoneNumDelEvent(PhoneNumDelEvent event){
        if (event.getResultStr().indexOf("code") != -1) {
            try {
                JSONObject result = new JSONObject(event.getResultStr());
                TextView textView = (TextView)findViewById(R.id.txt);
                textView.setText(event.getResultStr());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            TextView textView = (TextView)findViewById(R.id.txt);
            textView.setText(event.getResultStr());
        }
    }
}
