package com.example.backend.projection;

import java.util.UUID;

public interface ClientsForMapProjection {
    UUID getId();
    Double getLongitude();
    Double getLatitude();

    String getName();

    String getAddress();
    boolean getActive();
}
