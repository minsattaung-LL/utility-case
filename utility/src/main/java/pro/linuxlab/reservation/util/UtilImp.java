package pro.linuxlab.reservation.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pro.linuxlab.reservation.BaseResponse;
import pro.linuxlab.reservation.Translator;
import pro.linuxlab.reservation.exception.ErrorCode;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


@Component
@Slf4j
public class UtilImp implements Util{

    ObjectMapper objectMapper;
    static Integer paddingLength = 5;

    public UtilImp() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        this.objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        this.objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }


    @Override
    public String toJson(Object object) {
//        ObjectMapper ow = new ObjectMapper();
        String json;
        try {
            json = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.warn("Cannot convert java object to json string", e);
            json = object.toString();
        }
        return json;
    }

    @Override
    public String generateID (String prefix, Long count) {
        return prefix + "-" + String.format("%0" + paddingLength + "d", count);
    }

    @Override
    public String toJsonPretty(Object object) {
        String json;
        try {
            json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.warn("Cannot convert java object to json string", e);
            json = object.toString();
        }
        return json;
    }
    @Override
    public BaseResponse generateDefaultResponse(Object result) {
        return new BaseResponse(ErrorCode.Business.SUCCESS, result, Translator.toLocale(ErrorCode.Business.SUCCESS));
    }
    @Override
    public <T> T toObject(String json, Class t) {
        try {
            return (T) objectMapper.readValue(json, t);
        } catch (JsonProcessingException e) {
            log.error("Cannot convert to object", e);
        }
        return null;
    }
    @Override
    public Map<String, Object> convertObjectToMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(obj);
                map.put(field.getName(), value);
            } catch (IllegalAccessException e) {
                e.printStackTrace(); // Handle the exception according to your needs
            }
        }
        return map;
    }
}
