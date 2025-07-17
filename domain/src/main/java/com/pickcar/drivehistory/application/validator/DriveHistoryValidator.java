package com.pickcar.drivehistory.application.validator;

import com.pickcar.drivehistory.exception.DriveHistoryErrorCode;
import com.pickcar.drivehistory.exception.DriveHistoryException;
import com.pickcar.dto.command.DriveHistoryWriteCommand;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DriveHistoryValidator {

    @Value("${custom.driveHistory.maximum-inquiry-days}")
    private Integer maximumInquiryDays;

    public void checkFilterRequestDate(LocalDateTime from, LocalDateTime to) {
        LocalDate today = LocalDate.now();
        LocalDateTime inquiryLimitDate = today.atStartOfDay().minusDays(maximumInquiryDays);

        if (from.isAfter(to)) {
            throw new DriveHistoryException(DriveHistoryErrorCode.FROM_DATE_CANT_BE_BEFORE_TO_DATE);
        }

        if (from.isBefore(inquiryLimitDate)) {
            throw new DriveHistoryException(DriveHistoryErrorCode.MAXIMUM_INQUIRY_LIMIT_EXCEEDED);
        }
    }

    public void validatePayload(DriveHistoryWriteCommand command) {
        if (command.engineOffTime().isBefore(command.engineOnTime())) {
            throw new DriveHistoryException(DriveHistoryErrorCode.END_TIME_BEFORE_START_TIME);
        }
        if (command.vehicleId() == null || command.userId() == null) {
            throw new DriveHistoryException(DriveHistoryErrorCode.REQUIRED_FIELDS_MISSING);
        }
    }
}
