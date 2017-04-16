package com.xiaoantech.imeidatasearch.ui.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoantech.imeidatasearch.R;
import com.xiaoantech.imeidatasearch.event.RecordGetEvent;
import com.xiaoantech.imeidatasearch.tool.zxing.android.CaptureActivity;
import com.xiaoantech.imeidatasearch.ui.activity.RecordSearch.RecordSearch;
import com.xiaoantech.imeidatasearch.event.HttpGetEvent;
import com.xiaoantech.imeidatasearch.http.HttpManage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.StreamHandler;

public class MainActivity extends AppCompatActivity {
    String IMEI = null;
    private ListView lv = null;
    private ListView lv_record = null;
    private View view;
    private Button btn_record;
    private static final int REQUEST_CODE_SCAN = 0x0000;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText editText = (EditText)findViewById(R.id.Imei_input);
        editText.setText("86506702");
        final Button button = (Button)findViewById(R.id.btn);
        view = (View)findViewById(R.id.view_scan);
        button.setClickable(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText)findViewById(R.id.Imei_input);
                button.setClickable(false);
                IMEI = (editText.getText()).toString();
                if (IMEI.length() == 15){
                    changeIMEI(IMEI);
                    showToast("正在查询");
                    getIMEIData(IMEI);
                }else {
                    showToast("请输入正确的IMEI号");
                    button.setClickable(true);
                }
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
            }
        });
        btn_record = (Button)findViewById(R.id.btn_record);
        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IMEI == null || IMEI.length() != 15){
                    showToast("请输入IMEI号");
                }else{
                    Intent intent = new Intent(MainActivity.this, RecordSearch.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("IMEI", IMEI);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        String content = data.getStringExtra(DECODED_CONTENT_KEY);
        Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
        try{
            JSONObject result = new JSONObject(content);
            this.IMEI = result.getString("IMEI");
            if (IMEI.length() == 15){
                EditText editText = (EditText)findViewById(R.id.Imei_input);
                editText.setText(IMEI);
                getIMEIData(result.getString("IMEI"));
            }else {
                showToast("IMEI错误");
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            final String imei = bundle.getString("IMEI");
            if (null != imei){
                this.IMEI = imei;
                getIMEIData(IMEI);
                EditText editText = (EditText)findViewById(R.id.Imei_input);
                editText.setText(IMEI);
            }
        }
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

    public String getIMEI(){
        return this.IMEI;
    }

    public void changeIMEI(String IMEI){
        this.IMEI = IMEI;
    }

    public void getIMEIData(String IMEI){
        if (null != IMEI){
            String url =   "http://api.xiaoan110.com:8083/v1/imeiData/" + IMEI;
            HttpManage.getHttpResult(url, HttpManage.getType.GET_TYPE_IMEIDATA);
            /*Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.MILLISECOND, 001);
            long starttime = cal.getTimeInMillis()/1000;
            long endtime = new Date().getTime()/1000;
            url =   "http://api.xiaoan110.com:8083/v1/deviceEvent/" + IMEI + "?start=" + starttime + "&end=" + endtime;
            HttpManage.getRecordResult(url, HttpManage.RecordType.GET_RECORD);*/
        }else{
           showToast("请输入IMEI号");
        }
    }

    public void showToast(String errMsg){
        Toast.makeText(MainActivity.this, errMsg, Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpGetEvent(HttpGetEvent event){
        if (event.getResultStr().indexOf("code") != -1){
            String ErrMsg = "";
            try {
                JSONObject result = new JSONObject(event.getResultStr());
                int code = result.getInt("code");
                if (code == 100){
                    ErrMsg = "服务器内部错误";
                }else if (code == 101){
                    ErrMsg = "请求无IMEI";
                }else if (code == 102){
                    ErrMsg = "无请求内容";
                }else if (code == 103){
                    ErrMsg = "请输入正确的IMEI号";
                }else if (code == 104){
                    ErrMsg = "请求URL错误";
                }else if (code == 105){
                    ErrMsg = "请求范围过大";
                }else if (code == 106){
                    ErrMsg = "服务器无响应";
                }else if (code == 107){
                    ErrMsg = "服务器不在线";
                }else if (code == 108){
                    ErrMsg = "设备无响应";
                }else if (code == 109){
                    ErrMsg = "未登录";
                }else if (code == 110){
                    ErrMsg = "操作设备不成功";
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
            SimpleAdapter adapter = new SimpleAdapter(this, getError(ErrMsg), R.layout.item_data, new String[]{"txt_dataName", "txt_dataMsg"}, new int[]{R.id.txt_dataName, R.id.txt_dataMsg});
            lv = (ListView)findViewById(R.id.lv_data);
            lv.setAdapter(adapter);
            showToast("查询成功");
            /*TextView textView = (TextView)findViewById(R.id.txt_Imei);
            textView.setText("");
            textView = (TextView)findViewById(R.id.txt_version);
            textView.setText("");
            textView = (TextView)findViewById(R.id.txt_time);
            textView.setText("");
            textView = (TextView)findViewById(R.id.txt_latANDlon);
            textView.setText("");
            textView = (TextView)findViewById(R.id.txt_course);
            textView.setText("");
            textView = (TextView)findViewById(R.id.txt_speed);
            textView.setText("");
            textView = (TextView)findViewById(R.id.txt_GSM);
            textView.setText("");
            textView = (TextView)findViewById(R.id.txt_MAXGSM);
            textView.setText("");
            textView = (TextView)findViewById(R.id.txt_voltage);
            textView.setText("");*/
            Button button = (Button)findViewById(R.id.btn);
            button.setClickable(true);
        }else{
            Button button = (Button)findViewById(R.id.btn);
            button.setClickable(true);
            SimpleAdapter adapter = new SimpleAdapter(this, getData(event.getResultStr()), R.layout.item_data, new String[]{"txt_dataName", "txt_dataMsg"}, new int[]{R.id.txt_dataName, R.id.txt_dataMsg});
            lv = (ListView)findViewById(R.id.lv_data);
            lv.setAdapter(adapter);
        }
    }

    private Map<String, Object> createDataMap(String txt_dataName, String txt_dataMsg){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("txt_dataName", txt_dataName);
        map.put("txt_dataMsg", txt_dataMsg);
        return map;
    }

    private List<Map<String, Object>> getError(String string){
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list.add(createDataMap("Error", string));
        return list;
    }

    private List<Map<String, Object>> getData(String string) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            JSONObject result = new JSONObject(string);
            list.add(createDataMap("IMEI", result.getString("imei")));

            int version = result.getInt("version");
            String version_a = String.valueOf(version / 65536);
            String version_b = String.valueOf((version % 65536) / 256);
            String version_c = String.valueOf(version % 256);
            String Version = version_a + '.' + version_b + '.' + version_c;
            list.add(createDataMap("版本", Version));

            int state = result.getInt("state");
            String State = "";
            if (state == 1) {
                State = "online";
            } else {
                State = "offline";
            }
            list.add(createDataMap("状态", State));

            Long time = result.getLong("timestamp") * 1000;
            String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(time));
            list.add(createDataMap("时间", date));


            String latANDlon = Double.toString(result.getDouble("latitude")) + "," + Double.toString(result.getDouble("longitude"));
            list.add(createDataMap("经纬度", latANDlon));

            String GSM = Integer.toString(result.getInt("GSM"));
            list.add(createDataMap("GSM信号", GSM));

            /*textView.setText(GSM);
            if (gsm == 0){
                textView.setBackgroundColor(Color.parseColor("#DCDCDC"));
            }else if (gsm <10){
                textView.setBackgroundColor(Color.parseColor("#FF0000"));
            }else if (gsm < 20){
                textView.setBackgroundColor(Color.parseColor("#ffff00"));
            }else if (gsm >= 20){
                textView.setBackgroundColor(Color.parseColor("#ADFF2F"));
            }*/

            String MAXGSM = Integer.toString(result.getInt("MAXGSM"));
            list.add(createDataMap("MAXGSM", MAXGSM));
            /*textView.setText(MAXGSM);
            if (maxgsm == 0){
                textView.setBackgroundColor(Color.parseColor("#DCDCDC"));
            }else if (maxgsm <10){
                textView.setBackgroundColor(Color.parseColor("#FF0000"));
            }else if (maxgsm < 20){
                textView.setBackgroundColor(Color.parseColor("#ffff00"));
            }else if (maxgsm >= 20){
                textView.setBackgroundColor(Color.parseColor("#ADFF2F"));
            }*/

            String Voltage = Integer.toString(result.getInt("voltage"));
            list.add(createDataMap("电压", Voltage));

            String Course = Integer.toString(result.getInt("course"));
            list.add(createDataMap("方向", Course));

            String Speed = Integer.toString(result.getInt("speed"));
            list.add(createDataMap("速度", Speed));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /*@Subscribe(threadMode = ThreadMode.MAIN)
    public void onRecordGetEvent(RecordGetEvent event){
        if (event.getResultStr().indexOf("code") != -1) {
            try {
                JSONObject result = new JSONObject(event.getResultStr());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            try {
                SimpleAdapter adapter = new SimpleAdapter(this, getRecord(event.getResultStr()), R.layout.item, new String[]{"time","event"}, new int[]{R.id.txt_time, R.id.txt_event});
                lv_record = (ListView)findViewById(R.id.lv_record);
                lv_record.setAdapter(adapter);
                showToast("查询成功");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private List<Map<String, Object>> getRecord(String string){
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            JSONArray jsonArray = new JSONArray(string);
            int length = jsonArray.length();
            Map<String, Object> map = new HashMap<String, Object>();
            if (length == 0){
                map = new HashMap<String, Object>();
                map.put("time", "本日无设备日志");
                list.add(map);
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
        return list;
    }*/
}
