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
Framework / Utilities / Languages used :-`
* SpringBoot
* JDBI
* AMAZON AWS Client
* Maven3
* Flyway
* Java 

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
General format when successfull
`json
{
   "ok": true,
    "messages": [
        <success messages>
    ],
    "payload": <payload - end point specific>
}
`

General format on failure 
`json 
{
    "ok": false,
    "messages": [
        <error messages>
    ],
    "payload": null
}
`


### Generate a new dog breed record
|     |     |
| --- | --- |
| End Point URI | `http://<host>:<port>/dog/create` |
| Http Method | POST |
| Java Class | `GenerateNewDogEndPoint.java` |

#### Example Response:
`json
{
    "ok": true,
    "messages": [
        "Dog of breed otterhound saved (id = 0)"
    ],
    "payload": {
        "id": 0,
        "breedName": "otterhound",
        "creationDate": "14/01/2019 01:01:18",
        "url": "https://petapp-bucket.s3.ap-southeast-2.amazonaws.com/DogPicture-af16baa8-d276-45ae-939d-3f9c85d523d8"
    }
}
`


### Retrieve by id
|     |     |
| --- | --- |
| End Point URI | `http://<host>:<port>/dog/{id}` |
| Http Method | GET |
| Java Class | `RetrievalEndPoint.java` |
| Error | DogNotFoundException if dog not found |

| Path Variable | Description |
| ------------- | ----------- |
| id | The unique Dog id |

#### Example Response:
`json
{
    "ok": true,
    "messages": [
        "Success"
    ],
    "payload": {
        "id": 0,
        "s3Key": "DogPicture-af16baa8-d276-45ae-939d-3f9c85d523d8",
        "breedName": "otterhound",
        "url": "https://petapp-bucket.s3.ap-southeast-2.amazonaws.com/DogPicture-af16baa8-d276-45ae-939d-3f9c85d523d8",
        "date": "14/01/2019 01:01:18"
    }
}
`

`json
{
    "ok": false,
    "messages": [
        "tmjee.pet.commons.DogNotFoundException: Dog with id 1 is not found"
    ],
    "payload": null
}
`

### Remove by id
|     |     |
| --- | --- |
| End Point URI | `http://<host>:<port>/dog/{id}` |
| Http Method | DELETE |
| Java Class | `RemovalEndPoint.java` |
| Error | DogNotFoundException if dog not found |

| Path Variable | Description |
| ------------- | ----------- |
| id | The unique Dog id |

#### Example Response:
`json
{
    "ok": true,
    "messages": [
        "Dog with id 3 deleted"
    ],
    "payload": null
}
`

`json
{
    "ok": false,
    "messages": [
        "tmjee.pet.commons.DogNotFoundException: Dog with id 10 is not found"
    ],
    "payload": null
}
`


### Search by breed name
|     |     |
| --- | --- |
| End Point URI | `http://<host>:<port>/dog/breed/{breedName}` |
| Http Method | GET |
| Java Class | `DogSearchByBreedEndPoint.java` |

| Path Variable | Description |
| ------------- | ----------- |
| breedName | The Breed Name (case insensitive, match substring, eg 'mino' will match 'adominodi', 'minos', amino' etc |

#### Example Response:
`json
{
    "ok": true,
    "messages": [
        "Success"
    ],
    "payload": [
        {
            "id": 0,
            "breedName": "otterhound",
            "creationDate": "14/01/2019 01:01:18",
            "url": "https://petapp-bucket.s3.ap-southeast-2.amazonaws.com/DogPicture-af16baa8-d276-45ae-939d-3f9c85d523d8",
            "s3Key": "DogPicture-af16baa8-d276-45ae-939d-3f9c85d523d8"
        },
        {
            "id": 1,
            "breedName": "cotondetulear",
            "creationDate": "14/01/2019 01:08:18",
            "url": "https://petapp-bucket.s3.ap-southeast-2.amazonaws.com/DogPicture-93cf35dd-cdd4-47df-b73d-c4dcbc50b9ef",
            "s3Key": "DogPicture-93cf35dd-cdd4-47df-b73d-c4dcbc50b9ef"
        },
        {
            "id": 2,
            "breedName": "malinois",
            "creationDate": "14/01/2019 01:08:21",
            "url": "https://petapp-bucket.s3.ap-southeast-2.amazonaws.com/DogPicture-749dd47c-bc70-4520-9437-663fa5c7dd89",
            "s3Key": "DogPicture-749dd47c-bc70-4520-9437-663fa5c7dd89"
        },
        {
            "id": 3,
            "breedName": "pekinese",
            "creationDate": "14/01/2019 01:08:24",
            "url": "https://petapp-bucket.s3.ap-southeast-2.amazonaws.com/DogPicture-8e69d354-c8e2-4c7e-94e2-7aa863a05006",
            "s3Key": "DogPicture-8e69d354-c8e2-4c7e-94e2-7aa863a05006"
        },
        {
            "id": 4,
            "breedName": "basenji",
            "creationDate": "14/01/2019 01:08:27",
            "url": "https://petapp-bucket.s3.ap-southeast-2.amazonaws.com/DogPicture-daf0907e-a405-4e08-bc8c-619e5049c8f2",
            "s3Key": "DogPicture-daf0907e-a405-4e08-bc8c-619e5049c8f2"
        }
    ]
}
`



### Find breed names
|     |     |
| --- | --- |
| End Point URI | `http://<host>:<port>/dog/breeds` |
| Http Method | GET |
| Java Class | `DogBreedNamesEndPoint.java` |

#### Example Response:
`json
{
    "ok": true,
    "messages": [
        "success"
    ],
    "payload": {
        "breeds": [
            "otterhound",
            "cotondetulear",
            "malinois",
            "pekinese",
            "basenji",
            "kuvasz"
        ]
    }
}
`

