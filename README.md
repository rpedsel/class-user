# Educational Class-User API

A Spring Boot REST API

## Getting Started

These instructions will get you a copy of the project up and running on your local machine.

### Prerequisites

* Java Development Kit
* Maven


### Installing

Clone the project on GitHub:
```bash
git clone https://github.com/rpedsel/class-user.git
```
and build up the project

```bash
cd class-user
mvn install
```
To start the app,
```bash
cd rest

mvn spring-boot:run
# OR
java -jar target/rest-0.0.1-SNAPSHOT.jar
```
this will start the app on your [http://localhost:8080](http://localhost:8080)
### DEMO page
You can also find a working API from [this address](http://ec2-18-219-43-8.us-east-2.compute.amazonaws.com:8080)

*P.S There is nothing on the root path above, try append **/user/all** to see a list of all users.*