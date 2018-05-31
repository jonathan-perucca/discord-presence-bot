package com.under.discord.session.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "user", "Nombre de session effectuée" })
public abstract class SessionRecordStatisticsCSV {
    
    @JsonProperty(value = "User", required = true)
    abstract String getUser();
    
    @JsonProperty(value = "Nombre de session effectuée", required = true)
    abstract String getPresenceTimes();
    
}