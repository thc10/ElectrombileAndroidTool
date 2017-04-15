package com.xiaoantech.imeidatasearch.ui.main;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoantech.imeidatasearch.R;
import com.xiaoantech.imeidatasearch.event.HttpGetEvent;
import com.xiaoantech.imeidatasearch.http.HttpManage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    String IMEI = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText editText = (EditText)findViewById(R.id.Imei_input);
        editText.setText("86506702");
        final Button button = (Button)findViewById(R.id.btn);
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
            try {
                JSONObject result = new JSONObject(event.getResultStr());
                TextView textView = (TextView)findViewById(R.id.txt_state);
                textView.setText("" );
                int code = result.getInt("code");
                if (code == 100){
                    showToast("服务器内部错误");
                }else if (code == 101){
                    showToast("请求无IMEI");
                }else if (code == 102){
                    showToast("无请求内容");
                }else if (code == 103){
                    showToast("请输入正确的IMEI号");
                }else if (code == 104){
                    showToast("请求URL错误");
                }else if (code == 105){
                    showToast("请求范围过大");
                }else if (code == 106){
                    showToast("服务器无响应");
                }else if (code == 107){
                    showToast("服务器不在线");
                }else if (code == 108){
                    showToast("设备无响应");
                }else if (code == 109){
                    showToast("未登录");
                    textView.setText("未登录");
                }else if (code == 110){
                    showToast("操作设备不成功");
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
            TextView textView = (TextView)findViewById(R.id.txt_Imei);
            textView.setText("");
            textView = (TextView)findViewById(R.id.txt_version);
            textView.setText("");
            textView = (TextView)findViewById(R.id.txt_time);
            textView.setText("");
            textView = (TextView)findViewById(R.id.txt_latANDlon);
            textView.setText("");
            /*textView = (TextView)findViewById(R.id.txt_course);
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
            try {
                JSONObject result = new JSONObject(event.getResultStr());
                String IMEI = result.getString("imei");
                TextView textView = (TextView)findViewById(R.id.txt_Imei);
                textView.setText(IMEI);

                int version = result.getInt("version");
                String version_a = String.valueOf(version/65536);
                String version_b = String.valueOf((version%65536)/256);
                String version_c = String.valueOf(version%256);
                String Version = version_a + '.' + version_b + '.' + version_c;
                textView = (TextView)findViewById(R.id.txt_version);
                textView.setText(Version);

                Long time  = result.getLong("timestamp") * 1000;

                textView = (TextView)findViewById(R.id.txt_time);String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(time));
                textView.setText(date);

                String latANDlon = Double.toString(result.getDouble("latitude")) + "," +  Double.toString(result.getDouble("longitude"));
                textView = (TextView)findViewById(R.id.txt_latANDlon);
                textView.setText(latANDlon);

                /*String Course = Integer.toString(result.getInt("course"));
                textView = (TextView)findViewById(R.id.txt_course);
                textView.setText(Course);

                String Speed = Integer.toString(result.getInt("speed"));
                textView = (TextView)findViewById(R.id.txt_speed);
                textView.setText(Speed);

                int gsm = result.getInt("GSM");
                String GSM = Integer.toString(gsm);
                textView = (TextView)findViewById(R.id.txt_GSM);
                textView.setText(GSM);
                if (gsm == 0){
                    textView.setBackgroundColor(Color.parseColor("#DCDCDC"));
                }else if (gsm <10){
                    textView.setBackgroundColor(Color.parseColor("#FF0000"));
                }else if (gsm < 20){
                    textView.setBackgroundColor(Color.parseColor("#ffff00"));
                }else if (gsm >= 20){
                    textView.setBackgroundColor(Color.parseColor("#ADFF2F"));
                }

                int maxgsm = result.getInt("MAXGSM");
                String MAXGSM = Integer.toString(maxgsm);
                textView = (TextView)findViewById(R.id.txt_MAXGSM);
                textView.setText(MAXGSM);
                if (maxgsm == 0){
                    textView.setBackgroundColor(Color.parseColor("#DCDCDC"));
                }else if (maxgsm <10){
                    textView.setBackgroundColor(Color.parseColor("#FF0000"));
                }else if (maxgsm < 20){
                    textView.setBackgroundColor(Color.parseColor("#ffff00"));
                }else if (maxgsm >= 20){
                    textView.setBackgroundColor(Color.parseColor("#ADFF2F"));
                }

                String Voltage = Integer.toString(result.getInt("voltage"));
                textView = (TextView)findViewById(R.id.txt_voltage);
                textView.setText(Voltage);*/

                int state = result.getInt("state");
                String State = "";
                if (state == 1){
                    State = "online";
                }else{
                    State = "offline";
                }
                textView = (TextView)findViewById(R.id.txt_state);
                textView.setText(State);
                Button button = (Button)findViewById(R.id.btn);
                if (button.isClickable() == false){
                    button.setClickable(true);
                    showToast("查询成功");
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
