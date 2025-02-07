package com.weatherapp.myweatherapp.controller;

import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WeatherController {

  @Autowired
  WeatherService weatherService;

  @GetMapping("/forecast/{city}")
  public ResponseEntity<CityInfo> forecastByCity(@PathVariable("city") String city) {

    CityInfo ci = weatherService.forecastByCity(city);

    return ResponseEntity.ok(ci);
  }

  //DONE: Given two city names, compare the length of the daylight hours and return the city with the longest day
  @GetMapping("/compare/daylight/{city1}/{city2}")
  public ResponseEntity<String> compareDaylight(@PathVariable("city1") String city1,
      @PathVariable("city2") String city2) {
    try {
      String response = weatherService.compareDaylight(city1, city2);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error comparing the daylight with the given cities." + e.getMessage());
    }
  }

  //DONE: Given two city names, check which city its currently raining in
  @GetMapping("/check/rain/{city1}/{city2}")
  public ResponseEntity<String> checkRain(@PathVariable("city1") String city1, @PathVariable("city2") String city2) {
    try {
      String response = weatherService.checkRain(city1, city2);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error while checking the rain conditions." + e.getMessage());
    }
  }

}
