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

## Keycloak

Run this command to start the keycloak Instance
```
docker run -it -p "8081:8080" -e 'KEYCLOAK_ADMIN=admin' -e 'KEYCLOAK_ADMIN_PASSWORD=admin' quay.io/keycloak/keycloak:21.0.2 start-dev
```
Login to keycloak with URL - http://localhost:8080/ with username - admin and password - admin 

### Step 1:- Create Realm

![Create Realm](https://github.com/kuldeepsingh99/sidecar-proxy-authentication/blob/main/images/keycloak1.png)

![Create Realm](https://github.com/kuldeepsingh99/sidecar-proxy-authentication/blob/main/images/keycloak2.png)

### Step 2:- Create Client

![Create Realm](https://github.com/kuldeepsingh99/sidecar-proxy-authentication/blob/main/images/keycloak3.png)

![Create Realm](https://github.com/kuldeepsingh99/sidecar-proxy-authentication/blob/main/images/keycloak14.png)

![Create Realm](https://github.com/kuldeepsingh99/sidecar-proxy-authentication/blob/main/images/keycloak5.png)


### Make Sure the Service Account is enabled

![Create Realm](https://github.com/kuldeepsingh99/sidecar-proxy-authentication/blob/main/images/keycloak6.png)

### Create all the necessary Roles for the client 

![Create Realm](https://github.com/kuldeepsingh99/sidecar-proxy-authentication/blob/main/images/keycloak15.png)

![Create Realm](https://github.com/kuldeepsingh99/sidecar-proxy-authentication/blob/main/images/keycloak16.png)

### Map all the roles with the Service Account

![Create Realm](https://github.com/kuldeepsingh99/sidecar-proxy-authentication/blob/main/images/keycloak17.png)

![Create Realm](https://github.com/kuldeepsingh99/sidecar-proxy-authentication/blob/main/images/keycloak18.png)


### Now Try to get the access token and view the roles 

![Create Realm](https://github.com/kuldeepsingh99/sidecar-proxy-authentication/blob/main/images/keycloak7.png)

![Create Realm](https://github.com/kuldeepsingh99/sidecar-proxy-authentication/blob/main/images/keycloak13.png)


## Deploying Service
Make sure Kubernetes is enabled

### Step 1 - Compile the customer and proxy project 
```
mvn package
```

### Step 2 - Create images for customer and proxy project 
```
docker build -t customer:latest .
```
```
docker build -t proxy:latest .
```

### Step 3 - Deploy the yml file

Run the [file](https://github.com/kuldeepsingh99/sidecar-proxy-authentication/blob/main/deployment/deploy.yml) to deploy services
```
kubectl apply -f deploy.yml
```

the most important part of the file is the container section
```
containers:
        - name: ms-customer
          image: customer:latest
          imagePullPolicy: IfNotPresent
          ports:
            - name: http
              containerPort: 8085
        - name: proxy
          image: proxy:latest
          imagePullPolicy: IfNotPresent
          env:
            - name: ALLOWED_URLS
              value: "/api/v1/customer:USER,/api/v2/customer:USER,/api/v3/customer:ADMIN,/api/v4/customer:MANAGER"
            - name: CLIENT
              value: "demo-client"
            - name: SERVICE_PORT
              value: "8085"
          ports:
            - name: http
              containerPort: 9090
```
Here we can see we have two containers
- main
  - Main container is running on port 8085
- proxy
  - proxy container is running on port 9090
  - Proxy has three environment variables **ALLOWED_URLS**, **CLIENT** and **SERVICE_PORT**
  - In ALLOWED_URLS we specify the URL and Role Mapping ex. This URL `/api/v1/customer` can only be accessed by someone who has USER Role.
  - CLIENT is the name of the keycloak client where Roles are mapped with the service account.
  - SERVICE_PORT is the port on which main container is running, this is required because on this port only Side car container will forward the request.



  - 




















  


















