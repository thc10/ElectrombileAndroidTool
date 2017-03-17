package com.xiaoantech.imeidatasearch;

/**
 * Created by 73843 on 2017/3/17.
 */

public class PhoneAlramEvent {
    protected HttpManage.putType type;
    protected String resultStr;
    protected boolean isSuccess;

    public PhoneAlramEvent(HttpManage.putType type, String resultStr, boolean isSuccess){
        this.type = type;
        this.resultStr = resultStr;
        this.isSuccess = isSuccess;
    }

    public String getResultStr(){
        return resultStr;
    }

    public HttpManage.putType getRequestType(){
        return type;
    }

    public boolean isSuccess(){
        return isSuccess;
    }
}
