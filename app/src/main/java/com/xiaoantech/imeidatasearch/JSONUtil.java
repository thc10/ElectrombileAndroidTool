package com.xiaoantech.imeidatasearch;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 73843 on 2017/3/20.
 */

public class JSONUtil {
    public static String ParseJSON(String str, String name) throws JSONException {
        if(str.isEmpty()||name.isEmpty())
            return null;

        String result = null;
        try {
            JSONObject myJSobj= new JSONObject(str);
            result = myJSobj.has(name)?myJSobj.getString(name):null;
        } catch (JSONException e) {
            e.printStackTrace();
            throw e;
        }

        return result;
    }
}
