## App

> H2 console is at: http://localhost:8938/h2-console 
> user: sa
> password: password


### Booking campsite

	curl -X POST 'http://localhost:8938/booking' --header 'Content-Type: application/json' --data-raw '{"email": "spongebob@yahoo.com","firstName":"spongebob", "lastName": "squarepants","fromDate": "2021-08-22", "toDate": "2021-08-24" }'
	
> Response: 1
	
### Getting booking
  
	curl http://localhost:8938/booking/1

> Response: {"email":"spongebob@yahoo.com","lastName":"squarepants","firstName":"spongebob","fromDate":"2021-08-22","toDate":"2021-08-24","updatedAt":null}

### Delete booking

	curl -X DELETE http://localhost:8938/booking/1
