# Pet Assignment

Build a REST service that will store, retrieve, search, and delete images of dog breeds retrieved
from an external API.

The following public API can be used to retrieve a random dog breed picture:
https://dog.ceo/api/breeds/image/random

##Instructions
Build the following endpoints:

### Generate a new dog breed record
* When called, the endpoint will retrieve a dog breed picture from the above endpoint.
* The picture should be stored in an AWS S3 bucket.
* The following information relating to the image should be stored in a database:
* Name of the dog breed;
* Date that the image was uploaded;
* The S3 location of the uploaded image.

### Retrieve by id
* When called, the endpoint will retrieve the record from the database with the given
id.

### Remove by id
* When called, the endpoint will remove the record from the database with the given
id, and also remove the stored image from S3.

### Search by breed name
* When called, the endpoint will retrieve any records from the database with the given
dog breed name.

### Find breed names
* When called, the endpoint will retrieve a list of dog breeds stored in the system
(returning the dog breed names only).

* You may choose whichever database you feel is appropriate for the task.
* You must use Java to build the solution, but other than this you are free to use whatever libraries,
build tools etc. that you wish. 
* You may use your own AWS account (and DB host if you wish) for
testing purposes (available for free), but the delivered solution should allow us to easily substitute
for our own connection information. 

Good Luck!!!

# Solution 
Framework / Utilities used :-`
* SpringBoot
* JDBI
* AMAZON AWS Client
* Maven
* Flyway

## Building
`$> mvn clean package`

## Running
`java -Dhttps.protocols=TLSv1.2,TLSv1.1,TLSv1 -jar target/pet-assignment-0.0.1-SNAPSHOT.jar`
* Tomcat runs on port 9999 
* Internal HSQLDB runs on port 8888 (JDBC connection URI  `jdbc:hsqldb:hsql://localhost:8888/petapp-db`)
* Database structure automatically created (through flyway)
* db/ folder for HSQLDB will automatically be created. Deleting it will cause a fresh db be created
* configurations (eg database details, s3 bucket configs etc) done through application.properties (in the source code overridable through placing a copy at the java execution folder
See [Spring docs](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html) for more details.


## End Points

### Generate a new dog breed record
|     |     |
| --- | --- |
| End Point URI | `http://<host>:<port>/dog/create` |
| Http Method | POST |
| Java Class | `GenerateNewDogEndPoint.java` |



### Retrieve by id
|     |     |
| --- | --- |
| End Point URI | `http://<host>:<port>/dog/{id}` |
| Http Method | GET |
| Java Class | `RetrievalEndPoint.java` |

| Path Variable | Description |
| ------------- | ----------- |
| id | The unique Dog id |

### Remove by id
|     |     |
| --- | --- |
| End Point URI | `http://<host>:<port>/dog/{id}` |
| Http Method | DELETE |
| Java Class | `RemovalEndPoint.java` |

| Path Variable | Description |
| ------------- | ----------- |
| id | The unique Dog id |


### Search by breed name
|     |     |
| --- | --- |
| End Point URI | `http://<host>:<port>/dog/breed/{breedName}` |
| Http Method | GET |
| Java Class | `DogSearchByBreedEndPoint.java` |

| Path Variable | Description |
| ------------- | ----------- |
| breedName | The Breed Name |


### Find breed names
|     |     |
| --- | --- |
| End Point URI | `http://<host>:<port>/dog/breeds` |
| Http Method | GET |
| Java Class | `DogBreedNamesEndPoint.java` |

