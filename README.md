## IBAN verification example application

This is an application showcasing a Spring Boot application with a REST API and React front end for validating IBANs.

https://iban-verification.onrender.com

### Running the application
#### For development
start back end
`./gradlew bootRun`

start front end
`cd frontend && npm start`

#### as docker container
build docker image
`make build`

run docker container
`make docker run`

### About the application
The back end is written in Kotlin using Spring Boot and Gradle.

IBAN validation is treated as a solved problem and the back end uses the [iban4j](https://github.com/arturmkrtchyan/iban4j) to implement the validation.
The use of the library is limited to the [DDD inspired ValueObject](https://martinfowler.com/bliki/ValueObject.html) `IbanValueObject` class.
This class offers a factory method for the IBAN string and provides a type safe way to pass it around the application.
By using a value object we can be sure that the IBAN is valid, and we can avoid passing around invalid IBANs.
Instead of using the libraries exception based validation, the `IbanValueObject` uses the `Either` type from [Arrow](https://arrow-kt.io/) to indicate if the IBAN is valid or not.

For the requirement of returning the bank's name we use the BLZ data from [Bundesbank](https://www.bundesbank.de/resource/blob/602630/a5738aceb2e0a825cbf836ad4163be4c/mL/blz-aktuell-xlsx-data.xlsx).
The Excel file was explored and converted to a csv file using Kotlin Dataframe in a Kotlin Notebook (Kotlin Kernel for Jupyter).
With the exported csv file the actual production repository is as well using Kotlin Dataframe to load the data and provide the bank name for a given BLZ.

With the help of [public test IBAN](https://de.iban.com/testibans) and some self produced test data the entire back end was developed in a [TDD](https://martinfowler.com/bliki/TestDrivenDevelopment.html) way.
The application showcases different test methodologies like unit tests, WebMVCTest and integration tests.

The front end is written in TypeScript using React and based on [create-react-app](https://create-react-app.dev/).

It makes use of the [Fetch API](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API) to communicate with the back end.

For data modelling we use the algebraic data type [Option](https://gcanti.github.io/fp-ts/modules/Option.ts.html) from [fp-ts](https://gcanti.github.io/fp-ts/)
along with a self defined ApiResponseData ADT to model expected responses from the back end.

### Things to improve
- saving API requests to a database
- adding Spring Boot Actuator for runtime metrics
- having a definition of the API in OpenAPI format
- add styling to the front end
- adding a CI/CD pipeline