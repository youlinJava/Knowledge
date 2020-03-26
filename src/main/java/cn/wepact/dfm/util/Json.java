package cn.wepact.dfm.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Json {
    /**
     * catch异常
     */
    public static String toJson(Object object)  {
        try {
            ObjectMapper om = new ObjectMapper();
            return om.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toJsonString(Object object) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        return om.writeValueAsString(object);
    }

    public static Map toMap(String json) throws IOException {
        ObjectMapper om = new ObjectMapper();
        return om.readValue(json, HashMap.class);
    }

    public static <T> T fromJson(String json, Class<T> objectClass) throws IOException {
        ObjectMapper om = new ObjectMapper();
        return om.readValue(json, objectClass);
    }
}
