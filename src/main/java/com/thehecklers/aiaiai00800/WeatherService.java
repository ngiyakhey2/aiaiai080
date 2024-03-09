package com.thehecklers.aiaiai00800;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClient;

import java.util.function.Function;

public class WeatherService implements Function<Request, Response> {
    @Value("${weather.key:No valid key}")
    private String key;

    @Value("${weather.url:No valid URL}")
    private String url;

    @Value("${weather.host:No valid host}")
    private String host;

    private final RestClient client = RestClient.create();

    /*{
        "cloud_pct": 75,
            "temp": 12,
            "feels_like": 10,
            "humidity": 54,
            "min_temp": 9,
            "max_temp": 14,
            "wind_speed": 2.57,
            "wind_degrees": 340,
            "sunrise": 1709908516,
            "sunset": 1709949892
    }*/

    public Response apply(Request request) {
        return client.get()
                .uri(url + "?city=" + request.location())
                .header("X-RapidAPI-Key", key)
                .header("X-RapidAPI-Host", host)
                .retrieve()
                .body(Response.class);
    }
}