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

>*There is nothing on the root path above, try append **/user/all** to see a list of all users.*

## API Documentation
 ##### Header Content-type: **application/json**

| Type | Request       | Post Data | Description    |
| ---- | ------------- |--- | -------------    |
| GET  | /user/all     || List all Users   |
| GET  | /class/all    || List all Classes |
| GET  | /user/{userId}|| Get the User with {userId} |
| GET  | /class/{classId}|| Get the class with {classId} |
| GET  | /user/search/firstname/{searchText}|| Search User with first name {searchText} |
| GET  | /user/search/lastname/{searchText}|| Search User with last name {searchText} |
| GET  | /user/search/email/{searchText}|| Search User with email {searchText} |
| GET  | /class/search/classname/{searchText}|| Search Class with class name {searchText} |
| **GET**  | /user/{userId}/creator || Get all classes that the user is a creator for |
| **GET**  | /user/{userId}/student || Get all classes that the user is a student for |
| **GET**  | /class/{classId}/students || Get all students user objects for the class |
| **POST** | /class/{classId}/rename | {"classname": "New Class Name"}| Update a class name |
| **POST** | /class/{classId}/addstudent | {"id": {userId}} | Add a student to the class |
| **POST** | /user/{userId}/update | {"firstname": "Newfname", "lastname": "Newlname", "email": "newemail@example.com"}<br> *(all of the three)*| Update a student's first name, last name, and/or email |
| **PUT**  | /user/create | {"firstname": "Alice", "lastname": "Abe", "email": "alice@example.com"} <br> *(at least one of the three)* | Create a User |
| **PUT**  | /class/create/{userId} | {"classname": "Math"} | Create a Class, take User with {userId} as creator |



