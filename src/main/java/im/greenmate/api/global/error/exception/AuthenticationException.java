package im.greenmate.api.global.error.exception;


import im.greenmate.api.global.error.ErrorCode;

public class AuthenticationException extends BusinessException {

    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthenticationException() {
        super(ErrorCode.UNAUTHORIZED);
    }
}
