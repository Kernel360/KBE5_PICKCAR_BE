package com.pickcar.auth.exception;

import com.pickcar.exception.GlobalException;

public class UserException extends GlobalException {
  public UserException(UserErrorCode errorCode) {
    super(errorCode);
  }
}
