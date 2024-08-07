# sensor-gateway-integration


---

# API Documentation

## Overview

This API documentation describes the endpoints available for managing gateways and sensors in the EnergyBox Backend Coding Challenge application. The API is divided into two main controllers:

1. **GatewayController**: Manages gateway-related operations.
2. **SensorController**: Manages sensor-related operations.

## GatewayController

The `GatewayController` handles operations related to gateways. The endpoints allow you to create, fetch, and manage gateways.

### Base URL

`/gateways`

### Endpoints

#### 1. Create a New Gateway

- **URL**: `/gateways/add`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "name": "Gateway Name"
  }
  ```
- **Response**:
  - **Success (200 OK)**:
    ```json
    {
      "gateWayId": 1
    }
    ```
  - **Error (400 Bad Request)**: If the gateway name is missing or invalid.

#### 2. Fetch a Gateway by ID

- **URL**: `/gateways/gateway-id/{gatewayId}`
- **Method**: `GET`
- **Path Parameter**: `gatewayId` (Long)
- **Response**:
  - **Success (200 OK)**:
    ```json
    {
      "id": 1,
      "name": "Gateway Name"
    }
    ```
  - **Error (404 Not Found)**: If the gateway with the specified ID is not found.

#### 3. Fetch Gateways by Sensor Type

- **URL**: `/gateways/{type}`
- **Method**: `GET`
- **Path Parameter**: `type` (String)
- **Response**:
  - **Success (200 OK)**:
    ```json
    [
      {
        "id": 1,
        "name": "Gateway Name"
      }
    ]
    ```
  - **Error (500 Internal Server Error)**: For unexpected errors.

#### 4. Fetch All Gateways

- **URL**: `/gateways`
- **Method**: `GET`
- **Response**:
  - **Success (200 OK)**:
    ```json
    [
      {
        "id": 1,
        "name": "Gateway Name"
      }
    ]
    ```
  - **Error (500 Internal Server Error)**: For unexpected errors.

## SensorController

The `SensorController` handles operations related to sensors. The endpoints allow you to create, fetch, and manage sensors, as well as interact with their readings.

### Base URL

`/sensors`

### Endpoints

#### 1. Create a New Sensor

- **URL**: `/sensors/add`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "name": "Sensor Name",
    "locationCode": "LOC123"
  }
  ```
- **Response**:
  - **Success (200 OK)**:
    ```json
    {
      "sensorId": 1
    }
    ```
  - **Error (400 Bad Request)**: If the sensor name or location code is missing.
  - **Error (500 Internal Server Error)**: For unexpected errors.

#### 2. Fetch All Sensors

- **URL**: `/sensors`
- **Method**: `GET`
- **Response**:
  - **Success (200 OK)**:
    ```json
    [
      {
        "id": 1,
        "name": "Sensor Name",
        "locationCode": "LOC123"
      }
    ]
    ```
  - **Error (204 No Content)**: If no sensors are found.
  - **Error (500 Internal Server Error)**: For unexpected errors.

#### 3. Fetch Sensors by Type

- **URL**: `/sensors/type/{sensorType}`
- **Method**: `GET`
- **Path Parameter**: `sensorType` (String)
- **Response**:
  - **Success (200 OK)**:
    ```json
    [
      {
        "id": 1,
        "name": "Sensor Name",
        "locationCode": "LOC123"
      }
    ]
    ```
  - **Error (204 No Content)**: If no sensors of the specified type are found.
  - **Error (500 Internal Server Error)**: For unexpected errors.

#### 4. Fetch a Sensor by ID

- **URL**: `/sensors/{sensorId}`
- **Method**: `GET`
- **Path Parameter**: `sensorId` (Long)
- **Response**:
  - **Success (200 OK)**:
    ```json
    {
      "id": 1,
      "name": "Sensor Name",
      "locationCode": "LOC123"
    }
    ```
  - **Error (404 Not Found)**: If the sensor with the specified ID is not found.
  - **Error (500 Internal Server Error)**: For unexpected errors.

#### 5. Attach Type to Sensor

- **URL**: `/sensors/attachType`
- **Method**: `PUT`
- **Request Body**:
  ```json
  {
    "id": 1,
    "type": "Temperature"
  }
  ```
- **Response**:
  - **Success (200 OK)**:
    ```json
    "Sensor type added successfully."
    ```
  - **Error (400 Bad Request)**: If the sensor is not found.
  - **Error (500 Internal Server Error)**: For unexpected errors.

#### 6. Assign Sensor to Gateway

- **URL**: `/sensors/to-gateway`
- **Method**: `PUT`
- **Request Body**:
  ```json
  {
    "sensorId": 1,
    "gatewayId": 1
  }
  ```
- **Response**:
  - **Success (200 OK)**:
    ```json
    "Sensor is tagged to Gateway successfully"
    ```
  - **Error (400 Bad Request)**: If the request contains invalid data or if the sensor or gateway is not found.
  - **Error (500 Internal Server Error)**: For unexpected errors.

#### 7. Fetch Sensors by Gateway

- **URL**: `/sensors/gateway-id/{gatewayId}`
- **Method**: `GET`
- **Path Parameter**: `gatewayId` (Long)
- **Response**:
  - **Success (200 OK)**:
    ```json
    [
      {
        "id": 1,
        "name": "Sensor Name",
        "locationCode": "LOC123"
      }
    ]
    ```
  - **Error (500 Internal Server Error)**: For unexpected errors.

#### 8. Get Last Readings for Sensor

- **URL**: `/sensors/get-last-readings/{sensorId}`
- **Method**: `GET`
- **Path Parameter**: `sensorId` (Long)
- **Response**:
  - **Success (200 OK)**:
    ```json
    [
      {
        "sensorId": 1,
        "typeName": "Temperature",
        "reading": 22.5
      }
    ]
    ```
  - **Error (404 Not Found)**: If the sensor or readings are not found.
  - **Error (500 Internal Server Error)**: For unexpected errors.

#### 9. Add or Update Last Reading

- **URL**: `/sensors/add-last-readings`
- **Method**: `PUT`
- **Request Body**:
  ```json
  {
    "sensorId": 1,
    "sensorType": "Temperature",
    "reading": 22.5
  }
  ```
- **Response**:
  - **Success (200 OK)**: No content
  - **Error (404 Not Found)**: If the sensor or sensor type is not found.
  - **Error (500 Internal Server Error)**: For unexpected errors.


## Logging

All operations are logged for tracking and debugging purposes.

---
