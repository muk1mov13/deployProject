package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String region;

    @OneToOne
    private User supervisor;  //supervisor name

    @Column(nullable = false)
    private String company_name;

    @Column(nullable = false)
    private String support_phone;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private Timestamp registration_date;


}
