package com.xiaoantech.imeidatasearch.event;

import com.xiaoantech.imeidatasearch.http.HttpManage;

/**
 * Created by 73843 on 2017/3/6.
 */

public class HttpGetEvent {
    protected HttpManage.getType type;
    protected String resultStr;
    protected boolean isSuccess;

    public HttpGetEvent(HttpManage.getType type, String resultStr, boolean isSuccess){
        this.type = type;
        this.resultStr = resultStr;
        this.isSuccess = isSuccess;
    }

    public String getResultStr(){
        return resultStr;
    }

    public HttpManage.getType getRequestType(){
        return type;
    }

    public boolean isSuccess(){
        return isSuccess;
    }
}
