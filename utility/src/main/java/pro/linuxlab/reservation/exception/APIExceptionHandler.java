package pro.linuxlab.reservation.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import pro.linuxlab.reservation.Translator;

import javax.crypto.BadPaddingException;

@RestControllerAdvice
public class APIExceptionHandler {
    public static final Logger logger = LogManager.getLogger(APIExceptionHandler.class.getSimpleName());

    /**
     * catch app exception
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorExceptionMessage handleAllException(Exception ex, WebRequest request) {
        logger.error("Error ", ex);
        return new ErrorExceptionMessage(ErrorCode.Business.UNKNOWN, Translator.toLocale(ErrorCode.Business.UNKNOWN));
    }

    /**
     * Catch IndexOutOfBoundsException
     */
    @ExceptionHandler(IndexOutOfBoundsException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorExceptionMessage outException(Exception ex, WebRequest request) {
        logger.error("Error ", ex);
        return new ErrorExceptionMessage(ErrorCode.Business.UNKNOWN, "Wrong convert data");
    }




    /**
     * Catch validate exception
     */
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class,
            BadPaddingException.class,
            JsonProcessingException.class,
            HttpMessageNotReadableException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorExceptionMessage validateException(Exception ex, WebRequest request) {
        logger.error("Error ", ex);
        return new ErrorExceptionMessage(ErrorCode.Business.UNKNOWN, "Wrong request");
    }

    @ExceptionHandler({BusinessException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorExceptionMessage businessValidateExist(ErrorCommon ex, WebRequest request) {
        logger.error("Error ", ex);
        return new ErrorExceptionMessage(ex.getErrorCode(), ex.getMessage());
    }

/*    @ExceptionHandler({AuthenticationException.class, AccessDeniedException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorExceptionMessage handleAuthenticationException(Exception ex) {
        logger.error("Error ", ex);
        return new ErrorExceptionMessage("12333", ex.getMessage());
    }*/
}
