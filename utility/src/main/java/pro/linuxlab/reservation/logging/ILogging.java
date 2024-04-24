package pro.linuxlab.reservation.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface ILogging {
    void logRequest(HttpServletRequest httpServletRequest, Object body);

    void logResponse(HttpServletRequest httpServletRequest,
                     HttpServletResponse httpServletResponse,
                     Object body);
}
