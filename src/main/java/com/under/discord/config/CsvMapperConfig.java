package com.under.discord.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.under.discord.session.domain.SessionRecordStatistic;
import com.under.discord.session.domain.SessionRecordStatisticsCSV;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CsvMapperConfig {
    
    @Bean
    public CsvMapper csvMapper() {
        CsvMapper csvMapper = new CsvMapper();
        
        csvMapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        csvMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        csvMapper.registerModule(new JavaTimeModule());
        csvMapper.addMixIn(SessionRecordStatistic.class, SessionRecordStatisticsCSV.class);
        
        return csvMapper;
    }
}
