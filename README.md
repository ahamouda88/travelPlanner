# TravelPlanner
This simple web-site is used for planning your travels, by adding/updating/deleting your trip information (destination, start date, end date, ..etc), and you will be able to filter your trips as well.

## Implementation:
- This project is implemented using AngularJS and Bootstrap as the front-end technology, while for the back-end and the restful API are implemented using Java 8, Spring Boot, and Spring MVC. 
- Maven is used as the building tool. 
- For unit and integration testing, JUnit, and Mockito frameworks are used. 
- The database being used is MySQL.

## Running the application:
- Must have MYSQL database, and a schema named 'travelPlannerDb'
- Database username, and password for now are root, and no password
- For schema, username, password or database config changes, you can update the application.properties under src/main/resources
- Then build the application using Maven using the following command: mvn clean install
- Then simply you can either run the application by executing SpringBootConfig.java in the com.travelPlanner.config package or using the following command: mvn spring-boot:run

## Pages End Points 
|              URI                   |                               Description                               |   Method   |
|------------------------------------|-------------------------------------------------------------------------|------------|
| /                                  | Home/Welcome Page                                                       |     GET    |
| /login                             | Navigates to the Login Page                                             |     GET    |
| /user                              | Navigates to the Sign-up Page, where you can add a user                 |     GET    |
| /users                             | Navigates to all Users Page, where you can manage all users         	   |     GET    |
| /trip                              | Navigates to the Trip Page, where you can add a Trip                    |     GET    |
| /trips                             | Navigates to all Trips Page, where you can manage all trips             |     GET    |


## RESTful Web Service
### Users End Points:
|              URI                   |                  Description                     		              |    Method   |
|------------------------------------|------------------------------------------------------------------------|-------------|
| /api/users                         | Returns the list of all users           								  |     GET     |
| /api/users                         | Adds a new user to the database, given a user object                   |     POST    |
| /api/users                         | Updates a user, given a user object                                    |     PUT     |
| /api/users?id={id}                 | Deletes a user, given a user's id       								  |    DELETE   |
| /api/users/{id}                    | Returns a user by user's id            							      |     GET     |
| /api/users/username/{username}     | Returns a user by user's username                                      |     GET     | 
| /api/users/currentuser             | Returns current authenticated user                                     |     GET     |

### Body Request Examples:
#### Add User
```json
{
  	"firstName": "Ahmed",
  	"lastName": "Hamouda",
  	"username": "ahmed",
  	"password": "a",
  	"confirmPassword": "a",
  	"role": "ADMIN"
}
```
#### Update User's Last Name
```json
{
	"id": "9",
	"firstName": "Ahmed",
  	"lastName": "updatedField",
  	"username": "ahmed",
  	"password": "a",
  	"confirmPassword": "a",
  	"role": "ADMIN"
}
```

### Trips End Points:
|              URI                   |                  Description                     					  |    Method   |
|------------------------------------|------------------------------------------------------------------------|-------------|
| /api/trips                         | Returns the list of all trips                                          |     GET     |
| /api/trips                         | Adds a new trip to the database given a trip object                    |     POST    |
| /api/trips                         | Updates a trip given a trip object                                     |     PUT     |
| /api/trips?id={id}                 | Deletes a trip, given the trip's id                                    |    DELETE   |
| /api/trips/{id}                    | Returns a trip by id                                                   |     GET     |
| /api/trips/countries               | Returns a list of countries, currently reading from a file             |     GET     |

### Body Request Examples:
#### Add Trip
```json
{
    "destination": "Anguilla", 
    "startDate": "2017-07-01", 
    "endDate": "2017-08-15",
    "comment": "No Comment",
    "user" : {
        "id": "9",
        "firstName": "Ahmed",
        "lastName": "Hamouda",
        "username": "ahmed",
        "password": "a",
        "confirmPassword": "a",
        "role": "ADMIN"
    }
}
```
#### Update Trip's Destination
```json
{
    "id": 9,
    "destination": "Egypt",
    "startDate": "2017-07-01",
    "endDate": "2017-08-15",
    "comment": "No Comment",
    "user": {
        "id": 9,
        "firstName": "Ahmed",
        "lastName": "Hamouda",
        "username": "ahmed",
        "password": "a",
        "role": "ADMIN"
    }
}
```

#### Trips Search End Point
* URI: /api/trips/search?destination={destination}&startDate={startdate}&endDate={enddate}&user={userid}
* Description: Filters trips based on to the given destination, start date, end date, and user's Id
* Request Params: (destination, startdate, enddate, userid) all params are OPTIONAL
* Method: GET

> Note: Date provided should be in the following format: MM-dd-yyyy. For example: 08-21-2017