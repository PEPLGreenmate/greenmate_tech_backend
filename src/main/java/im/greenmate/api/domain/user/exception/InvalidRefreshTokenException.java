package im.greenmate.api.domain.user.exception;

import im.greenmate.api.global.error.ErrorCode;
import im.greenmate.api.global.error.exception.IllegalException;

public class InvalidRefreshTokenException extends IllegalException {
    public InvalidRefreshTokenException() {
        super(ErrorCode.INVALID_REFRESH_TOKEN);
    }
}
