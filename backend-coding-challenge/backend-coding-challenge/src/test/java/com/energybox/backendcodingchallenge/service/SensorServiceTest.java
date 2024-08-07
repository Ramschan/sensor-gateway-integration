package com.energybox.backendcodingchallenge.service;

import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.domain.LastReading;
import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.domain.SensorType;
import com.energybox.backendcodingchallenge.exception.SensorNotFoundException;
import com.energybox.backendcodingchallenge.model.SensorRequest;
import com.energybox.backendcodingchallenge.model.SensorResponse;
import com.energybox.backendcodingchallenge.repository.GatewayRepository;
import com.energybox.backendcodingchallenge.repository.SensorRepository;
import com.energybox.backendcodingchallenge.repository.SensorTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SensorServiceTest {

    @Mock
    private SensorRepository sensorRepository;

    @Mock
    private GatewayRepository gatewayRepository;

    @Mock
    private SensorTypeRepository sensorTypeRepository;

    @InjectMocks
    private SensorService sensorService;

    private Sensor sensor;
    private Gateway gateway;
    private SensorType sensorType;

    @BeforeEach
    void setUp() {
        sensor = new Sensor("Temp Sensor", "Location1");
        sensor.setId(1L);

        gateway = new Gateway("Main Gateway");
        gateway.setId(1L);

        sensorType = new SensorType("electricity");
        sensorType.setName("electricity");
    }

    @Test
    void createSensors_ShouldReturnSensorResponse() {
        SensorRequest sensorRequest = new SensorRequest();
        sensorRequest.setName("Temp Sensor");
        sensorRequest.setLocationCode("Location1");
        sensorRequest.setType( Set.of("electricity"));

        when(sensorRepository.save(any(Sensor.class))).thenReturn(sensor);
        when(sensorTypeRepository.findByName("electricity")).thenReturn(sensorType);

        SensorResponse response = sensorService.createSensors(sensorRequest);

        assertNotNull(response);
        assertEquals(sensor.getId(), response.getSensorId());
        verify(sensorRepository).save(any(Sensor.class));
        verify(sensorTypeRepository).findByName("electricity");
    }

    @Test
    void fetchAllSensors_ShouldReturnListOfSensors() {
        List<Sensor> sensors = Collections.singletonList(sensor);
        when(sensorRepository.findAll()).thenReturn(sensors);

        List<Sensor> result = sensorService.fetchAllSensors();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sensor, result.get(0));
    }

    @Test
    void fetchSensorById_ShouldReturnSensor() {
        when(sensorRepository.findById(1L)).thenReturn(Optional.of(sensor));

        Sensor result = sensorService.fetchSensorById(1L);

        assertNotNull(result);
        assertEquals(sensor, result);
    }

    @Test
    void fetchSensorById_ShouldThrowExceptionIfNotFound() {
        when(sensorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SensorNotFoundException.class, () -> sensorService.fetchSensorById(1L));
    }

    @Test
    void assignSensorToGateway_ShouldAssignSuccessfully() {
        when(sensorRepository.findById(1L)).thenReturn(Optional.of(sensor));
        when(gatewayRepository.findById(1L)).thenReturn(Optional.of(gateway));

        sensorService.assignSensorToGateway(1L, 1L);

        assertEquals(gateway, sensor.getGateway());
        verify(sensorRepository).save(sensor);
    }

    @Test
    void getSensorsByGateway_ShouldReturnListOfSensors() {
        List<Sensor> sensors = Collections.singletonList(sensor);
        when(sensorRepository.findAllByGatewayId(1L)).thenReturn(sensors);

        List<Sensor> result = sensorService.getSensorsByGateway(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sensor, result.get(0));
    }

    @Test
    void getLastReadings_ShouldReturnListOfReadings() {
        LastReading lastReading = new LastReading(LocalDateTime.now(), 23.5);
        sensor.addReading(sensorType, lastReading);

        when(sensorRepository.findById(1L)).thenReturn(Optional.of(sensor));

        List<LastReading> result = sensorService.getLastReadings(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(lastReading, result.get(0));
    }

    @Test
    void getLastReadings_ShouldThrowExceptionIfSensorNotFound() {
        when(sensorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SensorNotFoundException.class, () -> sensorService.getLastReadings(1L));
    }

}
