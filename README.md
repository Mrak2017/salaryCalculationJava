# Salary calculation application (Java)
+ Java 12
    + Spring Boot
    + Spring Data
    + Embeded Tomcat
    + Hibernate
    + Liquibase
+ Angular 7 
    + RxJS
    + Angular Material Design
    + moment.js
+ PostgreSQL 12.1
+ docker-compose (+ multi-stage)
+ gradle 5.0

Same application but with C# - https://github.com/Mrak2017/salaryCalculation

#### How to build and start application:
1) Prepare (build) containers  
`docker-compose -f docker-compose.builder.yml build`
2) Start application  
`docker-compose up -d`
3) Open in browser  
http://localhost:8001
