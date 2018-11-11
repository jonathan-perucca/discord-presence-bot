package com.under.discord.session.web;

import com.under.discord.session.SessionComponent;
import com.under.discord.session.domain.SessionRecordStatistic;
import com.under.discord.session.entity.SessionRecord;
import com.under.discord.session.mapper.SessionMapper;
import com.under.discord.session.mapper.SessionRecordDTOs;
import com.under.discord.session.tool.CsvStat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Comparator.comparing;

@Slf4j
@RestController
@RequestMapping("/reports")
public class ReportController {

    private final SessionComponent sessionComponent;
    private final SessionMapper sessionMapper;
    private final CsvStat csvStat;

    @Autowired
    public ReportController(SessionComponent sessionComponent,
                            SessionMapper sessionMapper,
                            CsvStat csvStat) {
        this.sessionComponent = sessionComponent;
        this.sessionMapper = sessionMapper;
        this.csvStat = csvStat;
    }

    @GetMapping("/export")
    public void getCsvStatsAsFile(HttpServletResponse response) {
        try {
            List<SessionRecordStatistic> allRecordStats = sessionComponent.getSessionRecordStatsFrom(LocalDate.of(1970, 1, 1));
            String statsText = csvStat.toCsvText(allRecordStats);
            InputStream inputStream = IOUtils.toInputStream(statsText, "ISO_8859_1");

            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=\"export.csv\"");
            IOUtils.copy(inputStream, response.getOutputStream());
        } catch (IOException ex) {
            log.error("Error during stream convertion/writing", ex);
        }
    }

    @GetMapping
    public List<SessionRecordDTOs> getAllRecords() {
        List<SessionRecord> sessionRecords = sessionComponent.getAllSessionRecords();

        Map<LocalDate, List<SessionRecord>> map = sortByReversedTimeSpent(toMap(sessionRecords));

        List<SessionRecordDTOs> results = new ArrayList<>();
        map.forEach((sessionDate, records) -> {
            SessionRecordDTOs sessionRecordDTOs = sessionMapper.toSessionRecordDTOs(records);
            results.add(sessionRecordDTOs);
        });

        return results;
    }

    private Map<LocalDate, List<SessionRecord>> toMap(List<SessionRecord> sessionRecords) {
        Map<LocalDate, List<SessionRecord>> map = new HashMap<>();
        for (SessionRecord sessionRecord : sessionRecords) {
            LocalDate startDate = sessionRecord.getStartDate();

            if(!map.containsKey(startDate)) {
                List<SessionRecord> newList = new ArrayList<>();
                newList.add(sessionRecord);
                map.put(startDate, newList);
                continue;
            }

            List<SessionRecord> recordsForDate = map.get(startDate);
            recordsForDate.add(sessionRecord);
        }
        return map;
    }

    private Map<LocalDate, List<SessionRecord>> sortByReversedTimeSpent(Map<LocalDate, List<SessionRecord>> map) {
        map.keySet().forEach(localDate ->
                map.get(localDate).sort(comparing(SessionRecord::getTimeSpentInSeconds).reversed())
        );

        return map;
    }
}
