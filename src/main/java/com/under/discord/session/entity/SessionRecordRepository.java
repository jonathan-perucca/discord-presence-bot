package com.under.discord.session.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SessionRecordRepository extends JpaRepository<SessionRecord, Long> {
    
    List<SessionRecord> findByStartDateAfter(LocalDate fromDate);
}