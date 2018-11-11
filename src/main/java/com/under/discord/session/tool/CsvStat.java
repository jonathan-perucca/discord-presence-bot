package com.under.discord.session.tool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.under.discord.session.domain.SessionRecordStatistic;
import com.under.discord.session.domain.SessionRecordStatisticsCSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CsvStat {

    private final CsvMapper csvMapper;

    @Autowired
    public CsvStat(CsvMapper csvMapper) {
        this.csvMapper = csvMapper;
    }

    public String toCsvText(List<SessionRecordStatistic> sessionRecordStats) throws JsonProcessingException {
        CsvSchema csvSchema = csvMapper.schemaFor(SessionRecordStatisticsCSV.class).withHeader();

        try {
            return csvMapper.writer(csvSchema).writeValueAsString(sessionRecordStats);
        } catch (JsonProcessingException e) {
            throw e;
        }
    }
}
