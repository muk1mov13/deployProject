package com.example.backend.payload.request;

import com.example.backend.entity.CustomerCategory;
import com.example.backend.entity.Territory;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.service.UnknownUnwrapTypeException;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReqClientSave {
    private UUID id;
    private String name;
    private String city;
    private String address;
    private String telephone;
    private String tin;
    private LocalDate registrationDate;
    private boolean active;
    private String referencePoint;
    private String companyName;
    private UUID categoryId;
    private UUID territoryId;
    private Double longitude;
    private Double latitude;
    private String[] visitingDays;


}
