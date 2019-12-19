package com.liupeidong.spring.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.*;

/**
 * @creator liupeidong
 * @createTime 2019-11-27 17:09
 * @description
 **/
public class JsonMapper {
    public JsonMapper() {
    }

    public Map<String, Object> convertToMap(String json) {
        return (Map) JSON.parseObject(json, Map.class);
    }

    public Map<String, Object> convertToMap(Object obj) {
        return this.convertToMap(this.convertToJson(obj));
    }

    public String convertToJson(Object obj) {
        return JSON.toJSONString(obj);
    }

    public List<Map<String, Object>> convertToList(Collection<String> jsons) {
        List<Map<String, Object>> list = new ArrayList();
        Iterator var3 = jsons.iterator();

        while (var3.hasNext()) {
            String json = (String) var3.next();
            if (json != null) {
                list.add(this.convertToMap(json));
            }
        }

        return list;
    }

    public static void main(String[] args){
        JsonMapper jm = new JsonMapper();
        String st = "{\"first\": \"未来\", \"second\": {\"third\": \"过去\"}}";
        // JSONObject jo = JSONObject.parseObject(st);
        // System.out.println(jo);
        Map<String, Object> dataJson = jm.convertToMap(st);
        System.out.println(((JSONObject)dataJson.get("second")).getString("third"));
    }
}
