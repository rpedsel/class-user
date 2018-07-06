# Educational Class-User API

A Spring Boot REST API

## Getting Started

These instructions will get you a copy of the project up and running on your local machine.

### Prerequisites

* Java Development Kit (I am using **Oracle Java 8**)
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

### Default, Predefined Objects 
There are several User and EClass Objects predefined in the application. Check them out with **GET /user/all**, **GET class/all**, and look up details of an object with **GET user/{userId}**, **GET /class/{classId}**.

### List of Requests
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
| **POST** | /user/{userId}/update | {"firstname": "Newfname", "lastname": "Newlname", "email": "newemail@example.com"}<br> *(at least one of the three)*| Update a student's first name, last name, and/or email |
| **PUT**  | /user/create | {"firstname": "Alice", "lastname": "Abe", "email": "alice@example.com"} <br> *(all of the three)* | Create a User |
| **PUT**  | /class/create/{userId} | {"classname": "Math"} | Create a Class, take User with {userId} as creator |

## Testing the Application

### Unit Tests
There are 11 [test cases](https://github.com/rpedsel/class-user/blob/master/rest/src/test/java/classusers/EClassRestControllerTest.java) run while building the project, to make sure that basic functionalities are working. 

### API Tester Result
Test result of 15 requests with API Tester: [Educational Class-User API Test Result](https://apitester.com/shared/runs/5ed4ad99300c41a69af8dbc7f5c1d4f8) 

(To see details of all steps, please find step selector on top-right of the page, *Viewing a Request Step*)

### Test with CURL
```bash
# GET
curl localhost:8080/user/all
```

```bash
# POST
curl -d "{\"classname\": \"New Name\"}" -H "Content-Type: application/json" -X POST http://localhost:8080/class/1/rename
```
```bash
# POST
curl -d "{\"classname\": \"New Class Name\"}" -H "Content-Type: application/json" -X PUT http://localhost:8080/class/create/1
```

### Time Complexity
M = # of record for User

N = # of record for EClass

| Type | Request       | Explanation | Time Complexity |
| ---- | ------------- | ----------- |---------------- |
| **GET**  | /user/{userId}/creator | Find eclass with creatorId = {userId} | O(N) |
| **GET**  | /user/{userId}/student | Join eclass_students & eclass table on studiedclassesId = eclassId, find those whose studentId = {userId} | O(MN) |
| **GET**  | /class/{classId}/students |1. Find eclass with {classId} <br> 2. Find all records in eclass_students where studiedclasses_id = {classId}| O(M) |
| **POST** | /class/{classId}/rename | 1. Find eclass with {classId} <br> 2. set new classname for the eclass | O(1) |
| **POST** | /class/{classId}/addstudent  | 1. Find eclass with {classId} <br> 2. Find user with {userId} <br> 3. Find students in eclass_students table that studies eclass with {classId} <br> 4. Insert new record into eclass_students | O(M) |
| **POST** | /user/{userId}/update | Update user with new value(s) | O(1) |
| **PUT**  | /user/create | Insert into user table | O(1) |
| **PUT**  | /class/create/{userId} | 1. Find user with {userId} <br> 2. Insert new record into eclass table | O(1) |

##### Debugging 
Add *logging.level.org.hibernate.SQL=debug* in model/target/classes/application.properties

##### Possible Improvement
[Hibernate Many-To-Many Revisited](https://josephmarques.wordpress.com/2010/02/22/many-to-many-revisited/)

## Dependencies ([Spring Initializr](https://start.spring.io))
* Web
* H2 Database
* JPA (Provider: Hibernate)