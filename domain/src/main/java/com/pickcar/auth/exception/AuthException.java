package com.pickcar.auth.exception;

import com.pickcar.exception.GlobalException;

public class AuthException extends GlobalException {
  public AuthException(AuthErrorCode errorCode) {
    super(errorCode);
  }
}
