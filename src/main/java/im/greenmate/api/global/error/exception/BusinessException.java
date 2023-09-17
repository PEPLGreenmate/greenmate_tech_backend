package im.greenmate.api.global.error.exception;

import im.greenmate.api.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode error;

    public BusinessException(ErrorCode e) {
        super(e.getMessage());
        this.error = e;
    }
}