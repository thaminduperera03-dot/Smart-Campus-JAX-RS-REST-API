package com.smartcampus;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class DataStore {

    public static final Map<String, Room> rooms = new ConcurrentHashMap<>();
    public static final Map<String, Sensor> sensors = new ConcurrentHashMap<>();
    public static final Map<String, List<SensorReading>> sensorReadings = new ConcurrentHashMap<>();

    static {
        // Available rooms
        Room r1 = new Room("2LA", "Study Area", 50, new ArrayList<>());
        Room r2 = new Room("3LB", "Computer Lab", 30, new ArrayList<>());
        Room r3 = new Room("5LA", "Main Lecture Hall", 200, new ArrayList<>());
        rooms.put(r1.getId(), r1);
        rooms.put(r2.getId(), r2);
        rooms.put(r3.getId(), r3);

        // Sensors
        Sensor s1 = new Sensor("Temp-01", "Temperature", "ACTIVE", 22.5, "2LA");
        Sensor s2 = new Sensor("CO2-02", "CO2", "ACTIVE", 410.0, "3LB");
        Sensor s3 = new Sensor("Occ-03", "Occupancy", "MAINTENANCE", 0.0, "5LA");
        sensors.put(s1.getId(), s1);
        sensors.put(s2.getId(), s2);
        sensors.put(s3.getId(), s3);

        r1.getSensorIds().add("Temp-01");
        r2.getSensorIds().add("CO2-02");
        r3.getSensorIds().add("Occ-03");

        List<SensorReading> readings1 = new ArrayList<>();
        readings1.add(new SensorReading("Read-01", System.currentTimeMillis() - 60000, 22.5));
        sensorReadings.put("Temp-01", readings1);

        List<SensorReading> readings2 = new ArrayList<>();
        readings2.add(new SensorReading("Read-02", System.currentTimeMillis() - 30000, 410.0));
        sensorReadings.put("CO2-02", readings2);
    }
}
