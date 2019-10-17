# Salary calculation application (Java)
+ Java 12
    + Spring Boot
    + Spring Data
    + Embeded Tomcat
    + Hibernate
+ Angular v7 
    + RxJS
    + Angular Material Design
    + moment.js 
+ PostgreSQL
+ docker-compose
+ gradle

Same application but with C# - https://github.com/Mrak2017/salaryCalculation

#### How to build and start application:
1) Build server application  
`docker-compose -f docker-compose.init.yml build build-container`
2) Init and build client application  
   go to ./app-client  
   run in console:  
```
npm i
npm run build
```
3) Start whole application  
`docker-compose up -d`
4) Open in browser  
http://localhost:8001
