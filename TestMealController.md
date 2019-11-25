#Curl commands to test MealRestController

## getAll()
* curl -i localhost:8080/topjava/rest/meals

## get()
* curl -i localhost:8080/topjava/rest/meals/100003

## getBetween()
* curl -i "localhost:8080/topjava/rest/meals/filter?startDate=2015-05-30&endDate=2015-05-30&endTime=14:00"

## delete()
* curl -i -X DELETE localhost:8080/topjava/rest/meals/100003

## create()
* curl -i -H 'Content-Type:application/json' -d '{"dateTime":"2016-06-01T18:00:00","description":"Созданный ужин","calories":300}' localhost:8080/topjava/rest/meals

## update()
* curl -i -H 'Content-Type:application/json' -X PUT -d '{"id":100002,"dateTime":"2015-05-30T10:00:00","description":"Обновленный заfffтрак","calories":200}' localhost:8080/topjava/rest/meals/100002