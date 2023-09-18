package im.greenmate.api.global.error.exception;


import im.greenmate.api.global.error.ErrorCode;

public class IllegalException extends BusinessException {
    public IllegalException() {
        super(ErrorCode.BAD_REQUEST);
    }

    public IllegalException(ErrorCode e) {
        super(e);
    }
}
