# Microservice Authorization with SideCar Proxy using Spring Security oAuth 2

In this example, I will show you how to handle Authorization for the microservices using Side Car.

There are other ways to handle Authorization like 
1. API Gateway 
2. Istio Side Car Container
3. Microservice itself handles the Authorization
4. Preparing a library that handles Authorization

But every solution has pros and cons as per project needs, like for example if we develop a library in Java, it can only be used for microservices that are developed in Java, if in the organization we have Nodejs based microservice, then we have developed a similar library in node.

## Prerequisites

- Java 17
- Docker compose or colima
- Keycloak 21






