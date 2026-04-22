**Smart Campus Sensor & Room Management API**


**Project Information**

Student Name: Praveen Dulshan Wijesundara
Student ID: w2120636 / 20231294
Module: Client-Server Architectures (5COSC022W)
University: University of Westminster / IIT
Module Leader: Hamed Hamzeh
Submission Date: 24th April 2026


**Quick Start Guide**

Prerequisites: Java 21, Maven 3.8+, Tomcat 9.0

Build and Deploy:
git clone https://github.com/Dulshan9/smart-campus-api-2026.git
cd smart-campus-api-2026
mvn clean package
copy target\smart-campus-api.war "C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps\"
net start Tomcat9

Access: http://localhost:8080/smart-campus-api/api/v1


**API Endpoints**

GET    /api/v1                           Discovery endpoint
GET    /api/v1/rooms                     List all rooms
POST   /api/v1/rooms                     Create a room
GET    /api/v1/rooms/{id}                Get room by ID
DELETE /api/v1/rooms/{id}                Delete room
GET    /api/v1/sensors                   List all sensors
GET    /api/v1/sensors?type={type}       Filter sensors by type
POST   /api/v1/sensors                   Create a sensor
GET    /api/v1/sensors/{id}/readings     Get sensor readings
POST   /api/v1/sensors/{id}/readings     Add a reading


**Sample Requests**

Discovery:
curl -X GET http://localhost:8080/smart-campus-api/api/v1

Create Room:
curl -X POST http://localhost:8080/smart-campus-api/api/v1/rooms -H "Content-Type: application/json" -d "{\"roomId\":\"R105\",\"name\":\"Lab\",\"roomType\":\"LAB\",\"capacity\":35,\"floor\":3,\"location\":\"Cavendish\"}"

Get All Rooms:
curl -X GET http://localhost:8080/smart-campus-api/api/v1/rooms

Create Sensor:
curl -X POST http://localhost:8080/smart-campus-api/api/v1/sensors -H "Content-Type: application/json" -d "{\"sensorId\":\"S010\",\"type\":\"TEMPERATURE\",\"status\":\"ACTIVE\",\"roomId\":\"R001\"}"

Filter Sensors:
curl -X GET "http://localhost:8080/smart-campus-api/api/v1/sensors?type=CO2"

Add Reading:
curl -X POST http://localhost:8080/smart-campus-api/api/v1/sensors/S001/readings -H "Content-Type: application/json" -d "{\"value\":24.5}"


**Error Response Examples**

409 Conflict (Delete room with sensors):
{"status":409,"error":"Conflict","message":"Cannot delete room - it contains active sensors","roomId":"R001","sensorCount":3}

422 Unprocessable Entity (Invalid room reference):
{"status":422,"error":"Unprocessable Entity","message":"Room with ID 'INVALID' not found","resourceType":"Room"}

403 Forbidden (Sensor in maintenance):
{"status":403,"error":"Forbidden","message":"Sensor is not available for readings","sensorId":"S005","currentStatus":"MAINTENANCE"}


**Theoretical Questions and Answers**

**Part 1: Service Architecture and Setup**

**Q1: Explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton?**

JAX-RS resource classes are request-scoped by default. A new instance is created for each HTTP request and destroyed after completion. This means instance variables cannot persist data across requests. To maintain state, I implemented a Singleton DataStore class with double-checked locking. Shared data uses ConcurrentHashMap to ensure thread safety and prevent race conditions.

**Q2: Why is the provision of Hypermedia (HATEOAS) considered a hallmark of advanced RESTful design?**

HATEOAS allows clients to navigate the API dynamically through links in responses rather than hardcoded URLs. This provides discoverability, loose coupling, and self-documentation. The server can change URL structures without breaking clients. My implementation includes a discovery endpoint at /api/v1 that returns links to available resources.


**Part 2: Room Management**

**Q3: When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects?**

Returning only IDs minimises bandwidth but forces clients to make additional requests for each room's details, creating the N+1 problem. Returning full objects consumes more bandwidth initially but provides all data in a single request. I chose to return full objects for simplicity and better user experience.

**Q4: Is the DELETE operation idempotent in your implementation?**

Yes, DELETE is idempotent. The first request removes the room and returns 204. The second request finds the room already deleted and returns 422. The server state remains the same after both requests. Idempotency concerns server state, not response codes. If the room has sensors, all requests return 409 and the room remains unchanged.


**Part 3: Sensor Operations and Linking**

**Q5: What happens if a client sends data in text/plain instead of application/json?**

JAX-RS examines the Content-Type header before invoking the method. If the format does not match the @Consumes annotation, the method is never called and JAX-RS returns HTTP 415 Unsupported Media Type. This prevents malformed data from entering the system.

**Q6: Why is @QueryParam superior to path-based filtering like /sensors/type/CO2?**

Query parameters filter collections semantically, while path parameters identify specific resources. Query parameters are optional, order-independent, and composable. Path-based filtering would require complex routing and incorrectly suggest hierarchical relationships.


**Part 4: Deep Nesting with Sub-Resources**

**Q7: What are the benefits of the Sub-Resource Locator pattern?**

The pattern separates concerns by delegating nested paths to dedicated classes. Each class handles one responsibility, making code easier to understand, test, and maintain. It avoids monolithic controllers and the URL hierarchy mirrors the class structure.

**Q8: How is the currentValue field updated when a new reading is posted?**

The addReading method on the Sensor object both appends the reading to the history list and updates the currentValue field. This ensures data consistency within a single atomic operation. If the sensor is in MAINTENANCE status, the request is rejected with a 403 Forbidden response.


**Part 5: Error Handling and Logging**

**Q9: Why is HTTP 422 more accurate than 404 for missing references?**

HTTP 422 indicates the server understands the request syntax but cannot process the semantic instructions. When a client references a non-existent roomId, the endpoint exists and the JSON is valid, so 404 would be misleading. 422 accurately communicates that the referenced entity does not exist.

**Q10: What are the security risks of exposing Java stack traces?**

Stack traces reveal package structures, library versions, file paths, and method names. Attackers can use this information to identify vulnerable libraries and map application architecture. My GlobalExceptionMapper logs stack traces server-side but returns only a sanitised error response to clients.

**Q11: Why use JAX-RS filters for logging instead of manual Logger.info() calls?**

Filters centralise logging logic in one location, eliminating code duplication. Changes to logging format require modifying only one class. Filters execute automatically for every request, ensuring comprehensive coverage without relying on developers to remember adding logging statements.


**Features Implemented**

- CRUD operations for Room and Sensor entities
- Nested sub-resource pattern for Sensor Readings
- Query parameter filtering
- Referential integrity validation
- Custom exception mappers (409, 422, 403)
- Global exception handler
- Request/response logging filter
- HATEOAS discovery endpoint
- Thread-safe data storage with ConcurrentHashMap


**Author**

Praveen Dulshan Wijesundara
Student ID: w2120636 / 20231294
Module: Client-Server Architectures (5COSC022W)
University of Westminster / IIT

GitHub: https://github.com/Dulshan9/smart-campus-api-2026