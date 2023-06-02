
# Recipe API

### Project intention
Recipe API is a java application which allows users to manage their favourite recipes. It will
allow adding, updating, removing and fetching recipes. Additionally, users should be able to filter
available recipes based on one or more of the following criteria:
* Whether or not the dish is vegetarian 
* The number of servings
* Specific ingredients (either include or exclude)
* Text search within the instructions.
### Technology and Frameworks
* Java 17
* Spring boot 3
* H2 database 
* Maven

### Run

* Ensure that you have installed all necessary software prior to proceeding. 
* Next, please locate and access the designated Github URL in order to checkout the code. 
* Lastly, execute the command provided below to install all dependencies and generate the requisite JAR file.

```bash
mvn clean install
```

To run the application locally you can use the following ways
- Using mvn command
```bash
 mvn spring-boot:run -Dspring-boot.run.profiles=test
```
- Using Java command, in your project target folder you can find the jar
```bash
 java -jar recipe/target/recipe-0.0.1-SNAPSHOT.jar
```

Once you have started the application it will load few entries in the in memory H2 database for our testing. The data will be loaded in 'test' and 'integration-test' profiles.

### Testing and Endpoints

We have five endpoints with this application

- GET -> http://localhost:8080/api/v1/recipe/
- POST -> http://localhost:8080/api/v1/recipe/create
- PUT -> http://localhost:8080/api/v1/recipe/{id}
- DELETE -> http://localhost:8080/api/v1/recipe/{id}
- POST -> http://localhost:8080/api/v1/recipe/search

Both Unit and Integration tests, covering a wide range of test cases. To execute these tests, use the following command:

- Using Java command
```bash
 mvn clean test
```

### Run via POSTMAN tool

Once you have run the application, you can use Postman tool to test teh functionality
#### GET ->  http://localhost:8080/api/v1/recipe/

The above endpoints will provide us the all available recipes

Sample response:
```json

{
    "data": [
        {
            "id": 1,
            "name": "pizza",
            "instructions": "bake pizza in oven",
            "isVegetarian": true,
            "servingSize": 1,
            "ingredient": [
                {
                    "ingId": 1,
                    "ingName": "sauce",
                    "measurement": "30ML",
                    "note": null
                },
                {
                    "ingId": 2,
                    "ingName": "dough",
                    "measurement": "100G",
                    "note": null
                },
                {
                    "ingId": 3,
                    "ingName": "vegetables",
                    "measurement": "100G",
                    "note": null
                },
                {
                    "ingId": 4,
                    "ingName": "cheese",
                    "measurement": "100G",
                    "note": null
                }
            ]
        },
        {
            "id": 2,
            "name": "Pasta",
            "instructions": "cook pasta in boiling water",
            "isVegetarian": false,
            "servingSize": 2,
            "ingredient": [
                {
                    "ingId": 5,
                    "ingName": "chicken",
                    "measurement": "100G",
                    "note": null
                },
                {
                    "ingId": 6,
                    "ingName": "pasta",
                    "measurement": "50G",
                    "note": null
                },
                {
                    "ingId": 7,
                    "ingName": "cream",
                    "measurement": "50G",
                    "note": null
                }
            ]
        }
    ],
    "timestamp": "31-05-2023 09:14:35",
    "message": "Retrieved the recipe successfully!",
    "responseCode": "OK"
}

```

#### POST -> http://localhost:8080/api/v1/recipe/create

In order to create a new recipe we need to provide our json in the request body.

Request sample:

```json
{
    "name": "Bread",
    "instructions": "bake bread in oven",
    "isVegetarian": true,
    "serviceSize": 3,
    "ingredient": [
        {
            "ingName": "oil",
            "measurement": "30ML"
        },
        {
            "ingName": "dough",
            "measurement": "50G"
        },
        {
            "ingName": "seeds",
            "measurement": "50G"
        }
    ]
}
```

Sample Response:

```json
{
    "data": {
        "id": 3,
        "name": "Bread",
        "instructions": "bake bread in oven",
        "isVegetarian": true,
        "servingSize": null,
        "ingredient": [
            {
                "ingId": 8,
                "ingName": "oil",
                "measurement": "30ML",
                "note": null
            },
            {
                "ingId": 9,
                "ingName": "dough",
                "measurement": "50G",
                "note": null
            },
            {
                "ingId": 10,
                "ingName": "seeds",
                "measurement": "50G",
                "note": null
            }
        ]
    },
    "timestamp": "02-06-2023 09:19:06",
    "message": "Successfully created the recipe!",
    "responseCode": "OK"
}
```
### PUT -> http://localhost:8080/api/v1/recipe/{id}

We will be able to modify the recipes like instructions and measurement, to do that we need to provide id of existing recipe and json request for new updates.

Sample request:

http://localhost:8080/api/v1/recipe/1

```json
{
    "name": "pizza",
    "instructions": "bake bread in oven",
    "isVegetarian": true,
    "serviceSize": 3,
    "ingredient": [
        {
            "ingName": "oil",
            "measurement": "100ML" // changing measurement 
        },
        {
            "ingName": "dough",
            "measurement": "50G"
        },
        {
            "ingName": "nuts", // Update ingredient
            "measurement": "50G" 
        }
    ]
}
```

Sample response:

```json
{
    "data": {
        "id": 1,
        "name": "pizza",
        "instructions": "bake bread in oven",
        "isVegetarian": true,
        "servingSize": null,
        "ingredient": [
            {
                "ingId": 11,
                "ingName": "oil",
                "measurement": "100ML",
                "note": null
            },
            {
                "ingId": 12,
                "ingName": "dough",
                "measurement": "50G",
                "note": null
            },
            {
                "ingId": 13,
                "ingName": "nuts",
                "measurement": "50G",
                "note": null
            }
        ]
    },
    "timestamp": "02-06-2023 09:26:29",
    "message": "Successfully updated the recipe!",
    "responseCode": "OK"
}


```
### DELETE -> http://localhost:8080/api/v1/recipe/{id}

We can also remove the recipes using the above url

Sample request :
http://localhost:8080/api/v1/recipe/1

Sample Response:
```json
{
"timestamp": "02-06-2023 09:31:07",
"message": "Deleted the recipe successfully!",
"responseCode": "OK"
}

```

### POST -> http://localhost:8080/api/v1/recipe/search

The above API specially used for complex search and please find the same request and supporting params for this.

These are all the combination operations for the search recipes.

- dataOption: all/any (determining the and/or operation)
- isVegetarion : with (value = boolean (true/false))
- noOfServings : eq,
- Instructions : like, not like
- Specific ingredient : eq/ne

Sample request:

```json
{
    "dataOption": "all",
    "searchCriteriaList": [
        {
            "filterKey": "instructions",
            "operation": "cn",
            "value": "cook"
        },
        {
            "filterKey": "isVegetarian",
            "operation": "with",
            "value": false
        },
        {
            "filterKey": "ingName",
            "operation": "eq",
            "value": "chicken"
        },
        {
            "filterKey": "servingSize",
            "operation": "eq",
            "value": 2
        }

    ]
}

```

Sample Response:
```json
{
    "data": [
        {
            "id": 2,
            "name": "Pasta",
            "instructions": "cook pasta in boiling water",
            "isVegetarian": false,
            "servingSize": 2,
            "ingredient": [
                {
                    "ingId": 5,
                    "ingName": "chicken",
                    "measurement": "100G",
                    "note": null
                },
                {
                    "ingId": 6,
                    "ingName": "pasta",
                    "measurement": "50G",
                    "note": null
                },
                {
                    "ingId": 7,
                    "ingName": "cream",
                    "measurement": "50G",
                    "note": null
                }
            ]
        }
    ],
    "timestamp": "02-06-2023 09:43:45",
    "message": "Successfully retried the recipes.",
    "responseCode": "OK"
}



```




Please reach out to developer for any clrifications:  Karthikeyan Karuppuchamy
