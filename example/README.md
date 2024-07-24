# Setup & Start Axon Server
1. Download Axon Server as an executable jar file from https://www.axoniq.io/download
2. Find the `axonserver.properties` file and add 2 properties:
  ```
  axoniq.axonserver.devmode.enabled=true
  axoniq.axonserver.standalone=true
  ```
3. Run `java -jar axonserver.jar`
4. The UI can be accessed from http://localhost:8024

# Start Application
1. Start the `RentalApplication` spring boot application.
2. Available endpoints are `POST /bikes`, `GET /bikes` and `GET /bikes/{id}`. See `RentalController` for parameters and more details.