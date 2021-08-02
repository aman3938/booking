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

If booking doesn't exist then not found header will be returned.

<pre>
HTTP/1.1 404 
Connection: keep-alive
Content-Length: 0
Date: Mon, 02 Aug 2021 07:37:16 GMT
Keep-Alive: timeout=60
</pre>

### Delete booking

	curl -X DELETE http://localhost:8938/booking/1
	
### Update booking

	curl -X PUT 'http://localhost:8938/booking/1' --header 'Content-Type: application/json' --data-raw '{"email": "patrick@gmail.com","firstName":"spongebob", "lastName": "squarepants","fromDate": "2021-08-22", "toDate": "2021-08-24" }'

