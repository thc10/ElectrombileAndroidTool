package com.xiaoantech.imeidatasearch.event;

import com.xiaoantech.imeidatasearch.http.HttpManage;

/**
 * Created by 73843 on 2017/3/9.
 */

public class RecordGetEvent {
    protected HttpManage.RecordType type;
    protected String resultStr;
    protected boolean isSuccess;

    public RecordGetEvent(HttpManage.RecordType type, String resultStr, boolean isSuccess){
        this.type = type;
        this.resultStr = resultStr;
        this.isSuccess = isSuccess;
    }

    public String getResultStr(){
        return resultStr;
    }

    public HttpManage.RecordType getRequestType(){
        return type;
    }

    public boolean isSuccess(){
        return isSuccess;
    }
}
