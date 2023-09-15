# Subscription Tracker Backend
### Overview
This application is a stateless subscription service with ability to store data in a in-memory DB which allows to
create, get and delete subscription objects.

This backend service registers and stores information about newsletter subscribers via API endpoints.
It accepts information via body of JSON and returns response in the same format. For detailed specification see below.
This service implemented following the MVC pattern and has a controller, service and data persistence layers.

Open API documentation can be viewed via [Swagger UI](http://localhost:8080/swagger-ui/index.html)
after application start.

### System requirements
The application is containerized with all dependencies being obtained on start.

In order to run application Docker is required.

_Note:_ For Mac OS, once the repository content is cloned, make sure that the folder with the application
is exposed to Docker via File Share to ensure the image can be mounted.

### How to run
To run the application execute from the application folder: ```docker-compose up```

The application will run on http://localhost:8080.

### How to check app build

```./mvnw clean install```

### Development requirements
- Java 17
- Spring Boot 3.0.0^
- Maven

## Notes on implementation
### Business logic
For easy testing DB is prefilled with test data [data.sql](./src/main/resources/data.sql).
Input dates format: `dd.MM.yyyy`, could be changed in [application.yml](./src/main/resources/application.yml)

Due to time limitation, I was not able to cover all edge-cases I noticed while writing the application.

Here are notes on such edge-cases:
- **DB**: In-memory DB: We don't persist DB state between application launches
- **General**: existing subscriptions can't be edited
- **Validation**: no email validation

### Technical implementation
Due to time constraints I was not able to implement the following:
- More Unit tests
- Integration tests for repository
- Validation service for data and emails checking, check that before date < after date
- Support of a list of errors for InvalidDateException
- Jacoco and Sonar support for static code analysis
- Add to controller an ability to update subscribers
- An ability to persist DB state between docker runs: e.g. H2 persist to file in shared folder
- Production profile with external DB support inside the same docker
- Add description annotations for controller methods to make API specification more descriptive.