# Smart Campus Sensor & Room Management API

## Project Information

| | |
|---|---|
| Student Name | Praveen Dulshan Wijesundara |
| Student ID | w2120636 | 20231294 |
| Module | Client-Server Architectures (5COSC022W) |
| University | University of Westminster / IIT |
| Module Leader | Hamed Hamzeh |
| Submission Date | 24th April 2026 |

---

## Project Overview

This project implements a RESTful API for a Smart Campus management system. The application is built using Java 21 with JAX-RS (Jersey 2.41) and deployed on Apache Tomcat 9. The API provides endpoints for managing rooms, sensors, and sensor readings within a university campus environment.

The system models an IoT infrastructure where various sensors are deployed across campus rooms to monitor environmental conditions. The API demonstrates core REST principles including proper resource modeling, nested resources, validation, filtering, and comprehensive error handling.

---

## Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 21 | Runtime environment |
| JAX-RS (Jersey) | 2.41 | REST framework |
| Jackson | 2.16.1 | JSON processing |
| Apache Tomcat | 9.0 | Servlet container |
| Maven | 3.8+ | Build management |

---

## Quick Start Guide

### Prerequisites
- Java Development Kit 21 or higher
- Apache Maven 3.8 or higher
- Apache Tomcat 9.0

### Build and Deploy Instructions

First, clone the repository to your local machine:
```bash
git clone https://github.com/Dulshan9/smart-campus-api-2026.git
cd smart-campus-api-2026
Build the WAR file using Maven:

bash
mvn clean package
This command generates a file named smart-campus-api.war in the target directory.

Deploy the WAR file to Tomcat by copying it to the webapps folder:

bash
copy target\smart-campus-api.war "C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps\"
Start the Tomcat service (run Command Prompt as Administrator):

bash
net start Tomcat9
Wait approximately ten seconds for the application to deploy, then verify by opening a browser and navigating to:

text
http://localhost:8080/smart-campus-api/api/v1
You should see a JSON response containing API metadata and available resource links.

API Endpoints
Discovery
Method	URL	Description
GET	/api/v1	API metadata with HATEOAS links
Room Management
Method	URL	Description
GET	/api/v1/rooms	Retrieve all rooms
POST	/api/v1/rooms	Create a new room
GET	/api/v1/rooms/{id}	Retrieve a specific room
PUT	/api/v1/rooms/{id}	Update room details
DELETE	/api/v1/rooms/{id}	Delete a room (fails if sensors present)
Sensor Management
Method	URL	Description
GET	/api/v1/sensors	Retrieve all sensors
GET	/api/v1/sensors?type={type}	Filter sensors by type
POST	/api/v1/sensors	Register a new sensor
GET	/api/v1/sensors/{id}	Retrieve a specific sensor
PUT	/api/v1/sensors/{id}	Update sensor details
DELETE	/api/v1/sensors/{id}	Delete a sensor
Sensor Readings (Sub-Resource)
Method	URL	Description
GET	/api/v1/sensors/{id}/readings	Retrieve reading history
POST	/api/v1/sensors/{id}/readings	Add a new reading
GET	/api/v1/sensors/{id}/readings/{rid}	Retrieve a specific reading
DELETE	/api/v1/sensors/{id}/readings/{rid}	Delete a reading
Sample Requests
Discovery Endpoint
bash
curl -X GET http://localhost:8080/smart-campus-api/api/v1
Create a Room
bash
curl -X POST http://localhost:8080/smart-campus-api/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{
    "roomId": "R105",
    "name": "Advanced Computing Lab",
    "roomType": "LAB",
    "capacity": 35,
    "floor": 3,
    "location": "Cavendish Campus"
  }'
Retrieve All Rooms
bash
curl -X GET http://localhost:8080/smart-campus-api/api/v1/rooms
Create a Sensor
bash
curl -X POST http://localhost:8080/smart-campus-api/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{
    "sensorId": "S010",
    "type": "TEMPERATURE",
    "status": "ACTIVE",
    "roomId": "R001"
  }'
Filter Sensors by Type
bash
curl -X GET "http://localhost:8080/smart-campus-api/api/v1/sensors?type=CO2"
Add a Sensor Reading
bash
curl -X POST http://localhost:8080/smart-campus-api/api/v1/sensors/S001/readings \
  -H "Content-Type: application/json" \
  -d '{"value": 24.5}'
Delete a Room (Conflict Example)
bash
curl -X DELETE http://localhost:8080/smart-campus-api/api/v1/rooms/R001
Error Response Examples
409 Conflict (Delete Room with Sensors)
json
{
  "status": 409,
  "error": "Conflict",
  "message": "Cannot delete room - it contains active sensors",
  "roomId": "R001",
  "sensorCount": 3,
  "resolution": "Remove or reassign all sensors from this room before deletion"
}
422 Unprocessable Entity (Invalid Room Reference)
json
{
  "status": 422,
  "error": "Unprocessable Entity",
  "message": "Room with ID 'INVALID' not found",
  "resourceType": "Room",
  "resourceId": "INVALID",
  "resolution": "Ensure the room exists before referencing it"
}
403 Forbidden (Sensor in Maintenance)
json
{
  "status": 403,
  "error": "Forbidden",
  "message": "Sensor is not available for readings",
  "sensorId": "S005",
  "currentStatus": "MAINTENANCE",
  "resolution": "Change sensor status to ACTIVE before submitting readings"
}
Theoretical Questions and Answers
Part 1: Service Architecture and Setup
Question 1: Explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures to prevent data loss or race conditions.

JAX-RS resource classes follow a request-scoped lifecycle by default. This means the runtime creates a new instance of the resource class for each incoming HTTP request. Once the request processing completes, that instance becomes eligible for garbage collection.

This design has significant implications for state management. Any data stored in instance variables would be lost between requests since each request receives a fresh instance. To maintain persistent application state across requests, I implemented a separate DataStore class using the Singleton pattern with double-checked locking. This ensures exactly one instance exists for the entire application lifetime.

Since multiple requests can arrive concurrently on different threads, the shared data structures must be thread-safe. I used ConcurrentHashMap for all collections rather than regular HashMap. ConcurrentHashMap provides atomic operations and proper synchronization, preventing race conditions where simultaneous read and write operations could corrupt the data or cause inconsistent states.

Question 2: Why is the provision of Hypermedia (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?

HATEOAS, which stands for Hypermedia as the Engine of Application State, represents the highest maturity level in the Richardson Maturity Model for REST APIs. The core principle is that API responses include links to related resources, allowing clients to navigate the API dynamically without prior knowledge of URL structures.

The primary benefits for client developers include discoverability and loose coupling. When a client receives a response containing resource links, they can follow those links to discover available actions rather than constructing URLs from hardcoded strings. This means the server can change its URL structure without breaking existing clients, as long as the link relations remain consistent.

Additional advantages include self-documentation, where the API response itself serves as documentation of available operations. The server can also conditionally include links based on the current state of the resource, guiding clients through valid workflows. This reduces the cognitive load on developers who no longer need to memorise endpoint patterns or constantly consult external documentation.

My implementation includes a discovery endpoint at /api/v1 that returns links to the primary resource collections, demonstrating this HATEOAS principle.

Part 2: Room Management
Question 3: When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client-side processing.

The choice between returning only identifiers versus complete object representations involves trade-offs between bandwidth consumption and client convenience.

Returning only IDs minimises the payload size significantly, which reduces network bandwidth usage and improves response times for the initial request. However, this approach forces the client to issue additional requests for each room they need details about, creating the classic N+1 query problem. For a list of fifty rooms, the client would need to make fifty additional API calls to retrieve the complete information, increasing total latency and server load.

Returning full room objects consumes more bandwidth in the initial response but provides all necessary data in a single round trip. This simplifies client logic considerably as they can render the complete list immediately without orchestrating multiple follow-up requests. The user experience improves due to reduced overall loading time despite the larger initial payload.

In my implementation, I chose to return full room objects by default. Given the expected scale of campus room data and typical client requirements, the simplicity and performance benefits of single-request retrieval outweigh the bandwidth considerations.

Question 4: Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times.

Yes, the DELETE operation in my implementation is idempotent according to the HTTP specification defined in RFC 7231. Idempotency means that multiple identical requests have the same effect on server state as a single request.

Consider a client sending the same DELETE request for a room multiple times. For the first request, assuming the room exists and contains no sensors, the server removes the room from the data store and returns a 204 No Content status. The room no longer exists in the system.

When the second identical DELETE request arrives, the server attempts to locate the room but finds it has already been removed. The server throws a ResourceNotFoundException, which is mapped to a 422 Unprocessable Entity response. The important observation is that the server state after the second request remains identical to the state after the first request. The room is still deleted.

If the room had active sensors, the first DELETE request would be rejected with a 409 Conflict status. Subsequent identical requests would also receive 409 Conflict responses, and the room would persist in the data store unchanged. Again, the server state remains consistent across multiple requests.

It is worth noting that idempotency concerns server state, not response codes. Different response codes for subsequent requests do not violate idempotency as long as the underlying resource state remains the same.

Part 3: Sensor Operations and Linking
Question 5: We explicitly use the @Consumes(MediaType.APPLICATION_JSON) annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as text/plain or application/xml. How does JAX-RS handle this mismatch?

When a client sends a request with a Content-Type header that does not match the value specified in the @Consumes annotation, JAX-RS performs content negotiation before the resource method is invoked.

The JAX-RS runtime examines the incoming request's Content-Type header and compares it against the supported media types declared by @Consumes annotations on candidate resource methods. If no method declares support for the provided Content-Type, the framework does not invoke any resource method.

Instead, JAX-RS automatically generates and returns an HTTP 415 Unsupported Media Type response to the client. This occurs before any application code executes, meaning the request never reaches the resource method body.

This behaviour provides important security benefits. By strictly enforcing content types, the framework prevents scenarios where malformed or unexpected payload formats might bypass validation logic or cause unpredictable behaviour. It also ensures that message body readers are only invoked for formats they are designed to handle, preventing parsing errors that could expose internal system details.

Question 6: You implemented this filtering using @QueryParam. Contrast this with an alternative design where the type is part of the URL path, such as /api/v1/sensors/type/CO2. Why is the query parameter approach generally considered superior for filtering and searching collections?

Query parameters provide a more appropriate semantic model for filtering collections than path parameters. The fundamental distinction lies in what each mechanism represents within REST architecture.

A URL path identifies a specific resource or a hierarchical relationship between resources. For example, /api/v1/sensors/S001 identifies a single sensor resource. Using a path segment for filtering, such as /api/v1/sensors/type/CO2, incorrectly implies that "CO2" is a sub-resource of sensors rather than a characteristic used to filter the sensor collection.

Query parameters, in contrast, modify the representation of a resource collection without changing the resource being addressed. The URL /api/v1/sensors?type=CO2 clearly communicates that the client is requesting the sensors collection with a filter applied to limit results to those with type CO2.

Additional advantages of query parameters include composability and optionality. Multiple filters can be combined naturally using ampersands, such as ?type=CO2&status=ACTIVE. The order of query parameters does not matter, and clients can omit parameters they do not need. Achieving equivalent functionality with path parameters would require complex and brittle URL patterns with fixed ordering requirements.

Industry practice and REST conventions strongly favour query parameters for filtering, sorting, and pagination of collections.

Part 4: Deep Nesting with Sub-Resources
Question 7: Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path in one massive controller class?

The Sub-Resource Locator pattern addresses the challenge of managing nested resource hierarchies by delegating responsibility for different URL segments to separate classes. This approach provides several architectural advantages.

Separation of concerns is the primary benefit. Each sub-resource class handles only its specific context. For instance, SensorReadingResource deals exclusively with reading-related operations, while SensorResource manages sensor entities themselves. This division makes each class focused and easier to understand, test, and maintain.

Without this pattern, a single controller class would need to handle all operations across multiple nesting levels, resulting in methods with paths like @GET @Path("/{sensorId}/readings/{readingId}") alongside methods for the parent resources. Such a class would quickly become unwieldy, potentially reaching hundreds or thousands of lines of code.

The pattern also enables context reuse. The parent resource identifier, such as sensorId, is resolved once in the parent resource and passed to the sub-resource constructor. The sub-resource can then operate with full knowledge of its context without repeatedly extracting and validating the same path parameter.

Testing benefits significantly as well. Each sub-resource can be unit tested in isolation with mocked dependencies, whereas testing a monolithic controller requires complex setup to isolate specific path combinations.

Finally, the URL hierarchy naturally mirrors the class hierarchy. The structure /sensors/{id}/readings corresponds to a SensorResource class that locates a SensorReadingResource class, making the codebase intuitive to navigate.

Question 8: A successful POST to a reading must trigger an update to the currentValue field on the corresponding parent Sensor object. How is this side effect implemented?

When a client successfully posts a new reading to the endpoint /api/v1/sensors/{sensorId}/readings, the system performs two related updates atomically within the same request scope.

The SensorReadingResource receives the reading data and first validates that the sensor is in an operational state. If the sensor status is MAINTENANCE, the request is rejected with a 403 Forbidden response.

Assuming validation passes, the resource calls the addReading method on the parent Sensor object. This method serves two purposes. First, it appends the new reading to the sensor's readings list, maintaining the historical record. Second, it updates the sensor's currentValue field with the value from the new reading.

This design ensures data consistency. The sensor always reflects its most recent reading without requiring a separate API call or background process. The operation is atomic from the client's perspective as both updates occur within the same HTTP request.

The implementation also handles missing fields appropriately. If the client does not provide a readingId, one is generated automatically. If no timestamp is provided, the current system time is used.

Part 5: Advanced Error Handling, Exception Mapping and Logging
Question 9: Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?

HTTP 422 Unprocessable Entity, defined in RFC 4918, provides more precise semantics for validation failures than HTTP 404 Not Found.

Consider a client request to create a sensor with a roomId that does not exist in the system. The request is sent to the endpoint /api/v1/sensors with a syntactically valid JSON payload. A 404 response would be inappropriate because the endpoint itself exists and is capable of processing requests. Returning 404 would incorrectly suggest that the client has requested a non-existent URL.

HTTP 400 Bad Request is also insufficiently specific. While technically correct that the client's request contains an error, 400 is a broad category that encompasses malformed syntax, missing required fields, and various other issues.

HTTP 422 Unprocessable Entity specifically indicates that the server understands the content type and the request syntax is correct, but the semantic instructions contained within the payload cannot be processed. This precisely describes the situation where a client references a non-existent room. The server understands what the client wants to do, recognises that the JSON is valid, but cannot fulfil the request because the referenced entity does not exist.

My implementation maps ResourceNotFoundException to 422 for exactly this reason. When a client attempts to reference a room that does not exist, they receive a 422 response with a clear message explaining which resource type and identifier could not be found.

Question 10: From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?

Exposing stack traces to API consumers creates significant information disclosure vulnerabilities that attackers can leverage to map and exploit the system.

Stack traces reveal the internal package structure of the application. An attacker can see class names like com.smartcampus.resource.SensorResource and understand the application's architectural organisation. This knowledge helps them identify potential areas of interest for further probing.

Library version information is often embedded in stack traces through package names or class references. Knowing that an application uses Jersey 2.41 or Jackson 2.16.1 allows an attacker to check for known vulnerabilities in those specific versions and craft targeted exploits.

File system paths appear in stack traces when exceptions originate from file operations. These paths reveal the server's directory structure and operating system conventions, providing information useful for path traversal attacks.

Method names and line numbers expose details about business logic flow. An attacker can infer validation sequences, database operations, and other internal processes by analysing the call chain.

My implementation prevents this information disclosure through a GlobalExceptionMapper that catches all unhandled exceptions. The mapper logs the full stack trace server-side for debugging purposes but returns only a sanitised response to the client containing a generic error message and a reference ID.

Question 11: Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting Logger.info() statements inside every single resource method?

Using JAX-RS filters for logging provides a cleaner and more maintainable approach compared to scattering logging statements throughout resource methods.

Filters centralise the logging logic in a single location. My LoggingFilter class implements both ContainerRequestFilter and ContainerResponseFilter, containing all the code needed to log incoming requests and outgoing responses. When logging requirements change, such as adding additional fields or changing the format, only one class needs modification.

Manual logging statements create code duplication across dozens of resource methods. Each method would need similar boilerplate code to log the request and response, violating the DRY principle. This duplication increases maintenance burden and the risk of inconsistent logging formats.

Filters execute automatically for every request without requiring developers to remember to add logging statements to new endpoints. There is no risk that a developer forgets to include logging in a resource method, ensuring comprehensive coverage.

Performance metrics become easier to implement with filters. The request filter can store a start timestamp in the request context, and the response filter can calculate the duration. Implementing equivalent functionality manually would require passing timing information through method parameters or thread-local storage.

Finally, filters separate infrastructure concerns from business logic. Resource methods focus on their primary responsibility of handling the domain operations, while filters handle orthogonal concerns like logging, authentication, and compression.

Project Structure
text
smart-campus-api/
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/
        │   └── com/smartcampus/
        │       ├── config/
        │       │   └── SmartCampusApplication.java
        │       ├── model/
        │       │   ├── Room.java
        │       │   ├── Sensor.java
        │       │   └── SensorReading.java
        │       ├── store/
        │       │   └── DataStore.java
        │       ├── exception/
        │       │   ├── RoomNotEmptyException.java
        │       │   ├── ResourceNotFoundException.java
        │       │   └── SensorUnavailableException.java
        │       ├── mapper/
        │       │   ├── RoomNotEmptyExceptionMapper.java
        │       │   ├── ResourceNotFoundExceptionMapper.java
        │       │   ├── SensorUnavailableExceptionMapper.java
        │       │   └── GlobalExceptionMapper.java
        │       ├── filter/
        │       │   └── LoggingFilter.java
        │       └── resource/
        │           ├── DiscoveryResource.java
        │           ├── RoomResource.java
        │           ├── SensorResource.java
        │           └── SensorReadingResource.java
        └── webapp/
            └── WEB-INF/
                └── web.xml
Features Implemented
Complete CRUD operations for Room entities

Complete CRUD operations for Sensor entities

Nested sub-resource pattern for Sensor Readings

Query parameter filtering for sensor type

Referential integrity validation between sensors and rooms

Custom exception mappers for specific error scenarios

Global exception handler for unhandled exceptions

Request and response logging filter

HATEOAS discovery endpoint

Thread safe in memory data storage using ConcurrentHashMap

Deployed as WAR on Apache Tomcat 9

Author
Praveen Dulshan Wijesundara
Student ID: w2120636 | 20231294
Module: Client-Server Architectures (5COSC022W)
University of Westminster / IIT

GitHub Repository: https://github.com/Dulshan9/smart-campus-api-2026