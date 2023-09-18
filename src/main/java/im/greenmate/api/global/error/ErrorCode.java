package im.greenmate.api.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // Common
    ENTITY_NOT_FOUND(HttpStatus.BAD_REQUEST, "C001", "엔티티 조회에 실패하였습니다."),
    INVALID_VALUE(HttpStatus.BAD_REQUEST, "C002", "잘못된 입력값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C003", "잘못된 HTTP 메서드입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "C004", "권한이 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C005", "서버 내부에서 에러가 발생하였습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "C006", "Not Found"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "C007", "Bad Request"),

    // Authentication
    INVALID_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "A001", "잘못된 로그인 정보입니다."),
    EMPTY_AUTHORIZATION(HttpStatus.UNAUTHORIZED, "A002", "Authorization Header가 빈 값입니다."),
    NOT_BEARER_GRANT_TYPE(HttpStatus.UNAUTHORIZED, "A003", "인증 타입이 Bearer 타입이 아닙니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A004", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "A005", "만료된 토큰입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A006", "Unauthorized"),
    INVALID_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "A007", "잘못된 토큰 타입입니다."),

    // User
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "U001", "이미 등록된 닉네임입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U002", "유저를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "U003", "이미 등록된 유저입니다."),

    // Refresh Token
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "R004", "refresh 토큰을 찾을 수 없습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "R001", "유효하지 않은 refresh 토큰입니다."),

    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

}
