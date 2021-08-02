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

### Availability


	curl GET 'http://localhost:8938/availability/'


<pre>
HTTP/1.1 200 
Connection: keep-alive
Content-Type: application/json
Date: Mon, 02 Aug 2021 07:51:45 GMT
Keep-Alive: timeout=60
Transfer-Encoding: chunked

[
    "2021-08-02",
    "2021-08-03",
    "2021-08-04",
    "2021-08-05",
    "2021-08-06",
    "2021-08-07",
    "2021-08-08",
    "2021-08-09",
    "2021-08-10",
    "2021-08-11",
    "2021-08-12",
    "2021-08-13",
    "2021-08-14",
    "2021-08-15",
    "2021-08-16",
    "2021-08-17",
    "2021-08-18",
    "2021-08-19",
    "2021-08-20",
    "2021-08-21",
    "2021-08-22",
    "2021-08-23",
    "2021-08-24",
    "2021-08-25",
    "2021-08-26",
    "2021-08-27",
    "2021-08-28",
    "2021-08-29",
    "2021-08-30",
    "2021-08-31"
]

</pre>