package com.under.discord.session.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class UserTimeTrack {
    private VoiceChannelEvent event;
    private LocalDateTime dateTime;
}
