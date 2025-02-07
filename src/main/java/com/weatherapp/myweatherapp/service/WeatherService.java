package com.weatherapp.myweatherapp.service;

import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.repository.VisualcrossingRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

  @Autowired
  VisualcrossingRepository weatherRepo;

  public CityInfo forecastByCity(String city) {

    return weatherRepo.getByCity(city);
  }

  public String compareDaylight(String city1, String city2) {
    try {
      CityInfo cityInfo1 = forecastByCity(city1);
      CityInfo cityInfo2 = forecastByCity(city2);

      long daylightHoursCity1 = calculateDaylightHours(
          cityInfo1.getCurrentConditions().getSunrise(),
          cityInfo1.getCurrentConditions().getSunset());

      long daylightHoursCity2 = calculateDaylightHours(
          cityInfo2.getCurrentConditions().getSunrise(),
          cityInfo2.getCurrentConditions().getSunset());

      return daylightComparisonResponseBuilder(city1, city2, daylightHoursCity1, daylightHoursCity2);
    } catch (Exception e) {
      return "Error while comparing daylight hours.";
    }
  }

  public String checkRain(String city1, String city2) {
    try {
      CityInfo cityInfo1 = forecastByCity(city1);
      CityInfo cityInfo2 = forecastByCity(city2);

      boolean isRainingCity1 = isRaining(cityInfo1);
      boolean isRainingCity2 = isRaining(cityInfo2);

      return rainCheckResponseBuilder(city1, city2, isRainingCity1, isRainingCity2);
    } catch (Exception e) {
      return "Error while checking rain conditions.";
    }
  }

  // Helper method for calculating the number of daylight hours
  private long calculateDaylightHours(String sunrise, String sunset) {
    try {
      SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
      Date sunriseTime = formatter.parse(sunrise);
      Date sunsetTime = formatter.parse(sunset);
      long daylightHours = (sunsetTime.getTime() - sunriseTime.getTime()) / (60 * 60 * 1000);
      return daylightHours;
    } catch (ParseException e) {
      return 0;
    }
  }

  // Helper method to check if it iss currently raining given a city
  private boolean isRaining(CityInfo cityInfo) {
    String conditions = cityInfo.getCurrentConditions().getConditions();
    return conditions != null && conditions.toLowerCase().contains("rain");
  }

  private String daylightComparisonResponseBuilder(String city1, String city2, long daylightHoursCity1,
      long daylightHoursCity2) {
    if (daylightHoursCity1 > daylightHoursCity2) {
      return String.format("The longest day is in %s with %d hours of daylight.", city1, daylightHoursCity1);
    } else if (daylightHoursCity2 > daylightHoursCity1) {
      return String.format("The longest day is in %s with %d hours of daylight.", city2, daylightHoursCity2);
    } else {
      return "Both cities have the same length of daylight hours.";
    }
  }

  private String rainCheckResponseBuilder(String city1, String city2, boolean isRainingCity1, boolean isRainingCity2) {
    if (isRainingCity1 && isRainingCity2) {
      return String.format("It is currently raining in both %s and %s.", city1, city2);
    } else if (isRainingCity1) {
      return String.format("It is currently raining in %s.", city1);
    } else if (isRainingCity2) {
      return String.format("It is currently raining in %s.", city2);
    } else {
      return String.format("It is not currently raining in either %s or %s.", city1, city2);
    }
  }

}
