package com.pickcar.drivehistory.exception;

import com.pickcar.exception.GlobalException;

public class DriveHistoryException extends GlobalException {
    
    public DriveHistoryException(DriveHistoryErrorCode errorCode) {
        super(errorCode);   //현재는 모든 예외에 대한 errorCode를 한 클래스에서 사용하지만, 나중에 양이 많아지면 이를 세분화 할 수 있음
    }
}
