package com.example.backend.projection;

import java.util.UUID;

public interface TerritoryForMapProjection {
    UUID getId();
    String getName();
    Double getLongitude();
    Double getLatitude();
}
