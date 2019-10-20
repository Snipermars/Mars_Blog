package com.liupeidong.kafkaCore.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * JsonUtils class
 * 1)
 *
 * @author liupeidong
 * @time 2019/10/6 0006 17:09
 */
public class JsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String toJsonWithFormat(Object _obj, String _dateFormat) {
        if (_obj == null) {
            return null;
        }
        String result = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            //备份老的日期格式
            //DateFormat oldFormat = objectMapper.getDateFormat();
            if (StringUtils.isNotEmpty(_dateFormat)) {
                objectMapper.setDateFormat(new SimpleDateFormat(_dateFormat));
                //不设置时区，会与系统当前时间相差8小时
                TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
                objectMapper.setTimeZone(timeZone);
            }
            result = objectMapper.writeValueAsString(_obj);
            //恢复日期格式
            //objectMapper.setDateFormat(oldFormat);
        } catch (IOException e) {
        }
        return result;
    }

    public static String object2Json(Object _obj) {
        if (_obj == null) {
            return null;
        }
        String result = null;
        try {
            result = objectMapper.writeValueAsString(_obj);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("对象转JSON字符串异常", e);
        }
        return result;
    }

    public static String object2Json(Object _obj, boolean _indented) {

        if(_obj == null) {
            return null;
        }
        String result = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            if(_indented) {
                result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(_obj);
            } else {
                result = objectMapper.writeValueAsString(_obj);
            }
        } catch (IOException e) {
            logger.error("error when object to json", e);
        }
        return result;
    }

    public static Map<?, ?> jsonToMap(String _json) {
        return json2Object(_json, Map.class);
    }

    public static <T> T json2Object(String _json, Class<T> _cls) {
        T result = null;
        try {
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            result = objectMapper.readValue(_json, _cls);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("JSON字符串转对象异常", e);
        }

        return result;
    }

    public static <T> T conveterObject(Object _srcObject, Class<T> _destObjectType) {
        String jsonContent = object2Json(_srcObject);
        return json2Object(jsonContent, _destObjectType);
    }

    public static <T> List<T> fromJsonList(String _json, Class<T> _clazz) throws IOException {
        return objectMapper.readValue(_json, objectMapper.getTypeFactory().constructCollectionType(List.class, _clazz));
    }

}
