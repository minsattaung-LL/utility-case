package pro.linuxlab.reservation.logging;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoggingService implements ILogging {
    private static final Logger logger = LoggerFactory.getLogger(LoggingService.class.getName());
    private final ObjectMapper mapper;

    public LoggingService() {
        this.mapper = new ObjectMapper();
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.mapper.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
        this.mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        this.mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    }

    @Override
    public void logRequest(HttpServletRequest httpServletRequest, Object body) {
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, String> parameters = buildParametersMap(httpServletRequest);
        stringBuilder.append("REQUEST \n");
        stringBuilder.append("METHOD=[").append(httpServletRequest.getMethod()).append("] \n");
        stringBuilder.append("PATH=[").append(httpServletRequest.getRequestURI()).append("] \n");
        stringBuilder.append("HEADERS=[").append(buildHeadersMap(httpServletRequest)).append("] \n");

        if (!parameters.isEmpty()) {
            stringBuilder.append("PARAMETERS=[").append(parameters).append("] ");
        }

        if (body != null) {
            try {
                String reqContent = mapper.writeValueAsString(body);
                stringBuilder.append("BODY=[").append(reqContent).append("]");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        logger.info(stringBuilder.toString());
    }

    @Override
    public void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object body) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!httpServletRequest.getRequestURI().contains("prometheus")) {
            stringBuilder.append("RESPONSE \n");
            stringBuilder.append("METHOD=[").append(httpServletRequest.getMethod()).append("] \n");
            stringBuilder.append("PATH=[").append(httpServletRequest.getRequestURI()).append("] \n");
            stringBuilder.append("RESPONSE-HEADERS=[").append(buildHeadersMap(httpServletResponse)).append("] \n");
            try {
                String responseBody = mapper.writeValueAsString(body);
                stringBuilder.append("RESPONSE-BODY=[").append(responseBody).append("] ");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            logger.info(stringBuilder.toString());
        }
    }

    private Map<String, String> buildParametersMap(HttpServletRequest httpServletRequest) {
        Map<String, String> resultMap = new HashMap<>();
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = httpServletRequest.getParameter(key);
            resultMap.put(key, value);
        }

        return resultMap;
    }

    private Map<String, String> buildHeadersMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }

    private Map<String, String> buildHeadersMap(HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();

        Collection<String> headerNames = response.getHeaderNames();
        for (String header : headerNames) {
            map.put(header, response.getHeader(header));
        }

        return map;
    }
}
