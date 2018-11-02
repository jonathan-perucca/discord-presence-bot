package com.under.discord.session.mapper;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SessionRecordDTOs {
    private Integer numberOfUsers;
    private String sessionDate;
    private List<SessionRecordDTO> records;
}
