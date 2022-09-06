package tech.outspace.papershare.utils.convert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConvert {
    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
    }

    public static ObjectMapper instance() {
        return mapper;
    }

    public static String toJson(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }

    public static <T> T toObject(String json, Class<T> type) throws JsonProcessingException {
        return mapper.readValue(json, type);
    }
}
