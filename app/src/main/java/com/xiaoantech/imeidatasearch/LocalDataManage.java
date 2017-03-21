package com.xiaoantech.imeidatasearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by 73843 on 2017/3/6.
 */

public class LocalDataManage {
    private final String SHARE_PREFERENCES = "SHARE_PREFERENCES";

    private final String MapType = "MapType";

    private final String Sex = "Sex";
    private final String Birth = "Birth";
    private final int    DefaultBirth = 19700101;
    private final String UserName = "UserName";
    private final String NickName = "NickName";
    private final String IdentityNum = "IdentityNum";

    private final String IMEI = "IMEI";
    private final String IMEIList = "IMEIList";
    private final String CarInfoList = "CarInfoList";

    private final String MQTTHost = "MQTTHost";
    private final String MQTTPort = "MQTTPort";
    private final String HTTPHost = "HTTPHost";
    private final String HTTPPort = "HTTPPort";

    public final String MQTTHost_Release = "mqtt.xiaoan110.com";
    public final String MQTTPort_Release = "1883";
    public final String HTTPHost_Release = "http://api.xiaoan110.com";
    public final String HTTPPort_Release = "8083";

    public final String MQTTHost_Test = "test.xiaoan110.com";
    public final String MQTTPort_Test = "1883";
    public final String HTTPHost_Test = "http://test.xiaoan110.com";
    public final String HTTPPort_Test = "8081";

    private String imei;

    private final String AutoLock = "AutoLock";
    private final String AutoLockPeriod = "AutoLockPeriod";

    private  static LocalDataManage mInstance = null;

    public static LocalDataManage getInstance() {
        if (mInstance == null){
            mInstance = new LocalDataManage();
        }
        return mInstance;
    }

    private SharedPreferences sharedPreferences;

    public void setIMEI(String imei) {
        sharedPreferences.edit().putString(IMEI,imei).apply();
    }

    public String getimei(){
        return this.imei;
    }

    public void setimei(String imei){
        this.imei = imei;
    }

    public String getIMEI() {
        return sharedPreferences.getString(IMEI,"");
    }

    public void setIMEIList(List<String> imeiList) {
        if (imeiList == null){
            sharedPreferences.edit().putString(IMEIList," ").apply();
            return;
        }

        JSONArray jsonArray = new JSONArray();
        for (String IMEI: imeiList){
            jsonArray.put(IMEI);
        }
        sharedPreferences.edit().putString(IMEIList,jsonArray.toString()).apply();
    }

    public List<String> getIMEIList(){
        List<String> imeiList = new ArrayList<>();
        JSONArray jsonArray;
        try{
            jsonArray = new JSONArray(sharedPreferences.getString(IMEIList," "));
            for (int i = 0; i < jsonArray.length(); i++){
                imeiList.add(jsonArray.getString(i));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return imeiList;
    }

    public void setMQTTHost(String mqttHost){
        sharedPreferences.edit().putString(MQTTHost,mqttHost).apply();
    }
    public String getMQTTHost() {
        return sharedPreferences.getString(MQTTHost,MQTTHost_Test);
    }

    public void setMQTTPort(String mqttPort) {
        sharedPreferences.edit().putString(MQTTPort,mqttPort).apply();
    }
    public String getMQTTPort() {
        return sharedPreferences.getString(MQTTPort,MQTTPort_Test);
    }

    public void setHTTPHost(String httpHost) {
        sharedPreferences.edit().putString(HTTPHost,httpHost).apply();
    }
    public String getHTTPHost(){
        return sharedPreferences.getString(HTTPHost,HTTPHost_Test);
    }
    public void setHTTPPort(String httpPort){
        sharedPreferences.edit().putString(HTTPPort,httpPort).apply();
    }
    public String getHTTPPort(){
        return sharedPreferences.getString(HTTPPort,HTTPPort_Test);
    }


    public void setSex(boolean isMale){
        sharedPreferences.edit().putBoolean(Sex,isMale).apply();
    }

    public boolean getSex(){
        return sharedPreferences.getBoolean(Sex,true);
    }

    //Birth存储并没有按照时间戳存储，而是使用8位数存储，格式为yyyymmdd,使用时再进行解析
    public void setBirth(int birth){
        sharedPreferences.edit().putInt(Birth,birth).apply();
    }

    public int getBirth(){
        return sharedPreferences.getInt(Birth,DefaultBirth);
    }

    public void setUserName(String userName) {
        sharedPreferences.edit().putString(UserName,userName).apply();
    }

    public String getUserName(){
        return sharedPreferences.getString(UserName,"名称");
    }

    public void setNickName(String nickName){
        sharedPreferences.edit().putString(NickName,nickName).apply();
    }

    public String getNickName(){
        return sharedPreferences.getString(NickName,"昵称");
    }

    public void setIdentityNum(String identityNum){
        sharedPreferences.edit().putString(IdentityNum,identityNum).apply();
    }

    public String getIdentityNum(){
        return sharedPreferences.getString(IdentityNum,"");
    }

    public void setAutoLock(boolean isOn){
        sharedPreferences.edit().putBoolean(AutoLock,isOn).apply();
    }

    public boolean getAutoLock(){
        return sharedPreferences.getBoolean(AutoLock,false);
    }

    public void setAutoLockPeriod(int period){
        sharedPreferences.edit().putInt(AutoLockPeriod,period).apply();
    }

    public int getAutoLockPeriod(){
        return sharedPreferences.getInt(AutoLockPeriod,5);
    }
}
