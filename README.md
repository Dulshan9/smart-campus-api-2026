markdown
# Smart Campus Sensor & Room Management API

## Project Information

| | |
|---|---|
| Student Name | Praveen Dulshan Wijesundara |
| Student ID | w2120636 / 20231294 |
| Module | Client-Server Architectures (5COSC022W) |
| University | University of Westminster / IIT |
| Module Leader | Hamed Hamzeh |
| Submission Date | 24th April 2026 |

---

## Quick Start Guide

### Prerequisites
- Java Development Kit 21 or higher
- Apache Maven 3.8 or higher
- Apache Tomcat 9.0

### Build and Deploy

```bash
git clone https://github.com/Dulshan9/smart-campus-api-2026.git
cd smart-campus-api-2026
mvn clean package
copy target\smart-campus-api.war "C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps\"
net start Tomcat9
Access the API at: http://localhost:8080/smart-campus-api/api/v1

API Endpoints
Method	URL	Description
GET	/api/v1	Discovery endpoint
GET	/api/v1/rooms	List all rooms
POST	/api/v1/rooms	Create a room
GET	/api/v1/rooms/{id}	Get room by ID
DELETE	/api/v1/rooms/{id}	Delete room
GET	/api/v1/sensors	List all sensors
GET	/api/v1/sensors?type={type}	Filter sensors
POST	/api/v1/sensors	Create sensor
GET	/api/v1/sensors/{id}/readings	Get readings
POST	/api/v1/sensors/{id}/readings	Add reading
Sample Requests
bash
# Discovery
curl -X GET http://localhost:8080/smart-campus-api/api/v1

# Create room
curl -X POST http://localhost:8080/smart-campus-api/api/v1/rooms -H "Content-Type: application/json" -d '{"roomId":"R105","name":"Lab","roomType":"LAB","capacity":35,"floor":3,"location":"Cavendish"}'

# Create sensor
curl -X POST http://localhost:8080/smart-campus-api/api/v1/sensors -H "Content-Type: application/json" -d '{"sensorId":"S010","type":"TEMPERATURE","status":"ACTIVE","roomId":"R001"}'

# Filter sensors
curl -X GET "http://localhost:8080/smart-campus-api/api/v1/sensors?type=CO2"

# Add reading
curl -X POST http://localhost:8080/smart-campus-api/api/v1/sensors/S001/readings -H "Content-Type: application/json" -d '{"value":24.5}'
Theoretical Questions and Answers
Part 1: Service Architecture and Setup
Q1: Explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures to prevent data loss or race conditions.

JAX-RS resource classes are request-scoped by default. A new instance is created for each HTTP request and destroyed after the request completes. This means instance variables cannot persist data across requests. To maintain application state, I implemented a Singleton DataStore class using double-checked locking. The shared data structures use ConcurrentHashMap to ensure thread safety when multiple concurrent requests access the data, preventing race conditions and data corruption.

Q2: Why is the provision of Hypermedia (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?

HATEOAS allows clients to navigate the API dynamically through links provided in responses rather than hardcoded URLs. This provides discoverability, loose coupling between client and server, and self-documentation. The server can change URL structures without breaking clients. My implementation includes a discovery endpoint at /api/v1 that returns links to available resources.

Part 2: Room Management
Q3: When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client-side processing.

Returning only IDs minimises bandwidth but forces clients to make additional requests for each room's details, creating the N+1 query problem. Returning full objects consumes more bandwidth initially but provides all data in a single request, simplifying client logic and improving user experience. I chose to return full objects by default.

Q4: Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times.

Yes, DELETE is idempotent. The first request removes the room and returns 204. The second request finds the room already deleted and returns 422. The server state remains the same after both requests. Idempotency concerns server state, not response codes. If the room has sensors, all requests return 409 and the room remains unchanged.

Part 3: Sensor Operations and Linking
Q5: We explicitly use the @Consumes(MediaType.APPLICATION_JSON) annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as text/plain or application/xml. How does JAX-RS handle this mismatch?

JAX-RS examines the Content-Type header before invoking the resource method. If the format does not match @Consumes, the method is never called. JAX-RS automatically returns HTTP 415 Unsupported Media Type. This prevents malformed data from entering the system and protects against content-type confusion attacks.

Q6: You implemented this filtering using @QueryParam. Contrast this with an alternative design where the type is part of the URL path, such as /api/v1/sensors/type/CO2. Why is the query parameter approach generally considered superior for filtering and searching collections?

Query parameters filter collections semantically, while path parameters identify specific resources. Query parameters are optional, order-independent, and composable. Multiple filters combine naturally. Path-based filtering would require complex routing and incorrectly suggest hierarchical relationships.

Part 4: Deep Nesting with Sub-Resources
Q7: Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path in one massive controller class?

The pattern separates concerns by delegating nested paths to dedicated classes. Each class handles one responsibility, making code easier to understand, test, and maintain. It avoids monolithic controllers with hundreds of lines of code. The URL hierarchy mirrors the class structure, making the codebase intuitive.

Q8: A successful POST to a reading must trigger an update to the currentValue field on the corresponding parent Sensor object. How is this side effect implemented?

The addReading method on the Sensor object both appends the reading to the history list and updates the currentValue field. This ensures data consistency within a single atomic operation. If the sensor is in MAINTENANCE status, the request is rejected with a 403 Forbidden response.

Part 5: Advanced Error Handling, Exception Mapping and Logging
Q9: Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?

HTTP 422 indicates the server understands the request syntax but cannot process the semantic instructions. When a client references a non-existent roomId, the endpoint exists and the JSON is valid, so 404 would be misleading. 422 accurately communicates that the referenced entity does not exist.

Q10: From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?

Stack traces reveal package structures, library versions, file system paths, and method names. Attackers can use this information to identify vulnerable libraries, map application architecture, and craft targeted exploits. My GlobalExceptionMapper logs stack traces server-side but returns only a sanitised generic error response to clients.

Q11: Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting Logger.info() statements inside every single resource method?

Filters centralise logging logic in one location, eliminating code duplication across resource methods. Changes to logging format require modification in only one class. Filters execute automatically for every request, ensuring comprehensive coverage without relying on developers to remember adding logging statements. This separates infrastructure concerns from business logic.

Features Implemented
CRUD operations for Room and Sensor entities

Nested sub-resource pattern for Sensor Readings

Query parameter filtering

Referential integrity validation

Custom exception mappers (409, 422, 403)

Global exception handler

Request/response logging filter

HATEOAS discovery endpoint

Thread-safe data storage with ConcurrentHashMap

Author
Praveen Dulshan Wijesundara
Student ID: w2120636 / 20231294
Module: Client-Server Architectures (5COSC022W)

GitHub Repository: https://github.com/Dulshan9/smart-campus-api-2026