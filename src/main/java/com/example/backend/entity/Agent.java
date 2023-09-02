package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "agents")
@DiscriminatorValue("AGENT")
public class Agent extends User {

    private Long chatId;

    @Column(nullable = false)
    private Timestamp created_at;

}
