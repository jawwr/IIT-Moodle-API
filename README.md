# IIT Moodle API

## About
### This is a multifunctional API for working with IIT Moodle
With this api, you can work with the moodle platform

## Technologies
### Microservices:
- Java
  - Spring Boot
    - Web services
    - Cloud GateWay
    - JPA
- Eureka Server
### Message Broker:
- RabbitMQ
### Parser
- Java
  - Selenium Library

## Functional
:white_check_mark: News parser from telegram
>The ability to receive the latest news from the telegram feed

:white_check_mark: Marks parser from Moodle IIT
>The ability to get grades for items displayed on the moodle platform

:white_check_mark: Schedule of classes
>The possibility of obtaining a lesson schedule

:white_check_mark: Events parser from Moodle IIT
>The ability to get a schedule of upcoming events located on the moodle platform

## Architecture
### The entire application is divided into separate microservices
#### Services:
- User service
  - Getting information about a person by login
- News service
  - Getting news
  - Parsing news from the channel's telegrams
- Schedule service
  - Getting a schedule by login
- Mark service
  - Getting mark by login
- Event service
  - Getting event by login

The RabbitMQ broker is used to deliver messages between microservices


![](/images/Architecture.png)