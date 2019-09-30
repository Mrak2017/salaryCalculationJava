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
- ng build (//todo убрать необходимость устанавливать Angular CLI)
- docker compose up -d
- зайти на http://localhost:8001

#### Dev-запуск:
- docker-compose up -d
- ng build --watch
- gradlew app-server:build
- docker-compose up -d
- docker-compose stop

#### Запуск тестов (dev-режим):
- docker-compose up -d test-db
- запустить нужный тест или пачку тестов
- docker-compose stop test-db
