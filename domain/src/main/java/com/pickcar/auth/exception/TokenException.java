package com.pickcar.auth.exception;

import com.pickcar.exception.GlobalException;

public class TokenException extends GlobalException {
  public TokenException(TokenErrorCode errorCode) {
    super(errorCode);
  }
}
