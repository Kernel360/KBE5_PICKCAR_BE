package com.pickcar.drivehistory.application.command;

import com.pickcar.emulator.domain.EventInfo;

public interface WriteDriveHistoryCommand {
    void execute(Long vehicleId, EventInfo offEventInfo);
}
