package com.under.discord.session.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(
        name = "session_record", 
        uniqueConstraints = @UniqueConstraint(name = "unique_date_record", columnNames = {"start_date", "user"})  
)
public class SessionRecord {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "user")
    private String user;
}