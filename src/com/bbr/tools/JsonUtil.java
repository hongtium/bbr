package com.bbr.tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Seriously, you need say something about your bullshit.
 * <pre>
 * User: chenjx
 * Date: 11-5-4
 * </pre>
 */
public class JsonUtil {
    private final static String STATUS_KEY = "status";
    private final static String MESSAGE_KEY = "message";
    private final static String RESULT_KEY = "result";

    /**
     * return json data
     *
     * @param status  status code,0:成功,>0:失败
     * @param message human readable message
     * @param data    key-value data
     * @return json format string
     */
    public static String toJson(int status, String message, Map<String, Object> data) {
        return getJson(status, message, data).toString();
    }

    /**
     * return json data
     *
     * @param status  status code,0:成功,>0:失败
     * @param message human readable message
     * @param data    key-value data
     * @return json object
     */
    public static JSONObject getJson(int status, String message, Map<String, Object> data) {
        JSONObject json = new JSONObject();
        json.accumulate(STATUS_KEY, status).accumulate(MESSAGE_KEY, message);
        if (null != data) {
            json.accumulateAll(data);
        }
        return json;
    }

    /**
     * return json data
     *
     * @param status status code,0:成功,>0:失败
     * @return json object
     */
    public static JSONObject getJson(int status) {
        JSONObject json = new JSONObject();
        json.accumulate(STATUS_KEY, status);
        return json;
    }


    /**
     * return json data
     *
     * @param status  status code,0:成功,>0:失败
     * @param message human readable message
     * @return json object
     */
    public static JSONObject getJson(int status, String message) {
        JSONObject json = new JSONObject();
        json.accumulate(STATUS_KEY, status).accumulate(MESSAGE_KEY, message);
        return json;
    }

    /**
     * return json data
     *
     * @param status  status code,0:成功,>0:失败
     * @param message human readable message
     * @return json format string
     */
    public static String toJson(int status, String message) {
        return getJson(status, message).toString();
    }

    /**
     * return json data
     *
     * @param status status code,0:成功,>0:失败
     * @param keys   key set
     * @param values values associated with each key
     * @return json format string
     */
    public static String toJson(int status, String message, String[] keys, String[] values) {
        JSONObject json = getJson(status, message, null);
        json.accumulateAll(join(keys, values));
        return json.toString();
    }

    public static String toJson(int status, String message, List<? extends Object> list) {
        return toJson(status, message, RESULT_KEY, list);
    }

    public static String toJson(int status, String message, String name, List<? extends Object> list) {
        JSONObject json = StringUtils.isEmpty(message) ? getJson(status) : getJson(status, message);
        json.accumulate(name, getJson(list));
        return json.toString();
    }

    public static String toJson(List<? extends Object> o) {
        JSONObject json = new JSONObject();
        json.accumulate(RESULT_KEY, getJson(o));
        return json.toString();
    }

    public static JSONArray getJson(List<? extends Object> objects) {
        JSONArray json = new JSONArray();
        JsonConfig config = new JsonConfig();
        config.setExcludes(new String[]{"rateLimitLimit", "rateLimitRemaining", "rateLimitReset", "remark"});
        return json.fromObject(objects, config);
    }

    /**
     * to key:value,key:value
     *
     * @param keys
     * @param values
     * @return
     */
    private static JSONObject join(String[] keys, String[] values) {
        JSONObject json = new JSONObject();
        if (null != keys && null != values && keys.length == values.length) {
            for (int i = 0; i < keys.length; i++) {
                String key = keys[i];
                String value = values[i];
                if (!(StringUtils.isEmpty(key) || StringUtils.isEmpty(value))) {
                    json.accumulate(key, value);
                }
            }
        }
        return json;
    }
}
