package im.greenmate.api.global.error.exception;


import im.greenmate.api.global.error.ErrorCode;

public class NotFoundException extends BusinessException {
    public NotFoundException() {
        super(ErrorCode.NOT_FOUND);
    }

    public NotFoundException(ErrorCode e) {
        super(e);
    }
}