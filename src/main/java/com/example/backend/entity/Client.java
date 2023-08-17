package com.example.backend.entity;

import com.example.backend.constants.Day;
import com.example.backend.converter.DayConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Client {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String phone;
    private String tin;
    @Column(nullable = false)
    private boolean active;
    private String company_name;

    @ManyToOne
    private CustomerCategory category;

    @ManyToOne
    private Territory territory;
    @Column(nullable = false)
    private Double longitude;
    @Column(nullable = false)
    private Double latitude;
    private String visiting_days;
    private String reference_point;
    private Timestamp registration_date;

    public Client(String name, String address, String phone, String tin, boolean active, String companyName, CustomerCategory category, Territory territory, Double longitude, Double latitude, String visiting_days, String referencePoint, Timestamp registration_date) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.tin = tin;
        this.active = active;
        this.company_name = companyName;
        this.category = category;
        this.territory = territory;
        this.longitude = longitude;
        this.latitude = latitude;
        this.visiting_days = visiting_days;
        this.reference_point = referencePoint;
        this.registration_date = registration_date;
    }
}
