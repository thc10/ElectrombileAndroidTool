package com.xiaoantech.imeidatasearch;

/**
 * Created by 73843 on 2017/3/16.
 */

public class RouteGetEvent {
    protected HttpManage.getType type;
    protected String resultStr;
    protected boolean isSuccess;

    public RouteGetEvent(HttpManage.getType type, String resultStr, boolean isSuccess){
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
