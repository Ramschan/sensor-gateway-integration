package com.energybox.backendcodingchallenge.service;

import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.domain.LastReading;
import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.domain.SensorType;
import com.energybox.backendcodingchallenge.exception.GatewayNotFoundException;
import com.energybox.backendcodingchallenge.exception.SensorAlreadyConnectedException;
import com.energybox.backendcodingchallenge.exception.SensorNotFoundException;
import com.energybox.backendcodingchallenge.exception.SensorTypeNotFoundException;
import com.energybox.backendcodingchallenge.model.SensorRequest;
import com.energybox.backendcodingchallenge.model.SensorResponse;
import com.energybox.backendcodingchallenge.repository.GatewayRepository;
import com.energybox.backendcodingchallenge.repository.SensorRepository;
import com.energybox.backendcodingchallenge.repository.SensorTypeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SensorService {

    private final SensorRepository sensorRepository;

    private final GatewayRepository gatewayRepository;

    private final SensorTypeRepository sensorTypeRepository;

    public SensorService(SensorRepository sensorRepository, GatewayRepository gatewayRepository, SensorTypeRepository sensorTypeRepository) {
        this.sensorRepository = sensorRepository;
        this.gatewayRepository = gatewayRepository;
        this.sensorTypeRepository = sensorTypeRepository;
    }

    /**
     * Creates a new Sensor.
     *
     * @param sensorRequest The request object containing sensor details.
     * @return A response object containing the ID of the newly created sensor.
     */
    public SensorResponse createSensors(SensorRequest sensorRequest) {

        //sensor request to sensor mapping
        Sensor sensor = new Sensor(sensorRequest.getName(),sensorRequest.getLocationCode());

        //call repo to insert sensor into db
        Sensor result = sensorRepository.save(sensor);
        //create sensor response model and return it

        Set<SensorType> sensorTypes = fetchOrCreateSensorTypes(sensorRequest.getType());
        sensor.setTypes(sensorTypes);

        SensorResponse response = new SensorResponse();
        response.setSensorId(result.getId());

        return response;
    }

    private Set<SensorType> fetchOrCreateSensorTypes(Set<String> typeNames) {
        Set<SensorType> sensorTypes = new HashSet<>();

        for (String typeName : typeNames) {
            SensorType sensorType = sensorTypeRepository.findByName(typeName);
            if (sensorType == null) {
                sensorType = new SensorType(typeName);
                sensorTypeRepository.save(sensorType);
            }
            sensorTypes.add(sensorType);
        }

        return sensorTypes;
    }

    /**
     * Fetches all Sensors.
     *
     * @return A set of all sensors.
     */
    public List<Sensor> fetchAllSensors() {
        return new ArrayList<>(sensorRepository.findAll());
    }

    /**
     * Fetches a Sensor by its ID.
     *
     * @param id The ID of the sensor to be fetched.
     * @return The Sensor with the specified ID.
     * @throws SensorNotFoundException if no Sensor is found with the given ID.
     */
    public Sensor fetchSensorById(Long id)  {
        return sensorRepository.findById(id)
                .orElseThrow(() -> new SensorNotFoundException("Sensor not found with ID: " + id));
    }

    /**
     * Fetches Sensors by type name.
     *
     * @param typeName The name of the sensor type to filter by.
     * @return A list of Sensors of the specified type.
     */
    public List<Sensor> getSensorsByType(String typeName) {
        return sensorRepository.findSensorsByTypeName(typeName);
    }

    /**
     * Assigns a Sensor to a Gateway.
     *
     * @param sensorId The ID of the Sensor to be assigned.
     * @param gatewayId The ID of the Gateway to assign the Sensor to.
     * @throws SensorNotFoundException if the sensor is not found.
     * @throws GatewayNotFoundException if the gateway is not found.
     * @throws SensorAlreadyConnectedException if the sensor is already connected to a gateway.
     */
    public void assignSensorToGateway(Long sensorId, Long gatewayId) {
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new SensorNotFoundException("Sensor ID " + sensorId + " does not exist. Please create a sensor first."));

        if(sensor.getGateway() != null){
            throw  new SensorAlreadyConnectedException("Sensor ID " + sensorId + " is already connected to " + sensor.getGateway().getId());
        }

        Gateway gateway = gatewayRepository.findById(gatewayId)
                .orElseThrow(() -> new GatewayNotFoundException("Gateway ID " + gatewayId + " does not exist. Please create a Gateway first."));

        //Gateway not found
        sensor.setGateway(gateway);
        sensorRepository.save(sensor);
    }

    /**
     * Adds a SensorType to a Sensor.
     *
     * @param sensorId The ID of the Sensor.
     * @param typeName The name of the SensorType to be added.
     * @throws RuntimeException if the sensor or sensor type is not found.
     */
    public void addSensorType(Long sensorId, String typeName) {
        // Find the sensor or throw an exception if not found
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new SensorNotFoundException("Sensor not found"));

        // Find or create the sensor type
        SensorType sensorType = findOrCreateSensorType(typeName);

        // Add the sensor type to the sensor's types set
        sensor.getTypes().add(sensorType);

        // Save both entities to ensure the relationships are updated
        sensorRepository.save(sensor);
        sensorTypeRepository.save(sensorType);
    }

    /**
     * Retrieves all Sensor entities associated with a given gateway.
     * Returns a list of sensors connected to the gateway identified by the specified ID.
     *
     * @param gatewayId The ID of the gateway.
     * @return A list of Sensor objects.
     */
    public List<Sensor> getSensorsByGateway(Long gatewayId) {
        return sensorRepository.findAllByGatewayId(gatewayId);
    }

    /**
     * Retrieves the most recent readings for a specified sensor.
     * Returns a list of the latest readings for the sensor identified by the given ID.
     *
     * @param sensorId The ID of the sensor.
     * @return A list of LastReading objects.
     * @throws SensorNotFoundException If the sensor is not found.
     */
    public List<LastReading> getLastReadings(Long sensorId) {
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new SensorNotFoundException("Sensor not found"));
        return new ArrayList<>(sensor.getLastReadings().values());
    }

    /**
     * Adds or updates the most recent reading for a specific sensor.
     *
     * Creates or updates a LastReading for the given sensor ID and type name.
     *
     * @param sensorId The ID of the sensor.
     * @param typeName The name of the sensor type.
     * @param reading The reading value to add or update.
     * @throws SensorNotFoundException If the sensor is not found.
     * @throws SensorTypeNotFoundException If the sensor type is not found.
     */
    public void addOrUpdateReading(Long sensorId, String typeName, Double reading) {
        // Find the sensor
        LastReading lastReading = new LastReading(LocalDateTime.now(),reading);
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new SensorNotFoundException("Sensor not found"));

        // Find the sensor type ir create new sensor type if it does not exist
        SensorType sensorType = findOrCreateSensorType(typeName);

        // Update or create the reading
        sensor.addReading(sensorType, lastReading);

        // Save the updated sensor
        sensorRepository.save(sensor);
    }

    /**
     * Finds an existing SensorType by name or creates a new one if it doesn't exist.
     * Checks the repository for the sensor type with the given name and creates it if absent.
     *
     * @param typeName The name of the sensor type.
     * @return The existing or newly created SensorType.
     */
    private SensorType findOrCreateSensorType(String typeName) {
        // Find the sensor type or create a new one if it doesn't exist
        SensorType sensorType = sensorTypeRepository.findByName(typeName);
        if (sensorType == null) {
            sensorType = new SensorType(typeName);
            sensorTypeRepository.save(sensorType);
        }
        return sensorType;
    }
}
