package com.xiaoantech.imeidatasearch;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;
import com.xiaoantech.imeidatasearch.CMDEvent;

/**
 * Created by 73843 on 2017/3/6.
 */

public class StatusEvent extends CMDEvent{
    public StatusEvent(EventBusConstant.cmdType type, JSONObject jsonObject){
        this.cmdType = type;
        this.jsonObject = jsonObject;
    }
}

