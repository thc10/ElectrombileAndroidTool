package com.xiaoantech.imeidatasearch;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 73843 on 2017/3/8.
 */

public class RecordSearch extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Button button = (Button)findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIMEIRecord();
            }
        });
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

    public void getIMEIRecord(){
        EditText editText = (EditText)findViewById(R.id.Imeiinput);
        String IMEI = (editText.getText()).toString();
        if (null != IMEI){
            String starttime = "1488947000";
            String endtime = "1488964282";
            String url =   "http://api.xiaoan110.com:8083/v1/deviceEvent/" + IMEI + "?start=" + starttime + "&end=" + endtime;
            HttpManage.getRecordResult(url, HttpManage.RecordType.GET_RECORD);
        }else{

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRecordGetEvent(RecordGetEvent event){
        if (event.getResultStr().indexOf("code") != -1) {
            try {
                JSONObject result = new JSONObject(event.getResultStr());
                TextView textView = (TextView) findViewById(R.id.txt_Event1);
                int code = result.getInt("code");
                if (code == 100) {
                    textView.setText("服务器内部错误");
                } else if (code == 101) {
                    textView.setText("请求无IMEI");
                } else if (code == 102) {
                    textView.setText("无请求内容");
                } else if (code == 103) {
                    textView.setText("请求内容错误");
                } else if (code == 104) {
                    textView.setText("请求URL错误");
                } else if (code == 105) {
                    textView.setText("请求范围过大");
                } else if (code == 106) {
                    textView.setText("simcom服务器无响应");
                } else if (code == 107) {
                    textView.setText("simcom服务器不在线");
                } else if (code == 108) {
                    textView.setText("设备无响应");
                } else if (code == 109) {
                    textView.setText("未登录");
                } else if (code == 110) {
                    textView.setText("操作设备不成功");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            TextView textView = (TextView) findViewById(R.id.txt_Event1);
            textView.setText("");
            textView = (TextView) findViewById(R.id.title_Event1);
            textView.setText("");
            textView = (TextView) findViewById(R.id.txt_Event2);
            textView.setText("");
            textView = (TextView) findViewById(R.id.title_Event2);
            textView.setText("");
            textView = (TextView) findViewById(R.id.txt_Event3);
            textView.setText("");
            textView = (TextView) findViewById(R.id.title_Event3);
            textView.setText("");
            textView = (TextView) findViewById(R.id.txt_Event4);
            textView.setText("");
            textView = (TextView) findViewById(R.id.title_Event4);
            textView.setText("");
            textView = (TextView) findViewById(R.id.txt_Event5);
            textView.setText("");
            textView = (TextView) findViewById(R.id.title_Event5);
            textView.setText("");
            textView = (TextView) findViewById(R.id.txt_Event6);
            textView.setText("");
            textView = (TextView) findViewById(R.id.title_Event6);
            textView.setText("");
            textView = (TextView) findViewById(R.id.txt_Event7);
            textView.setText("");
            textView = (TextView) findViewById(R.id.title_Event7);
            textView.setText("");
            textView = (TextView) findViewById(R.id.txt_Event8);
            textView.setText("");
            textView = (TextView) findViewById(R.id.title_Event8);
            textView.setText("");
            textView = (TextView) findViewById(R.id.txt_Event9);
            textView.setText("");
            textView = (TextView) findViewById(R.id.title_Event9);
            textView.setText("");
            textView = (TextView) findViewById(R.id.txt_Event10);
            textView.setText("");
            textView = (TextView) findViewById(R.id.title_Event10);
            textView.setText("");
        }else{
            try {
                JSONArray jsonArray = new JSONArray(event.getResultStr());
                TextView textView = (TextView)findViewById(R.id.title_Event1);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                Long time  = jsonObject.getLong("timestamp") * 1000;
                String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(time));
                textView.setText(date);
                textView = (TextView)findViewById(R.id.txt_Event1);
                String string = jsonObject.getString("event");
                textView.setText(string);

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
