package com.xiaoantech.imeidatasearch.ui.activity.RecordSearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoantech.imeidatasearch.R;
import com.xiaoantech.imeidatasearch.event.RecordGetEvent;
import com.xiaoantech.imeidatasearch.http.HttpManage;
import com.xiaoantech.imeidatasearch.ui.main.MainActivity;
import com.xiaoantech.imeidatasearch.utils.DateTimePickDialogUtil;
import com.xiaoantech.imeidatasearch.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 73843 on 2017/3/8.
 */

public class RecordSearch extends AppCompatActivity {
    private ListView Lv = null;
    private Button btn_startTime;
    private Button btn_endTime;
    private Button btn_search;
    private Button btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        Bundle bundle = getIntent().getExtras();
        final String IMEI = bundle.getString("IMEI");
        btn_back = (Button)findViewById(R.id.btn_back);
        btn_startTime = (Button)findViewById(R.id.btn_startTime);
        btn_endTime = (Button)findViewById(R.id.btn_endTime);
        btn_search = (Button)findViewById(R.id.btn_search);
        long curenttime = new Date().getTime();
        final String endTime = new java.text.SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new java.util.Date(curenttime));
        btn_endTime.setText(endTime);
        curenttime = curenttime/1000;
        String endtime = String.valueOf(curenttime);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 001);
        long starttime = cal.getTimeInMillis();
        final String startTime = new java.text.SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new java.util.Date(starttime));
        btn_startTime.setText(startTime);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordSearch.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("IMEI", IMEI);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        btn_startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickDialogUtil dateTimePickDialogUtil = new DateTimePickDialogUtil(RecordSearch.this, startTime);
                dateTimePickDialogUtil.dateTimePickDialog(btn_startTime);
            }
        });
        btn_endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickDialogUtil dateTimePickDialogUtil = new DateTimePickDialogUtil(RecordSearch.this, endTime);
                dateTimePickDialogUtil.dateTimePickDialog(btn_endTime);
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String starttime = btn_startTime.getText().toString();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                    Date date = simpleDateFormat.parse(starttime);
                    long startTime = date.getTime()/1000;
                    starttime = String.valueOf(startTime);
                    String endtime = btn_endTime.getText().toString();
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                    Date date1 = simpleDateFormat1.parse(endtime);
                    long endTime = date1.getTime()/1000;
                    endtime = String.valueOf(endTime);
                    Toast.makeText(RecordSearch.this, "正在查询", Toast.LENGTH_SHORT).show();
                    if (null != starttime && null != endtime){
                        getIMEIRecord(IMEI, starttime, endtime);
                    }
                }catch (ParseException e){
                    e.printStackTrace();
                }
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

    public void getIMEIRecord(String IMEI, String starttime, String endtime){
        if (null != IMEI){
            String url =   "http://api.xiaoan110.com:8083/v1/deviceEvent/" + IMEI + "?start=" + starttime + "&end=" + endtime;
            HttpManage.getRecordResult(url, HttpManage.RecordType.GET_RECORD);
        }else{
            Toast.makeText(RecordSearch.this, "请输入IMEI号", Toast.LENGTH_SHORT).show();
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRecordGetEvent(RecordGetEvent event){
        if (event.getResultStr().indexOf("code") != -1) {
            try {
                JSONObject result = new JSONObject(event.getResultStr());

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
            Map<String, Object> map = new HashMap<String, Object>();
            if (length == 0){
                map = new HashMap<String, Object>();
                map.put("time", "本日无设备日志");
                list.add(map);
                Toast.makeText(RecordSearch.this, "查询成功", Toast.LENGTH_SHORT).show();
                return list;
            }
            int conter = 0;
            JSONObject jsonObject = null;
            for (conter = length - 1;; conter--){
                map = new HashMap<String, Object>();
                jsonObject = jsonArray.getJSONObject(conter);
                Long time  = jsonObject.getLong("timestamp") * 1000;
                String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(time));
                String event = jsonObject.getString("event");
                map.put("time", date);
                map.put("event", event);
                list.add(map);
                if (conter == 0){
                    break;
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        Toast.makeText(RecordSearch.this, "查询成功", Toast.LENGTH_SHORT).show();
        return list;
    }
}
