package pro.linuxlab.reservation.exception;

import pro.linuxlab.reservation.Translator;

public class BusinessException extends ErrorCommon{
    public BusinessException(String errorCode) {
        super(errorCode, Translator.toLocale(errorCode));
    }
}
