package com.xiaoantech.imeidatasearch;

/**
 * Created by 73843 on 2017/3/17.
 */

public class PhoneNumDelEvent {
    protected HttpManage.deleteType type;
    protected String resultStr;
    protected boolean isSuccess;

    public PhoneNumDelEvent(HttpManage.deleteType type, String resultStr, boolean isSuccess){
        this.type = type;
        this.resultStr = resultStr;
        this.isSuccess = isSuccess;
    }

    public String getResultStr(){
        return resultStr;
    }

    public HttpManage.deleteType getRequestType(){
        return type;
    }

    public boolean isSuccess(){
        return isSuccess;
    }
}
