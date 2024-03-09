package com.thehecklers.aiaiai00800;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Response(int cloud_pct,
                       double temp,
                       double feels_like,
                       int humidity,
                       double min_temp,
                       double max_temp,
                       double wind_speed,
                       int wind_degrees,
                       long sunrise,
                       long sunset) {
}
