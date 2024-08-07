package com.energybox.backendcodingchallenge.service;

import com.energybox.backendcodingchallenge.controller.GatewayController;
import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.exception.GatewayNotFoundException;
import com.energybox.backendcodingchallenge.model.GatewayRequest;
import com.energybox.backendcodingchallenge.model.GatewayResponse;
import com.energybox.backendcodingchallenge.repository.GatewayRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GatewayService {

    private static final Logger logger = LoggerFactory.getLogger(GatewayService.class);

    private final GatewayRepository gatewayRepository;

    public GatewayService( GatewayRepository gatewayRepository) {
        this.gatewayRepository = gatewayRepository;
    }

    /**
     * Creates a new Gateway.
     *
     * @param gatewayRequest The request object containing gateway details.
     * @return A response object containing the ID of the newly created gateway.
     */
    public GatewayResponse createGateway(GatewayRequest gatewayRequest) {
        logger.info("Creating new Gateway with name: {}", gatewayRequest.getName());
        // Map gateway request to gateway entity
        Gateway gateway = new Gateway(gatewayRequest.getName());
        // Save gateway entity to the repository
        Gateway result = gatewayRepository.save(gateway);

        // Create and return response with the ID of the newly created gateway
        GatewayResponse response = new GatewayResponse();
        response.setGateWayId(result.getId());
        logger.info("Created Gateway with ID: {}", result.getId());
        return response;
    }

    /**
     * Fetches all Gateways from the repository.
     *
     * @return A list of all Gateways.
     */
    public List<Gateway> fetchAllGateways() {
        return new ArrayList<>(gatewayRepository.findAll()) {
        };
    }

    /**
     * Fetches a Gateway by its ID.
     *
     * @param id The ID of the Gateway to be fetched.
     * @return The Gateway with the specified ID.
     * @throws GatewayNotFoundException if no Gateway is found with the given ID.
     */
    public Gateway fetchGatewayById(Long id)  {
        return gatewayRepository.findById(id)
                .orElseThrow(() -> new GatewayNotFoundException("Gateway not found with ID: " + id));
    }

    /**
     * Fetches Gateways by the sensor type associated with them.
     *
     * @param sensorType The sensor type to filter Gateways by.
     * @return A list of Gateways associated with the specified sensor type.
     */
    public List<Gateway> fetchGatewaysBySensorType(String sensorType) {
        return gatewayRepository.findGatewaysWithSensorType(sensorType);
    }

}
