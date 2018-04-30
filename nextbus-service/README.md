# Nextbus

How to start the Nextbus application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/nextbus-service-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`

Running
---

Make sure you have successfully built the project.  Then run `service.sh` in the root directory.  Once the service is running, you can us `curl` to test the REST endpoint like this:

    % curl "localhost:8080/api/nextbus/v1/nextbus/METRO%20Blue%20Line/Target%20Field%20Station%20Platform%201/south"

The format of the URL is `/api/nextbus/v1/nextbus/{Route}/{Stop}/{Direction}`
