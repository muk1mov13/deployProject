package com.example.backend.projection;

import com.example.backend.constants.Day;
import com.example.backend.entity.CustomerCategory;
import com.example.backend.entity.Territory;
import jakarta.persistence.OneToOne;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ClientProjection {
    UUID getId();
    String getName();
    String getAddress();
    String getPhone();
    String getTin();
    boolean getActive();
    String getCompany_name();
    String  getCategory();
    UUID getCategory_id();
    String getTerritory();
    UUID getTerritory_id();
    Double getLongitude();
    Double getLatitude();
    List<String> getVisiting_days();
    LocalDate getRegistration_date();
    String getReference_point();
}
