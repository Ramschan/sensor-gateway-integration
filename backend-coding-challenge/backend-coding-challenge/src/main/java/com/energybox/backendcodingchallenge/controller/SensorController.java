package com.energybox.backendcodingchallenge.controller;

import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.domain.LastReading;
import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.domain.SensorType;
import com.energybox.backendcodingchallenge.exception.*;
import com.energybox.backendcodingchallenge.model.*;
import com.energybox.backendcodingchallenge.service.SensorService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing sensors.
 */
@RestController
@RequestMapping( value =  "/sensors")
public class SensorController {

    private static final Logger logger = LoggerFactory.getLogger(SensorController.class);

    private final SensorService sensorService;

    public SensorController(SensorService service) {
        this.sensorService = service;
    }

    /**
     * Creates a new sensor.
     *
     * @param sensorRequest The details of the sensor to be created.
     * @return The created sensor response.
     * @throws InvalidRequestException if the sensor type or ID is invalid.
     */
    @ApiOperation(value = "create a sensor", response = Sensor.class)
    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SensorResponse> createSensor(
            @RequestBody SensorRequest sensorRequest
    ) throws InvalidRequestException {
        logger.info("Received request to create sensor with name: {}", sensorRequest.getName());

        if (sensorRequest.getName().isEmpty() || sensorRequest.getLocationCode().isEmpty())
            throw new InvalidRequestException("Sensor Name or Location Code is missing in the request");

        try {
            SensorResponse sensorResponse = sensorService.createSensors(sensorRequest);
            logger.info("Successfully created sensor with ID: {}", sensorResponse.getSensorId());
            sensorResponse.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(sensorResponse, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error("Error occurred while creating sensor with name: {}", sensorRequest.getName(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Fetches all sensors in the DB.
     *
     * @return A list of all sensors.
     */
    @ApiOperation(value = "fetch all sensors", response = Sensor.class, responseContainer = "List")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<Sensor>> fetchAllSensors() {
        logger.info("Fetching all sensors");
        List<Sensor> response = sensorService.fetchAllSensors();
        if (response.isEmpty()) {
            logger.info("No sensors found");
            return ResponseEntity.noContent().build();
        }
        logger.info("Successfully retrieved {} sensors", response.size());
        return ResponseEntity.ok(response);
    }

    /**
     * Fetches sensors by their type.
     *
     * @param sensorType The type of the sensors to be fetched.
     * @return A list of sensors of the specified type.
     */
    @ApiOperation(value = "fetch all sensors of input type", response = Sensor.class, responseContainer = "List")
    @RequestMapping(value = "type/{sensorType}", method = RequestMethod.GET)
    public ResponseEntity<List<Sensor>> fetchSensorBySensorType(@PathVariable String sensorType) {
        logger.info("Fetching sensors of type: {}", sensorType);

        List<Sensor> sensors = sensorService.getSensorsByType(sensorType);

        if (sensors.isEmpty()) {
            logger.info("No sensors found for type: {}", sensorType);
            return ResponseEntity.noContent().build();
        }

        logger.info("Successfully retrieved {} sensors for type: {}", sensors.size(), sensorType);
        return ResponseEntity.ok(sensors); // 200 OK
    }

    /**
     * Fetches a sensor by its ID.
     *
     * @param sensorId The ID of the sensor to be fetched.
     * @return The sensor with the specified ID.
     * @throws InvalidRequestException if the sensor ID is invalid.
     * @throws SensorNotFoundException if the sensor with the specified ID is not found.
     */
    @ApiOperation(value = "fetch a sensor by id ", response = Sensor.class)
    @RequestMapping(value = "/{sensorId}", method = RequestMethod.GET)
    public ResponseEntity<Sensor> fetchSensoryById(@PathVariable Long sensorId) throws InvalidRequestException, GatewayNotFoundException {
        try {
            Sensor sensor = sensorService.fetchSensorById(sensorId);
            logger.info("Successfully retrieved sensor with ID: {}", sensorId);
            return ResponseEntity.ok(sensor);
        } catch (RuntimeException e) {
            logger.error("Unexpected error occurred while fetching sensor with ID: {}", sensorId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }

    /**
     * Attaches a type to a sensor.
     *
     * @param sensorTypeRequest The request containing sensor ID and type to be attached.
     * @return A success message if the type was attached successfully.
     */
    @ApiOperation(value = "attach type to a sensor", response = SensorType.class)
    @RequestMapping(value = "/attachType", method = RequestMethod.PUT)
    public ResponseEntity<String> addSensorType(@RequestBody SensorTypeRequest sensorTypeRequest) {
        logger.info("Received request to attach type {} to sensor with ID: {}", sensorTypeRequest.getType(), sensorTypeRequest.getId());
        try {
            sensorService.addSensorType(sensorTypeRequest.getId(), sensorTypeRequest.getType());
            return ResponseEntity.ok("Sensor type added successfully.");
        } catch (SensorNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Assigns a sensor to a gateway.
     *
     * @param request The request containing sensor ID and gateway ID.
     * @return A success message if the sensor was successfully assigned to the gateway.
     * @throws InvalidRequestException         if the request contains invalid data.
     * @throws SensorNotFoundException         if the sensor is not found.
     * @throws GatewayNotFoundException        if the gateway is not found.
     * @throws SensorAlreadyConnectedException if the sensor is already connected to the gateway.
     */
    @ApiOperation(value = "assign a given sensory to gateway", response = Gateway.class)
    @RequestMapping(value = "/to-gateway", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> assignSensorGateway(@RequestBody SensorGatewayRequest request)
            throws InvalidRequestException, GatewayNotFoundException, SensorNotFoundException {
        try {
            // Validate input
            if (request.getGatewayId() == null || request.getSensorId() == null) {
                throw new InvalidRequestException("Gateway ID and Sensor ID must be provided");
            }

            // Process request
            sensorService.assignSensorToGateway(request.getSensorId(), request.getGatewayId());

            return new ResponseEntity<>("Sensor is tagged to Gateway successfully", HttpStatus.OK);
        } catch (InvalidRequestException | SensorNotFoundException | GatewayNotFoundException |
                 SensorAlreadyConnectedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Retrieves all sensors that are connected to a specific/input gateway.
     */
    @ApiOperation(value = "fetches all the sensors attached to the requested gateway", response = Sensor.class)
    @RequestMapping(value = "/gateway-id/{gatewayId}", method = RequestMethod.GET)
    public ResponseEntity<List<Sensor>> getSensorsByGateway(@PathVariable Long gatewayId) {
        try {
            List<Sensor> sensors = sensorService.getSensorsByGateway(gatewayId);
            return ResponseEntity.ok(sensors);
        } catch (RuntimeException e) {
            // Handle runtime exceptions and return an appropriate response
            return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves the most recent readings for a specified sensor.
     * Returns a list of {LastReading} objects for the given sensor ID.
     * Responds with 404 Not Found if the sensor does not exist.
     */
    @ApiOperation(value = "returns all reading s associated with the sensor", response = LastReading.class)
    @RequestMapping(value = "/get-last-readings/{sensorId}", method = RequestMethod.GET)
    public ResponseEntity<List<LastReading>> getLastReadingsForSensor(@PathVariable Long sensorId) {
        try {
            List<LastReading> readings = sensorService.getLastReadings(sensorId);
            return ResponseEntity.ok(readings);
        } catch (SensorNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Adds or updates the most recent reading for a specified sensor.
     * Creates or updates a {LastReading} for the given sensor ID and typeName.
     * Returns 404 Not Found if the sensor or sensor type is not found.
     */
    @ApiOperation(value = "returns all reading s associated with the sensor", response = LastReading.class)
    @RequestMapping(value = "/add-last-readings/", method = RequestMethod.PUT)
    public ResponseEntity<Void> addLastReading(@RequestBody ReadingRequest readingRequest) {
        try {
            sensorService.addOrUpdateReading(readingRequest.getSensorId(), readingRequest.getSensorType(), readingRequest.getReading());
            return ResponseEntity.ok().build();
        } catch (SensorNotFoundException | SensorTypeNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
