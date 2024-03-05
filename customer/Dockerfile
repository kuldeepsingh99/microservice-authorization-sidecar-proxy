FROM openjdk:17
COPY /target/customer-0.0.1-SNAPSHOT.jar customer.jar
EXPOSE 8085
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/customer.jar"]