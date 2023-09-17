package im.greenmate.api.domain.user.exception;


import im.greenmate.api.global.error.ErrorCode;
import im.greenmate.api.global.error.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
