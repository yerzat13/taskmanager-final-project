# Task Management System
## Author: Abdrassulayev Yerzat
## Technologies: Spring Boot, JWT, Docker, PostgreSQL


## Проектирование базы данных

Спроектирована реляционная схема с 5+ таблицами, включая отношения:

- **One-to-Many**: пользователь → задачи
- **Many-to-Many**: задачи ↔ категории (через связующую таблицу)
- **One-to-One**: пользователь ↔ профиль

run 

git clone https://github.com/yerzat13/taskmanager-final-project.git
docker-compose up -d
mvn spring-boot:run -Dspring-boot.run.profiles=dev


