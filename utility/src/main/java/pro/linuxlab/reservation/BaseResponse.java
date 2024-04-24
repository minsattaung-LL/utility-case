package pro.linuxlab.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
    private String errorCode;
    private Object result;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING,  pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS", timezone="Asia/Yangon")
    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    public BaseResponse(String errorCode, Object result, String message) {
        this.errorCode = errorCode;
        this.result = result;
        this.message = message;
    }
}
