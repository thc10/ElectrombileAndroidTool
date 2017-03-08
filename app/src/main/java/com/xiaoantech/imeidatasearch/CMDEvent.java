package com.xiaoantech.imeidatasearch;

import org.json.JSONObject;

/**
 * Created by 73843 on 2017/3/6.
 */

public class CMDEvent {
    protected EventBusConstant.cmdType     cmdType;
    protected JSONObject jsonObject;

    public JSONObject getJsonObject(){
        return jsonObject;
    }

    public EventBusConstant.cmdType getCmdType(){
        return cmdType;
    }
}
