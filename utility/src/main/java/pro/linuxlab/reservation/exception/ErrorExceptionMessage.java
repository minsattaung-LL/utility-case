package pro.linuxlab.reservation.exception;

import pro.linuxlab.reservation.BaseResponse;

public class ErrorExceptionMessage extends BaseResponse {
    public ErrorExceptionMessage(String status, String message) {
        super(status, null, message);
    }
}
