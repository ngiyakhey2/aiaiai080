package com.thehecklers.aiaiai00800;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class Ai080Config {
    @Bean
    @Description("What is the weather in location")
    public Function<Request, Response> weatherFunction() {
        return new WeatherService();
    }
}
