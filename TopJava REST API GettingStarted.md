#Getting Started

##Overview

This system provides you a capability to journal what and when you eat. It also helps you track the amount of calories 
you consume a day. 

Along with Web UI to manage records this system provides you a REST API to manage you records. It consist of the two main 
parts:
* User API - which is primarily requred for admins to manage users that have access to the system.
* Meals API - it gives you access to the individual user meal journals.

Please find below the usage examples:  

## Meal API

### Accessing the full list of meals in your journal: 

```
GET /rest/meals
```

*Example:*
* curl -i localhost:8080/topjava/rest/meals

### Accessing an individual meal record

```
GET /rest/meals/{id}
```

*Example:*
* curl -i localhost:8080/topjava/rest/meals/100003

### Accessing records for a given period of time 

```
GET /rest/meals/filter?startDate={yyyy-MM-dd}
                      &endDate={yyyy-MM-dd}
                      &startTime={hh:mm}
                      &endTime={hh:mm}
```

*Example:*
* curl -i "localhost:8080/topjava/rest/meals/filter?startDate=2015-05-30&endDate=2015-05-30&endTime=14:00"

### Removing a single record 

```
DELETE /rest/meals/{id}
```

*Example:*
* curl -i -X DELETE localhost:8080/topjava/rest/meals/100003

### Creating a new meal record

```
POST /rest/meals
```
where body is a JSON representation of the meal record

*Example:*
* curl -i -H 'Content-Type:application/json' -d '{"dateTime":"2016-06-01T18:00:00","description":"Созданный ужин","calories":300}' localhost:8080/topjava/rest/meals

### Updating a record

```
PUT /rest/meals/{id}
```
where body is a JSON representation of the meal record (all fields)

*Example:*
* curl -i -H 'Content-Type:application/json' -X PUT -d '{"id":100002,"dateTime":"2015-05-30T10:00:00","description":"Обновленный заfffтрак","calories":200}' localhost:8080/topjava/rest/meals/100002

## User API

### Accessing the full list of users: 

```
GET /rest/admin/users
```

*Example:*
* curl -i localhost:8080/topjava/rest/admin/users

### Accessing an individual user

```
GET /rest/admin/users/{id}
```

*Example:*
* curl -i localhost:8080/topjava/rest/admin/users/100001

### Finding a user by email 

```
GET /rest/admin/users/by?email={email}
```

*Example:*
* curl -i "localhost:8080/topjava/rest/admin/users/by?email=user@yandex.ru"

### Removing a single user 

```
DELETE /rest/admin/users/{id}
```

*Example:*
* curl -i -X DELETE localhost:8080/topjava/rest/admin/users/100001

### Creating a new user record

```
POST /rest/admin/users
```
where body is a JSON representation of the user record


*Example:*
* curl -i -H 'Content-Type:application/json' -d '{"name":"New","email":"new@gmail.com","password":"newPass","enabled":false,"registered":"2019-11-26T06:42:10.536+0000","roles":["ROLE_USER"],"caloriesPerDay":1555}' localhost:8080/topjava/rest/admin/users

### Updating a user record

```
PUT /rest/admin/users/{id}
```
where body is a JSON representation of the user record (all fields)

*Example:*
* curl -i -H 'Content-Type:application/json' -X PUT -d '{"id":100000,"name":"UpdatedName","email":"user@yandex.ru","password":"password","enabled":true,"registered":"2019-11-26T06:43:44.573+0000","roles":["ROLE_ADMIN"],"caloriesPerDay":330}' localhost:8080/topjava/rest/admin/users/100000


