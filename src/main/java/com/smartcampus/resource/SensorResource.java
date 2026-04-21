package com.smartcampus.resource;

import com.smartcampus.DataStore;
import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.exception.SensorNotFoundException;
import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;


@Path("/sensors")
public class SensorResource {

    /*
      GET /api/v1/sensors - Retrieve all sensors and filtered by type
     */
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sensor> getAllSensors(@QueryParam("type") String type) {
        List<Sensor> allSensors = new ArrayList<>(DataStore.sensors.values());
        if (type == null || type.trim().isEmpty()) {
            return allSensors;
        }
       
        List<Sensor> filtered = new ArrayList<>();
        for (Sensor s : allSensors) {
            if (s.getType() != null && s.getType().equalsIgnoreCase(type)) {
                filtered.add(s);
            }
        }
        return filtered;
    }

    /*
      GET /api/v1/sensors/{sensorId} - For getting a specific sensor
     */
    
    @GET
    @Path("/{sensorId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Sensor getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            throw new SensorNotFoundException("Sensor with ID '" + sensorId + "' was not found.");
        }
        return sensor;
    }

    /*
      POST /api/v1/sensors - for registering a new sensor
     */
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSensor(Sensor sensor) {
        if (sensor.getId() == null || sensor.getId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Sensor ID is required\"}")
                    .build();
        }
        if (DataStore.sensors.containsKey(sensor.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"Sensor with this ID already exists\"}")
                    .build();
        }
        
        if (sensor.getRoomId() == null || !DataStore.rooms.containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException(
                "The roomId '" + sensor.getRoomId() + "' does not reference an existing room. " +
                "Please provide a valid roomId."
            );
        }
        DataStore.sensors.put(sensor.getId(), sensor);
        
        Room room = DataStore.rooms.get(sensor.getRoomId());
        room.getSensorIds().add(sensor.getId());

        return Response.status(Response.Status.CREATED).entity(sensor).build();
    }

    /*
      DELETE /api/v1/sensors/{sensorId} - Remove a sensor
     */
    
    @DELETE
    @Path("/{sensorId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            throw new SensorNotFoundException("Sensor with ID '" + sensorId + "' was not found.");
        }
        
        if (sensor.getRoomId() != null && DataStore.rooms.containsKey(sensor.getRoomId())) {
            DataStore.rooms.get(sensor.getRoomId()).getSensorIds().remove(sensorId);
        }
        DataStore.sensors.remove(sensorId);
        DataStore.sensorReadings.remove(sensorId);
        return Response.ok("{\"message\":\"Sensor '" + sensorId + "' successfully deleted.\"}").build();
    }

    
    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingsResource(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            throw new SensorNotFoundException("Sensor with ID '" + sensorId + "' was not found.");
        }
        return new SensorReadingResource(sensorId);
    }
}
