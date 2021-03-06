# MICROSERVICES WITH SPRING BOOT
## BEFORE TO START
Each project have their own characteristics and scopes, so it's important mark off the project type and the ports range.
| FOLDER NAME | PROJECT TYPE | PORTS |
| ----------- | ------------ | ----- |
| spring-cloud-config-server | Spring Cloud Config Server | 8888 |
| limits-service | Spring Cloud Config Client | 8080 |
| currency-exchange-service | Consumes from database | 8000, 8001, 8002... |
| currency-conversion-service | Consumes from Exchange service | 8100, 8101, 8102... |
## WHAT ARE MICROSERVICES?
Microservices are an architectural approach to building applications. As an architectural framework, microservices are distributed and loosely coupled, so one team’s changes won’t break the entire app. The benefit to using microservices is that development teams are able to rapidly build new components of apps to meet changing business needs.
### Microservices vs monolithic applications
![monolithic-vs-microservices](https://raw.githubusercontent.com/droidark/microservices/master/diagrams/monolithic-vs-microservices.svg)
### What are the benefits of a microservices architecture?
Microservices give your teams and routines a boost through distributed development. You can also develop multiple microservices concurrently. This means more developers working on the same app, at the same time, which results in less time spent in development.

![benefits-of-microservices](https://raw.githubusercontent.com/droidark/microservices/master/diagrams/benefits-of-microservices.svg)

### Challenges with microservices
- Bounded context. [Identifying microservice boundaries](https://docs.microsoft.com/en-us/azure/architecture/microservices/model/microservice-boundaries).
- Configuration management.
- Dynamic scale up and scale down.
- Visibility with what happend with each microservice.
- Pack of cards. A common practice is binding a lot of microservices, if the most important is down, all applications collapse.
## SPRING CLOUD
Spring Cloud is not a simple project, Spring Cloud is a range of projects to develop decoupling systems.
### Solutions provided by Spring Cloud
- Configuration management
    - **Spring Cloud Config Server**: Provides an approach where you can store all configuration for all the different environments of all the microservices in a Git repository.
- Dynamic scale up and scale down
    - **Naming Server (Eureka).**
    - **Ribbon Load Balancing:** It’s used to load the charge between all instances of each microservice.
    - **Feign (Easier REST Clients).**
- Visibility and monitoring
    - **Zipkin Distributed Tracing.**
    - Netflix API Gateway
- Fault tolerance
    - **Hystrix.**
## SPRING CLOUD CONFIG SERVER
**Spring Cloud Config provides server-side and client-side support for externalized configuration in a distributed system.** With the Config Server, you have a central place to manage external properties for applications across all environments. The concepts on both client and server map identically to the Spring Environment and PropertySource abstractions, so they fit very well with Spring applications but can be used with any application running in any language. As an application moves through the deployment pipeline from dev to test and into production, you can manage the configuration between those environments and be certain that applications have everything they need to run when they migrate. The default implementation of the server storage backend uses git, so it easily supports labelled versions of configuration environments as well as being accessible to a wide range of tooling for managing the content. It is easy to add alternative implementations and plug them in with Spring configuration.

![spring-cloud-config-server](https://raw.githubusercontent.com/droidark/microservices/master/diagrams/spring-cloud-config-server.svg)

### Create server for Spring Cloud Config Server
We need to create a project with **Spring Cloud Config Server** dependency.

![spring-cloud-config-server-setup](https://raw.githubusercontent.com/droidark/microservices/master/diagrams/spring-cloud-config-server.png)

### Connect Spring Cloud Config Server with GIT
1. Add the Git folder as a link source inside the project.
1. Edit application properties file with the Git repo URL.
    ```yml
    spring:
    cloud:
        config:
        server:
            git:
            uri: git-url
    ```
1. `@EnableConfigServer` annotation in project main class.
### Verify configuration
To verify the correct behavior follow the next path:
    > http://spring-clud-config-server-host/{properties-file-name-in-git-repository}/default

### Create properties for many environments
1. Create a copy based on main application properties files followed by -dev or -qa (example: demo-dev.yml or demo-qa.yml)
1. Go to 
    > http://spring-clud-config-server-host/{properties-file-name-in-git-repository}/(default|dev|qa)

### Create client for Spring Cloud Config Server
We need to create a project with **Spring Cloud Config Client** dependency.

![spring-cloud-config-client-setup](https://raw.githubusercontent.com/droidark/microservices/master/diagrams/spring-cloud-config-client.png)

### Read properties from properties file
1. Create a POJO
    ```java
    public class LimitConfiguration {
        private int minimum;
        private int maximum;
        
        // CONSTRUCTOS, GETTERS & SETTERS
    }
    ```
1. Create Configuration Bean to read the properties file.
    ```java
    @ConfigurationProperties("{prefix}")
    public class AppConfiguration {
        private int minimum;
        private int maximum;

        // GETTERS AND SETTERS
    }
    ```
    1. **OPTION 1:** Add `@Configuration` annotation before @ConfigurationProperties.
    1. **OPTION 2 (RECOMMENDED):** Add `@EnableConfigurationProperties(AppConfiguration.class)` in the main class.

### Connect Spring Cloud Config Server Client with Spring Cloud Config Server
1. In **Spring Cloud Config Server Client**, rename *application.yml* to *bootstrap.yml*
1. Add **Spring Cloud Config Server** URL to *bootstrap.yml*
    ```yml
    spring:
    application:
        name: spring-cloud-config-client-name
    cloud:
        config:
        uri: http://spring-clud-config-server-host
    ```
> **NOTE:** The application properties files inside the git repository must be the same name that **Spring Cloud Config Server Client application name:** eg: **limits-service**.yml, **limits-service-dev**.yml, **limits-service-qa**.yml.

> **Spring Cloud 2020.0 Release Notes** Bootstrap, provided by spring-cloud-commons, is no longer enabled by default. If your project requires it, it can be re-enabled by the following new starter. To solve that it's necessary add the following dependency in pom.xml file
```xml
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-bootstrap</artifactId>
    </dependency>
```
### Configuring profiles in Spring Cloud Config Server Client
1. In *bootstrap.yml* add the next lines (Profiles are used to get the configuration for a specific environment).
    ```yml
    spring:
    application:
        name: spring-cloud-config-client-name
    cloud:
        config:
        uri: http://spring-clud-config-server-host
    profiles:
        active: qa | dev | etc
    ```
## CONSUMING REST SERVICE
### WebClient instead of RestTemplate
Simply put, WebClient is an interface representing the main entry point for performing web requests.

It has been created as a part of the Spring Web Reactive module and will be replacing the classic RestTemplate in these scenarios. The new client is a reactive, non-blocking solution that works over the HTTP/1.1 protocol.

Finally, the interface has a single implementation – the Default WebClient class – which we'll be working with.

> **More information:** https://springframework.guru/spring-5-webclient/ and https://www.baeldung.com/spring-5-webclient

### WebClient Example

```java
@GetMapping(path = "/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
public Mono<CurrencyConversion> convertCurrency(@PathVariable String to,
        @PathVariable String from,
        @PathVariable BigDecimal quantity) {
    Map<String, String> uriVariables = Map.of("from", from, "to", to);
    WebClient client = WebClient
            .builder()
            .baseUrl("http://localhost:8000/currency-exchange/from/{from}/to/{to}")
            .defaultUriVariables(uriVariables)
            .build();
    return client.get().retrieve().bodyToMono(CurrencyConversion.class);
}
```
### FEING
Feing makes it very easy to invoke other microservices, other RESTful services. The other additional thing is, it provides integration with something called **Ribbon** which is a client-side balancing framework.

#### Use Feing
##### Import dependency
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```
##### Configure in Application Environment
```java
@SpringBootApplication
@EnableFeignClients("{default-application-package-is-OPTIONAL}")
public class CurrencyConversionServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CurrencyConversionServiceApplication.class, args);
    }

}
```
##### Create a feing proxy (in service layer)
```java
@FeignClient(name = "currency-exchange-service", url = "localhost:8000")
public interface CurrencyExchangeServiceProxy {
    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    CurrencyConversion retrieveExchangeValue(@PathVariable("from") String from, @PathVariable("to") String to);
}
```
##### Using Feing Proxy in Controller
```java
@GetMapping(path = "/currency-converter-feing/from/{from}/to/{to}/quantity/{quantity}")
public CurrencyConversion convertCurrencyFeing(@PathVariable String to,
        @PathVariable String from,
        @PathVariable BigDecimal quantity) {
    Map<String, String> uriVariables = Map.of("from", from, "to", to);
    CurrencyConversion response = currencyExchangeServiceProxy.retrieveExchangeValue(from, to);
    return new CurrencyConversion(response.getId(), from, to, response.getConversionMultiple(), quantity,
            quantity.multiply(response.getConversionMultiple()), response.getPort());
}
```

## NAMING SERVER
All the instances of all microservices would register with the naming server. Whenever an instance of a microservice comes up it would register itself with the **EUREKA NAMING SERVER** (called service registration). Also that is naming **SERVICE DISCOVERY**.

![naming-server](https://raw.githubusercontent.com/droidark/microservices/master/diagrams/naming-server.svg)

### Setting up Eureka Server
![eureka-server](https://raw.githubusercontent.com/droidark/microservices/master/diagrams/eureka-server.png)

### Configure Eureka Server Application
Add `@EnableEurekaServer` annotation in main application class
```java
@SpringBootApplication
@EnableEurekaServer
public class NetflixEurekaNamingServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(NetflixEurekaNamingServerApplication.class, args);
    }
}
```
Edit `application.yml`
```yml
spring:
  application:
    name: netflix-eureka-naming-server
server:
  port: 8761 #DEFAULT PORT FOR EUREKA SERVER
eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
```
### Eureka Client, connecting services to Eureka Server

Add Eureka Client dependency
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

Add `@EnableDiscoveryClient` to the main application class
```java
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class CurrencyConversionServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CurrencyConversionServiceApplication.class, args);
    }
}
```

Edit `application.yml` file
```yml
eureka:
  client:
    service-url:
      default-zone: http://[eureka-server-host]:[eureka-server-port]
```

## LOAD BALANCING

### Spring Cloud LoadBalancer
Spring Cloud Load Balancer provides a simple round-robin rule for load balancing between multiple instances of a single service, the most common is to implement the Load Balancer on the client-side.

#### Setting up Spring Cloud LoadBalancer
Add the following depency in client `pom.xml`
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-loadbalancer</artifactId>
</dependency>
```

> **NOTE:** Spring Cloud LoadBalancer is preconfigured to work  with Eureka Server, thus it's not necessary to write code to get the communication though the different instances

### Relationship between Eureka, Ribbon and Feing

![spring-cloud-loadbalancer](https://raw.githubusercontent.com/droidark/microservices/master/diagrams/spring-cloud-loadbalancer.svg)

- **Naming server (Eureka):** Naming server is the one which offers registration and service discovery functionality.
- **Spring Cloud LoadBalancer**: Spring Cloud LoadBalancer provides the client side load balancing to distribute the load between multiple services providers.
- **Feing:** Feign is a declarative web service client. It makes writing web service clients easier.

## Connect Spring Cloud LoadBalancer with Eureka Server
Eureka clients are able to get different instances through the service-name automatically.

Modify `@FeignClient` configuration on client interface.
```java
@FeignClient(name = "currency-exchange-service")
public interface CurrencyExchangeServiceProxy {
    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    CurrencyConversion retrieveExchangeValue(@PathVariable("from") String from, @PathVariable("to") String to);
}
```