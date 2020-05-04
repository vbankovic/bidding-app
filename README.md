# Bidding App
This is homework assignment app done as part of hiring process.
More details can be found in `homework_assignment.pdf` file.


## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. 


### Prerequisites

This app is using [OpenJDK 11.0.2](https://jdk.java.net/archive/) so please set this java version as `JAVA_HOME`.
If you need more JDK versions at same time please consider using of [SDKMAN](https://sdkman.io/).

For building and running app we are using [Gradle](https://gradle.org/). App has Gradle wrapper and
 it's not needed to install Gradle on your machine.
 
There is no need to set up database since app is using in memory database.
When app is running to access DB go to next url in your browser.
```
http://localhost:8080/db-console/

jdbc url: jdbc:h2:mem:bidderapp
platform: h2
username: sa
password:
```

### Run
To build app please execute next command from Terminal.
```
./gradlew build
```

To run app please execute next command from Terminal.
```
./gradlew bootRun
```

### Running the tests

To run integration tests please execute next command:
```
./gradlew test
```
For unit tests please run next:
```
./gradlew unitTest
``` 

### API Documentation

API documentation is created with [Spring REST docs](https://spring.io/projects/spring-restdocs).
Please follow next steps to generate `html` version of documentation.

To create necessary document snippets please run integration tests:
```
./gradlew test
```
and now run next command to generate `api-guide.html` file in `build/asciidoc/html5`.
```
./gradlew asciidoctor
```

### Using IDE

For using IntelliJ IDEA IDE for development simply import project as Gradle project.

This project is using [Lombok](https://projectlombok.org) library so you need to set it up in
IntelliJ IDEA. To do this please follow instructions from [here](https://projectlombok.org/setup/intellij).


### Coding style

This project is using [Google Style Guides](https://github.com/google/styleguide) for code style.

Please follow next [instructions](https://www.jetbrains.com/help/idea/settings-code-style.html) to set code style scheme in IntelliJ IDEA.
Please use [java](https://github.com/google/styleguide/blob/gh-pages/intellij-java-google-style.xml) scheme.

Before you contribute any line of code please make sure that code is properly formatted.

### Decisions I made
- I used Spring REST docs cause it's a standard way of generating API documentation.
- In memory H2 database is used because it's embedded and no additional configuration is needed. 
  This is demo project so there is no need for data persistence.
- I used Lombok to avoid writing getters and setters.
