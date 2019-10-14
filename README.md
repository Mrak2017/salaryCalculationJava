# Приложение для ведения заработной платы (на Java)
+ Java 8
    + Spring Boot
    + Spring Data
    + Embeded Tomcat
    + Hibernate
+ Angular v7 
    + RxJS
    + Angular Material Design
    + moment.js 
+ PostgreSQL
+ docker
+ gradle

Реализация задания на C# - https://github.com/Mrak2017/salaryCalculation

#### Запуск:
1) Init and build client application  
   go to ./app-client  
   run in console:  
```
npm i
npm run build
```
2) Start docker container with test database  
`docker-compose up -d test-db`
3) Build java server (in main directory)  
`gradlew app-server:build`
   or  
`./gradlew app-server:build`
4) Start application  
`docker-compose up -d`
5) Open in browser  
http://localhost:8001

#### Dev-запуск:
- docker-compose up -d
- ng build --watch
- gradlew app-server:build
- docker-compose up -d
- docker-compose stop
