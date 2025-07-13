package com.pickcar.drivehistory.application.validator;

import com.pickcar.drivehistory.exception.DriveHistoryErrorCode;
import com.pickcar.drivehistory.exception.DriveHistoryException;
import com.pickcar.drivehistory.presentation.dto.request.DriveHistoryPayload;
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

    public void validatePayload(DriveHistoryPayload payload) {
        if (payload.getEngineOffTime().isBefore(payload.getEngineOnTime())) {
            throw new DriveHistoryException(DriveHistoryErrorCode.END_TIME_BEFORE_START_TIME);
        }
        if (payload.getVehicleId() == null || payload.getUserId() == null) {
            throw new DriveHistoryException(DriveHistoryErrorCode.REQUIRED_FIELDS_MISSING);
        }
    }
}
