package com.pickcar.dto.command;

public interface DomainCommand {
    String getCommandType();
    String getTraceId();
}
