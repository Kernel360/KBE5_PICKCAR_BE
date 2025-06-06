package com.pickcar.application.command;

import com.pickcar.application.CycleService;
import com.pickcar.drivehistory.application.command.dto.WriteDriveHistoryCommandDto;
import com.pickcar.emulator.domain.Cycle;
import com.pickcar.emulator.domain.EventInfo;
import com.pickcar.drivehistory.application.DriveHistoryService;
import com.pickcar.drivehistory.application.command.WriteDriveHistoryCommand;
import com.pickcar.reservation.application.ReservationService;
import com.pickcar.reservation.domain.Reservation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WriteDriveHistoryCommandImpl implements WriteDriveHistoryCommand {

    private final CycleService cycleService;
    private final ReservationService reservationService;
    private final DriveHistoryService driveHistoryService;

    @Override
    public void execute(Long vehicleId, EventInfo offEventInfo) {
        Reservation reservation = reservationService.getActiveReservationByVehicleId(vehicleId);
        List<Cycle> cycles = cycleService.getAllByVehicleIdAndOccurredAtBetween(vehicleId,
                offEventInfo.getEngineOnTime(), offEventInfo.getEngineOffTime());

        driveHistoryService.write(new WriteDriveHistoryCommandDto(reservation.getId(), offEventInfo.getEngineOnTime(),  offEventInfo.getEngineOffTime(), cycles));
    }
}
