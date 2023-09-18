package im.greenmate.api.domain.user.exception;

import im.greenmate.api.global.error.ErrorCode;
import im.greenmate.api.global.error.exception.IllegalException;

public class DuplicationNicknameException extends IllegalException {
    public DuplicationNicknameException() {
        super(ErrorCode.DUPLICATE_NICKNAME);
    }
}
