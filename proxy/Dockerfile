FROM openjdk:17
COPY /target/proxy-0.0.1-SNAPSHOT.jar proxy.jar
EXPOSE 9090
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/proxy.jar"]