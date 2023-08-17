package com.example.backend.projection;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

public interface DashboardViewProjection {

    @Value("#{target.support_phone}")
    String getSupportPhone();

    @Value("#{T(java.time.LocalDate).ofInstant(target.current_date, T(java.time.ZoneId).systemDefault())}")
    LocalDate getCurrentDate();

}