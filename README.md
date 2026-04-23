# 🏫 Smart-Campus-JAX-RS-REST-API 

A RESTful API built using JAX-RS (Jersey) and Apache Tomcat to manage university rooms and IoT sensors.  
Developed for the 5COSC022W Client-Server Architectures module.

---

## 📌 API Design Overview

This API follows **REST principles** with a clear resource-based architecture:

- Rooms → core entities representing physical spaces  
- Sensors → linked to rooms   
- Sensor Readings → implemented as nested sub-resources

### Key Features
- RESTful endpoints with proper HTTP methods (GET, POST, DELETE)
- JSON-based communication
- Query parameter filtering ('?type=CO2')
- Sub-resource design ('/sensors/{id}/readings')
- Structured error handling with appropriate status codes

---

## ⚙️ Tech Stack

- Java 8+
- JAX-RS (Jersey)
- Maven
- Apache Tomcat
- In-memory storage ('ConcurrentHashMap')

---

## ▶️ Build & Run Instructions

### 1. Clone Repository
```bash
git clone https://github.com/thaminduperera03-dot/Smart-Campus-JAX-RS-REST-API.git
cd Smart-Campus-JAX-RS-REST-API
```

### 2. Access API
```
http://localhost:8080/SmartCampusAPI/api/v1
```

---

## 🔗 API Endpoints

 Resource  Endpoint 

 Rooms  `/rooms` 
 Sensors  `/sensors` 
 Readings  `/sensors/{id}/readings` 

---

## 🧪 Sample curl Commands

### 1. API Discovery
```bash
curl 1 - GET http://localhost:8080/SmartCampusAPI/api/v1
```

### 2. Get All Rooms
```bash
curl 2 - GET http://localhost:8080/SmartCampusAPI/api/v1/rooms
```

### 3. Create Room
```bash
curl 3 - POST http://localhost:8080/SmartCampusAPI/api/v1/rooms 
```

### 4. Register Sensor
```bash
curl 4 - POST http://localhost:8080/SmartCampusAPI/api/v1/sensors 
```

### 5. Filter Sensors
```bash
curl 5 - GET "http://localhost:8080/SmartCampusAPI/api/v1/sensors?type=CO2"
```

### 6. Add Sensor Reading
```bash
curl 6 - POST http://localhost:8080/SmartCampusAPI/api/v1/sensors/Temp-01/readings 
```

---

- Uses in-memory storage (data resets on restart)
- Returns structured JSON responses only
- Implements proper HTTP status codes:
  - '200' OK
  - '201' Created
  - '404' Not Found
  - '409' Conflict
  - '422' Unprocessable Entity

---

