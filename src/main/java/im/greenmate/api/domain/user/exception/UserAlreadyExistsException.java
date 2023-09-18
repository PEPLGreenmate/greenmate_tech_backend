package im.greenmate.api.domain.user.exception;

import im.greenmate.api.global.error.ErrorCode;
import im.greenmate.api.global.error.exception.IllegalException;

public class UserAlreadyExistsException extends IllegalException {
    public UserAlreadyExistsException() {
        super(ErrorCode.USER_ALREADY_EXISTS);
    }
}
