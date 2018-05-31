package com.under.discord.session.domain;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionRecordStatistic {
    
    private String user;
    
    private String presenceTimes;
}
