package com.energybox.backendcodingchallenge.service;

import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.exception.GatewayNotFoundException;
import com.energybox.backendcodingchallenge.model.GatewayRequest;
import com.energybox.backendcodingchallenge.model.GatewayResponse;
import com.energybox.backendcodingchallenge.repository.GatewayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GatewayServiceTest {

    @Mock
    private GatewayRepository gatewayRepository;

    @InjectMocks
    private GatewayService gatewayService;

    private Gateway gateway;

    @BeforeEach
    void setUp() {
        gateway = new Gateway("Main Gateway");
        gateway.setId(1L);
    }

    @Test
    void createGateway_ShouldReturnGatewayResponse() {
        GatewayRequest gatewayRequest = new GatewayRequest();
        gatewayRequest.setName("Main Gateway");

        when(gatewayRepository.save(any(Gateway.class))).thenReturn(gateway);

        GatewayResponse response = gatewayService.createGateway(gatewayRequest);

        assertNotNull(response);
        assertEquals(gateway.getId(), response.getGateWayId());
        verify(gatewayRepository).save(any(Gateway.class));
    }

    @Test
    void fetchAllGateways_ShouldReturnListOfGateways() {
        List<Gateway> gateways = Collections.singletonList(gateway);
        when(gatewayRepository.findAll()).thenReturn(gateways);

        List<Gateway> result = gatewayService.fetchAllGateways();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(gateway, result.get(0));
    }

    @Test
    void fetchGatewayById_ShouldReturnGateway() {
        when(gatewayRepository.findById(1L)).thenReturn(Optional.of(gateway));

        Gateway result = gatewayService.fetchGatewayById(1L);

        assertNotNull(result);
        assertEquals(gateway, result);
    }

    @Test
    void fetchGatewayById_ShouldThrowExceptionIfNotFound() {
        when(gatewayRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(GatewayNotFoundException.class, () -> gatewayService.fetchGatewayById(1L));
    }

    @Test
    void fetchGatewaysBySensorType_ShouldReturnListOfGateways() {
        List<Gateway> gateways = Collections.singletonList(gateway);
        when(gatewayRepository.findGatewaysWithSensorType("electricity")).thenReturn(gateways);

        List<Gateway> result = gatewayService.fetchGatewaysBySensorType("electricity");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(gateway, result.get(0));
    }
}
