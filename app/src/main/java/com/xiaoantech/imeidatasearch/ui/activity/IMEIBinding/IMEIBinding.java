package com.xiaoantech.imeidatasearch.ui.activity.IMEIBinding;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xiaoantech.imeidatasearch.R;
import com.xiaoantech.imeidatasearch.event.HttpGetEvent;
import com.xiaoantech.imeidatasearch.event.HttpPostEvent;
import com.xiaoantech.imeidatasearch.http.HttpManage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by 73843 on 2017/4/26.
 */

public class IMEIBinding extends AppCompatActivity{
    private EditText txt_phone;
    private EditText txt_IMEI;
    private String PhoneNum;
    private String IMEI;
    private Button btn_Binding;
    private Button btn_Check;
    private Button btn_Delte;
    private String HTTPHost = "http://test.xiaoan110.com:8083/v1/user2dev/";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imeibinding);
        BindingData();
        btn_Binding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneNum = txt_phone.getText().toString();
                IMEI = txt_IMEI.getText().toString();
                if (PhoneNum.length() == 11 && IMEI.length() == 15){
                    String URL = createURL(PhoneNum);
                    String body = createBody(IMEI);
                    Log.e("URL", URL);
                    Log.e("body", body);
                    HttpManage.postHttpResult(URL, HttpManage.postType.POST_TYPE_BINDING, body);
                    Toast.makeText(IMEIBinding.this, "正在绑定", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneNum = txt_phone.getText().toString();
                IMEI = txt_IMEI.getText().toString();
                if (PhoneNum.length() == 11 && IMEI.length() == 15){
                    String URL = createURL(PhoneNum) + "?imei=" + IMEI;
                    HttpManage.getHttpResult(URL, HttpManage.getType.GET_TYPE_BINDING);
                }
            }
        });
        btn_Delte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneNum = txt_phone.getText().toString();
                IMEI = txt_IMEI.getText().toString();
                if (PhoneNum.length() == 11 && IMEI.length() == 15){
                    String URL = createURL(PhoneNum);

                }
            }
        });
    }

    protected void BindingData(){
        txt_phone = (EditText)findViewById(R.id.PhoneNum_Binding);
        txt_phone.setText("15973324498");
        txt_IMEI = (EditText)findViewById(R.id.IMEI_Binding);
        txt_IMEI.setText("865067022382595");
        btn_Binding = (Button)findViewById(R.id.btn_Binding);
        btn_Check = (Button)findViewById(R.id.btn_checkBinding);
        btn_Delte = (Button)findViewById(R.id.btn_deleteBinding);
    }

    public String createURL(String PhoneNum){
        String URL = HTTPHost + PhoneNum;
        return URL;
    }

    public String createBody(String IMEI){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("imei", IMEI);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return jsonObject.toString();
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
    public void onHttpPostEvent(HttpPostEvent event){
        if (event.getRequestType() == HttpManage.postType.POST_TYPE_BINDING){
            Log.e("PostResult", event.getResultStr());
            Toast.makeText(IMEIBinding.this, "绑定成功", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpGetEvent(HttpGetEvent event){
        if (event.getRequestType() == HttpManage.getType.GET_TYPE_BINDING){
            Log.e("Result", event.getResultStr());
            Toast.makeText(IMEIBinding.this, "查询成功", Toast.LENGTH_SHORT).show();
        }
    }
}
