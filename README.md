# Smart Campus API

## Run
mvn clean install
deploy WAR in Tomcat

## Base URL
http://localhost:8080/smart-campus-api/api/v1

## Sample CURL

### Create Room
curl -X POST http://localhost:8080/api/v1/rooms \
-H "Content-Type: application/json" \
-d '{"id":"R1","name":"Lab","capacity":50}'

### Get Rooms
curl http://localhost:8080/api/v1/rooms

### Create Sensor
curl -X POST http://localhost:8080/api/v1/sensors \
-H "Content-Type: application/json" \
-d '{"id":"S1","type":"CO2","status":"ACTIVE","roomId":"R1"}'

### Filter Sensors
curl http://localhost:8080/api/v1/sensors?type=CO2

### Add Reading
curl -X POST http://localhost:8080/api/v1/sensors/S1/readings \
-H "Content-Type: application/json" \
-d '{"id":"R1","timestamp":123456789,"value":30}'
