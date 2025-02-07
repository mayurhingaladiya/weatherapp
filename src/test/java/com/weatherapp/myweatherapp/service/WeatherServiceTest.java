package com.weatherapp.myweatherapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.repository.VisualcrossingRepository;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

  @Mock
  private VisualcrossingRepository weatherRepo;

  @InjectMocks
  private WeatherService weatherService;

  private CityInfo cityInfo1;
  private CityInfo cityInfo2;

  @BeforeEach
  void setUp() {
    // Initialise CityInfo objects for testing
    cityInfo1 = new CityInfo();
    CityInfo.CurrentConditions conditions1 = new CityInfo.CurrentConditions();
    conditions1.setSunrise("06:00:00");
    conditions1.setSunset("18:00:00");
    conditions1.setConditions("Rain");
    cityInfo1.setCurrentConditions(conditions1);

    cityInfo2 = new CityInfo();
    CityInfo.CurrentConditions conditions2 = new CityInfo.CurrentConditions();
    conditions2.setSunrise("06:00:00"); 
    conditions2.setSunset("19:00:00");
    conditions2.setConditions("Clear");
    cityInfo2.setCurrentConditions(conditions2);
  }

  @Test
  void compareDaylight_ShouldReturnCityWithLongestDay() {
    // Arrange
    when(weatherRepo.getByCity("City1")).thenReturn(cityInfo1);
    when(weatherRepo.getByCity("City2")).thenReturn(cityInfo2);

    // Act
    String result = weatherService.compareDaylight("City1", "City2");

    // Assert
    assertEquals("The longest day is in City2 with 13 hours of daylight.", result);
    verify(weatherRepo, times(1)).getByCity("City1");
    verify(weatherRepo, times(1)).getByCity("City2");
  }

  @Test
  void compareDaylight_ShouldReturnSameDaylightHours() {
    // Arrange
    cityInfo1.getCurrentConditions().setSunrise("06:00:00");
    cityInfo1.getCurrentConditions().setSunset("18:00:00");
    cityInfo2.getCurrentConditions().setSunrise("06:00:00");
    cityInfo2.getCurrentConditions().setSunset("18:00:00");

    when(weatherRepo.getByCity("City1")).thenReturn(cityInfo1);
    when(weatherRepo.getByCity("City2")).thenReturn(cityInfo2);

    // Act
    String result = weatherService.compareDaylight("City1", "City2");

    // Assert
    assertEquals("Both cities have the same length of daylight hours.", result);
    verify(weatherRepo, times(1)).getByCity("City1");
    verify(weatherRepo, times(1)).getByCity("City2");
  }

  @Test
  void checkRain_ShouldReturnRainingInCity1() {
    // Arrange
    when(weatherRepo.getByCity("City1")).thenReturn(cityInfo1);
    when(weatherRepo.getByCity("City2")).thenReturn(cityInfo2);

    // Act
    String result = weatherService.checkRain("City1", "City2");

    // Assert
    assertEquals("It is currently raining in City1.", result);
    verify(weatherRepo, times(1)).getByCity("City1");
    verify(weatherRepo, times(1)).getByCity("City2");
  }

  @Test
  void checkRain_ShouldReturnRainingInCity2() {
    // Arrange
    cityInfo1.getCurrentConditions().setConditions("Clear");
    cityInfo2.getCurrentConditions().setConditions("Rain");

    when(weatherRepo.getByCity("City1")).thenReturn(cityInfo1);
    when(weatherRepo.getByCity("City2")).thenReturn(cityInfo2);

    // Act
    String result = weatherService.checkRain("City1", "City2");

    // Assert
    assertEquals("It is currently raining in City2.", result);
    verify(weatherRepo, times(1)).getByCity("City1");
    verify(weatherRepo, times(1)).getByCity("City2");
  }

  @Test
  void checkRain_ShouldReturnRainingInBothCities() {
    // Arrange
    cityInfo1.getCurrentConditions().setConditions("Rain");
    cityInfo2.getCurrentConditions().setConditions("Rain");

    when(weatherRepo.getByCity("City1")).thenReturn(cityInfo1);
    when(weatherRepo.getByCity("City2")).thenReturn(cityInfo2);

    // Act
    String result = weatherService.checkRain("City1", "City2");

    // Assert
    assertEquals("It is currently raining in both City1 and City2.", result);
    verify(weatherRepo, times(1)).getByCity("City1");
    verify(weatherRepo, times(1)).getByCity("City2");
  }

  @Test
  void checkRain_ShouldReturnNotRainingInEitherCity() {
    // Arrange
    cityInfo1.getCurrentConditions().setConditions("Clear");
    cityInfo2.getCurrentConditions().setConditions("Clear");

    when(weatherRepo.getByCity("City1")).thenReturn(cityInfo1);
    when(weatherRepo.getByCity("City2")).thenReturn(cityInfo2);

    // Act
    String result = weatherService.checkRain("City1", "City2");

    // Assert
    assertEquals("It is not currently raining in either City1 or City2.", result);
    verify(weatherRepo, times(1)).getByCity("City1");
    verify(weatherRepo, times(1)).getByCity("City2");
  }
}