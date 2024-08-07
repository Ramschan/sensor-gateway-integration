package com.energybox.backendcodingchallenge.controller;

import com.energybox.backendcodingchallenge.domain.LastReading;
import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.exception.*;
import com.energybox.backendcodingchallenge.model.*;
import com.energybox.backendcodingchallenge.service.SensorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SensorControllerTest {

    @Mock
    private SensorService sensorService;

    @InjectMocks
    private SensorController sensorController;

    private Sensor sensor;
    private LastReading lastReading;

    @BeforeEach
    void setUp() {
        sensor = new Sensor("Temperature Sensor","LOC123");
        sensor.setId(1L);

        SensorResponse sensorResponse = new SensorResponse();
        sensorResponse.setSensorId(1L);

        lastReading = new LastReading(LocalDateTime.now(), 22.5);
        lastReading.setId(1L);
    }

    @Test
    void fetchAllSensors_ShouldReturnListOfSensors() {
        List<Sensor> sensors = Collections.singletonList(sensor);
        when(sensorService.fetchAllSensors()).thenReturn(sensors);

        ResponseEntity<List<Sensor>> response = sensorController.fetchAllSensors();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sensors, response.getBody());
    }

    @Test
    void fetchAllSensors_ShouldReturnNoContentWhenEmpty() {
        when(sensorService.fetchAllSensors()).thenReturn(List.of());

        ResponseEntity<List<Sensor>> response = sensorController.fetchAllSensors();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void fetchSensorBySensorType_ShouldReturnListOfSensors() {
        List<Sensor> sensors = Collections.singletonList(sensor);
        when(sensorService.getSensorsByType("Temperature")).thenReturn(sensors);

        ResponseEntity<List<Sensor>> response = sensorController.fetchSensorBySensorType("Temperature");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sensors, response.getBody());
    }

    @Test
    void fetchSensorBySensorType_ShouldReturnNoContentWhenEmpty() {
        when(sensorService.getSensorsByType("Temperature")).thenReturn(List.of());

        ResponseEntity<List<Sensor>> response = sensorController.fetchSensorBySensorType("Temperature");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void fetchSensoryById_ShouldReturnSensor() throws SensorNotFoundException {
        when(sensorService.fetchSensorById(1L)).thenReturn(sensor);

        ResponseEntity<Sensor> response = sensorController.fetchSensoryById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sensor, response.getBody());
    }

    @Test
    void assignSensorGateway_ShouldReturnBadRequestWhenInvalidRequest() {
        SensorGatewayRequest invalidRequest = new SensorGatewayRequest();

        ResponseEntity<String> response = sensorController.assignSensorGateway(invalidRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getSensorsByGateway_ShouldReturnListOfSensors() {
        List<Sensor> sensors = Collections.singletonList(sensor);
        when(sensorService.getSensorsByGateway(1L)).thenReturn(sensors);

        ResponseEntity<List<Sensor>> response = sensorController.getSensorsByGateway(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sensors, response.getBody());
    }

    @Test
    void getLastReadingsForSensor_ShouldReturnListOfReadings() {
        List<LastReading> readings = Collections.singletonList(lastReading);
        when(sensorService.getLastReadings(1L)).thenReturn(readings);

        ResponseEntity<List<LastReading>> response = sensorController.getLastReadingsForSensor(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(readings, response.getBody());
    }

    @Test
    void getLastReadingsForSensor_ShouldReturnNotFoundWhenSensorDoesNotExist() {
        when(sensorService.getLastReadings(1L)).thenThrow(new SensorNotFoundException("Sensor not found"));

        ResponseEntity<List<LastReading>> response = sensorController.getLastReadingsForSensor(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
