package com.energybox.backendcodingchallenge.controller;

import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.exception.GatewayNotFoundException;
import com.energybox.backendcodingchallenge.exception.InvalidRequestException;
import com.energybox.backendcodingchallenge.model.GatewayRequest;
import com.energybox.backendcodingchallenge.model.GatewayResponse;
import com.energybox.backendcodingchallenge.service.GatewayService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing gateway operations.
 */
@RestController
@RequestMapping( value = "/gateways" )
public class GatewayController {

    private static final Logger logger = LoggerFactory.getLogger(GatewayController.class);

    private final GatewayService service;

    public GatewayController( GatewayService service ) {
        this.service = service;
    }

    /**
     * Creates a new gateway.
     *
     * @param gatewayRequest the request object containing gateway details
     * @return a ResponseEntity containing the created gateway response or an error message
     */
    @ApiOperation( value = "create a new gateway", response = Gateway.class )
    @RequestMapping( value = "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<GatewayResponse> createGateway(
            @RequestBody GatewayRequest gatewayRequest
    ) throws InvalidRequestException {
        logger.info("Received request to create gateway with name: {}", gatewayRequest.getName());
        try {
            if (gatewayRequest.getName() == null) {
                throw new InvalidRequestException("Gateway ID cannot be empty");
            }
            GatewayResponse response = service.createGateway(gatewayRequest);
            response.setStatus(HttpStatus.OK);
            logger.info("Successfully created gateway with ID: {}", response.getGateWayId());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InvalidRequestException e) {
            logger.info("Error creating gateway with ID: {}", gatewayRequest.getName());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            logger.error("Error creating gateway with ID:", e);
            return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Fetches a gateway by its ID.
     *
     * @param gatewayId the ID of the gateway to fetch
     * @return a ResponseEntity containing the gateway or an error message
     */
    @ApiOperation( value = "fetch a gateway by Id ", response = Gateway.class )
    @RequestMapping( value = "gateway-id/{gatewayId}", method = RequestMethod.GET )
    public ResponseEntity<Object> getSensorByGatewayId(@PathVariable Long gatewayId) throws InvalidRequestException, GatewayNotFoundException {
        logger.info("Received request to fetch gateway with ID: {}", gatewayId);
        try {
            Gateway result = service.fetchGatewayById(gatewayId);
            logger.info("Successfully fetched gateway with ID: {}", gatewayId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (GatewayNotFoundException e) {
            logger.warn("Gateway not found with ID: {}", gatewayId);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            logger.error("Error occurred while fetching gateway with ID: {}", gatewayId, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Fetches gateways by sensor type.
     *
     * @param type the sensor type to filter gateways
     * @return a ResponseEntity containing a list of gateways matching the sensor type
     */
    @ApiOperation(value = "Fetch gateways with specific sensor type", response = Gateway.class, responseContainer = "List")
    @RequestMapping(value = "/{type}", method = RequestMethod.GET)
    public ResponseEntity<List<Gateway>> fetchGatewaysBySensorType(@PathVariable String type) {
        logger.info("Received request to fetch gateways with sensor type: {}", type);

        try {
            List<Gateway> result = service.fetchGatewaysBySensorType(type);
            logger.info("Successfully fetched {} gateways for sensor type: {}", result.size(), type);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error("Error occurred while fetching gateways for sensor type: {}", type, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Fetches all gateways.
     *
     * @return a ResponseEntity containing a list of all gateways
     */
    @ApiOperation( value = "fetch all gateways ", response = Gateway.class )
    @RequestMapping( value = "", method = RequestMethod.GET )
    public ResponseEntity<List<Gateway>> fetchAllGateways() throws InvalidRequestException, GatewayNotFoundException {
        logger.info("Received request to fetch all gateways");

        try {
            List<Gateway> result = service.fetchAllGateways();
            logger.info("Successfully fetched {} gateways", result.size());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while fetching all gateways", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
