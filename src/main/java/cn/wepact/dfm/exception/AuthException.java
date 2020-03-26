package cn.wepact.dfm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author wangbin
 */
@ResponseStatus(HttpStatus.PERMANENT_REDIRECT)
public class AuthException extends RuntimeException {
}
