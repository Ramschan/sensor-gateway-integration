package com.energybox.backendcodingchallenge.controller;

import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.exception.GatewayNotFoundException;
import com.energybox.backendcodingchallenge.exception.InvalidRequestException;
import com.energybox.backendcodingchallenge.model.GatewayRequest;
import com.energybox.backendcodingchallenge.model.GatewayResponse;
import com.energybox.backendcodingchallenge.service.GatewayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GatewayControllerTest {

    @Mock
    private GatewayService gatewayService;

    @InjectMocks
    private GatewayController gatewayController;

    private Gateway gateway;
    private GatewayRequest gatewayRequest;
    private GatewayResponse gatewayResponse;

    @BeforeEach
    void setUp() {
        gateway = new Gateway("Main Gateway");
        gateway.setId(1L);
        gatewayRequest = new GatewayRequest();
        gatewayRequest.setName("Main Gateway");
        gatewayResponse = new GatewayResponse();
        gatewayResponse.setGateWayId(1L);
    }

    @Test
    void createGateway_ShouldReturnCreatedGatewayResponse() throws InvalidRequestException {
        when(gatewayService.createGateway(any(GatewayRequest.class))).thenReturn(gatewayResponse);

        ResponseEntity<GatewayResponse> response = gatewayController.createGateway(gatewayRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(gatewayResponse.getGateWayId(), Objects.requireNonNull(response.getBody()).getGateWayId());
    }

    @Test
    void createGateway_ShouldReturnBadRequestWhenNameIsNull() throws InvalidRequestException {
        GatewayRequest invalidRequest = new GatewayRequest();

        ResponseEntity<GatewayResponse> response = gatewayController.createGateway(invalidRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getSensorByGatewayId_ShouldReturnGateway() throws GatewayNotFoundException, InvalidRequestException {
        when(gatewayService.fetchGatewayById(1L)).thenReturn(gateway);

        ResponseEntity<Object> response = gatewayController.getSensorByGatewayId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(gateway, response.getBody());
    }

    @Test
    void getSensorByGatewayId_ShouldReturnNotFoundWhenGatewayDoesNotExist() throws GatewayNotFoundException {
        when(gatewayService.fetchGatewayById(1L)).thenThrow(new GatewayNotFoundException("Gateway not found"));

        ResponseEntity<Object> response = gatewayController.getSensorByGatewayId(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void fetchGatewaysBySensorType_ShouldReturnListOfGateways() {
        List<Gateway> gateways = Collections.singletonList(gateway);
        when(gatewayService.fetchGatewaysBySensorType("electricity")).thenReturn(gateways);

        ResponseEntity<List<Gateway>> response = gatewayController.fetchGatewaysBySensorType("electricity");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(gateways, response.getBody());
    }

    @Test
    void fetchAllGateways_ShouldReturnListOfGateways() throws InvalidRequestException, GatewayNotFoundException {
        List<Gateway> gateways = Collections.singletonList(gateway);
        when(gatewayService.fetchAllGateways()).thenReturn(gateways);

        ResponseEntity<List<Gateway>> response = gatewayController.fetchAllGateways();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(gateways, response.getBody());
    }

    @Test
    void fetchAllGateways_ShouldReturnInternalServerErrorOnException() throws InvalidRequestException, GatewayNotFoundException {
        when(gatewayService.fetchAllGateways()).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<List<Gateway>> response = gatewayController.fetchAllGateways();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
