package com.xiaoantech.imeidatasearch.event;

import com.xiaoantech.imeidatasearch.http.HttpManage;

/**
 * Created by 73843 on 2017/4/26.
 */

public class HttpDeleteEvent {
    protected HttpManage.deleteType type;
    protected String resultStr;
    protected boolean isSuccess;

    public HttpDeleteEvent(HttpManage.deleteType type, String resultStr, boolean isSuccess){
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
