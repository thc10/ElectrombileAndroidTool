package com.xiaoantech.imeidatasearch;

/**
 * Created by 73843 on 2017/3/17.
 */

public class PhoneNumSetEvent {
    protected HttpManage.postType type;
    protected String resultStr;
    protected boolean isSuccess;

    public PhoneNumSetEvent(HttpManage.postType type, String resultStr, boolean isSuccess){
        this.type = type;
        this.resultStr = resultStr;
        this.isSuccess = isSuccess;
    }

    public String getResultStr(){
        return resultStr;
    }

    public HttpManage.postType getRequestType(){
        return type;
    }

    public boolean isSuccess(){
        return isSuccess;
    }
}
