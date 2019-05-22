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
- npm i (внутри app-client)
- docker compose up -d

#### Dev-запуск:
- gradlew app-server:build
- ng build --watch
- docker-compose up -d
- docker-compose stop 