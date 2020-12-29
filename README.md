# MICROSERVICES WITH SPRING BOOT
## BEFORE TO START
Each project have their own characteristics and scopes, so it's important mark off the project type and the ports range.
| FOLDER NAME | PROJECT TYPE | PORTS |
| ----------- | ------------ | ----- |
| spring-cloud-config-server | Spring Cloud Config Server | 8888 |
| limits-service | Spring Cloud Config Client | 8080 |
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