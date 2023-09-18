package im.greenmate.api.domain.user.exception;

import im.greenmate.api.global.error.ErrorCode;
import im.greenmate.api.global.error.exception.NotFoundException;

public class RefreshTokenNotFoundException extends NotFoundException {
    public RefreshTokenNotFoundException() {
        super(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
    }
}
