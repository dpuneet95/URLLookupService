# URL Lookup Service to authenticate URLs

## Pre-requisites - 
1. Java version - JDK17 
2. Maven version - 3.1.0
3. IDE - Any, Intellij(used for development)

## Problem Statement: 
To build an API which when given a get request in the following format - /v1/urlinfo/{resource_url_with_query_string} - returns success if the URL is not present in the database. Else it will return forbidden.
Also, the service doesn't accept urls with reserved characters. If a request is sent containing the same, file not found error is thrown.

## FrameWork:
A spring-boot MVC application is created to build the API. 

### Model: 
#### URL: 
Currently, we only have the URL string in this class. The same entity is also used for storing urls in the database.
#### Response: 
This class contains the Response object containing the status code and message.

### Controller: 
#### AuthNController: 
This controller has the get request mapping for the api. We read the url sent from the user, and check if its present in the H2(local, in-memory) database. If yes, it's a malware and hence forbidden response is sent. Else a success response is sent.

### Repository:
#### URLRepository:
We do not have a service class as currently we do not have any business logic apart from querying the database.
The URLRepository extends the JPARepository and is responsible for executing the database queries.

## Tested Scenarios:
1. Valid URL present in database/list - Forbidden status is sent in response.
2. Valid URL not present in database - Success status is sent in response.
3. Invalid URL: An invalid URL is a URL containing reserved characters, eg: /, response for the same is file not found.

### Assumption:
We are currently assuming all the forbidden urls (malware) are added to the database.


### Additional Questions asked in the exercise:

#### Question 1:
If the size of the list increases infinitely, we can store data outside system in a different storage - RDMS database to ensure ACID properties.
we can introduce horizontal partitioning based on hash keys of the url strings, and store the data in a distributed horizontally partitioned databases.

#### Question 2:
If the number of requests exceed the capacity our system, we should scale our system and create a distributed system along with distributed storage. 
We can add load balancers and introduce circular hashing to ensure equal load. Also if the load increases/decreases in future we can add or deleted nodes as needed.

#### Question 3: 
If the list is being updated every 10 minutes with additional records(min 5k), we can have two separate lists, one old list and the other new list containing the additional records. 
So we have a separate job writing the new records to the new list. Now when a request comes in, we check its time, if the time is greater than the list sent for update, we will check the old list, and wait for the new list to populate, once the new list is populated we'll check for the url is this new list. Since the number of records in the new list is way lesser than the number of old records, search would be faster ensuring less latency.
Also, once the new list is populated, we can have another job, that will merge the old and the new list in the background, once that's done, we can switch old list to now point to the merged list. 