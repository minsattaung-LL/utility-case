package pro.linuxlab.reservation.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorCommon extends RuntimeException{
    private String errorCode;
    private String message;
}
