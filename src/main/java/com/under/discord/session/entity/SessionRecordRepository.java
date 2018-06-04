package com.under.discord.session.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SessionRecordRepository extends JpaRepository<SessionRecord, Long> {

    @Query("select sr from SessionRecord sr where startDate >= :start_date")
    List<SessionRecord> findByStartDateEqualOrAfter(@Param("start_date") LocalDate fromDate);
}