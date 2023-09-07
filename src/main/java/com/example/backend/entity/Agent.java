package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "agents") 
public class Agent extends User {

    private Long chatId;

    @Column(nullable = false)
    private Timestamp created_at;

}
