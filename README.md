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
