package com.xiaoantech.imeidatasearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 73843 on 2017/3/8.
 */

public class RecordSearch extends AppCompatActivity {
    private ListView Lv = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        Bundle bundle = getIntent().getExtras();
        String IMEI = bundle.getString("IMEI");
        getIMEIRecord(IMEI);
        Button button = (Button)findViewById(R.id.btn_back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordSearch.this, MainActivity.class);
                startActivity(intent);
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

    public void getIMEIRecord(String IMEI){
        if (null != IMEI){
            long curenttime = new Date().getTime();
            curenttime = curenttime/1000;
            String endtime = String.valueOf(curenttime);
            String starttime = String.valueOf(curenttime - 604800);
            /*String endtime = "1489048980";
            String starttime = "148602498";*/
            String url =   "http://api.xiaoan110.com:8083/v1/deviceEvent/" + IMEI + "?start=" + starttime + "&end=" + endtime;
            HttpManage.getRecordResult(url, HttpManage.RecordType.GET_RECORD);
        }else{

        }
    }
/*
    public String TimeUtil(int year, int mouth, int day, String Detail){
        String Year = String.valueOf(year);
        String Mouth = String.valueOf(mouth);
        while (Mouth.length() <2 ){
            Mouth = '0' + Mouth;
        }
        if (Mouth.compareTo("12") > 0){
            Mouth = "12";
        }else if (Mouth.compareTo("01") < 0){
            Mouth = "01";
        }
        String Day = String.valueOf(day);
        while (Day.length() < 2){
            Day = '0' + Day;
        }
        if (Day.compareTo("31") > 0){
            Day = "31";
        }else if (Day.compareTo("01") < 0){
            Day = "01";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = Year + "-" + Mouth + "-" + Day + " " + Detail;
        try {
            Date date = format.parse(time);
            return String.valueOf(date.getTime());
        }catch (ParseException e){
            e.printStackTrace();
        }
        return "";
    }
*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRecordGetEvent(RecordGetEvent event){
        if (event.getResultStr().indexOf("code") != -1) {
            try {
                JSONObject result = new JSONObject(event.getResultStr());
                /*if (code == 100) {
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
                }*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            try {
                SimpleAdapter adapter = new SimpleAdapter(this, getData(event.getResultStr()), R.layout.item, new String[]{"time","event"}, new int[]{R.id.txt_time, R.id.txt_event});
                Lv = (ListView)findViewById(R.id.lv);
                Lv.setAdapter(adapter);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private List<Map<String, Object>> getData(String string){
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            JSONArray jsonArray = new JSONArray(string);
            int length = jsonArray.length();
            int conter = 0;
            JSONObject jsonObject = null;
            Map<String, Object> map = new HashMap<String, Object>();
            for (;conter < length; conter++){
                map = new HashMap<String, Object>();
                jsonObject = jsonArray.getJSONObject(conter);
                Long time  = jsonObject.getLong("timestamp") * 1000;
                String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(time));
                String event = jsonObject.getString("event");
                map.put("time", date);
                map.put("event", event);
                list.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }


        return list;
    }
}
