# Проект

### Проектная работа

## Цель:

Реализовать собственный проект с применением Spring

## Описание/Пошаговая инструкция выполнения домашнего задания:

### Требования к проекту и его технологическому стеку

0. Объем, как 3-4 ДЗ (за меру можно взять ДЗ по веб-библиотеке)
1. Spring Core 
2. Spring Security (желательно через JWT, желательно вынести в отдельный сервис)
3. Spring MVC/WebFlux 
4. Bean Validation 
5. Spring Data Jdbc/Jpa/MongoDB/... 
6. Коммуникация между микросервисами по REST/очередям (если есть микросервисы)
7. Соблюдение всех практик и рекомендаций, которые давались на курсе. В том числе должна быть работа с исключениями 
8. Юнит и интеграционные тесты приветствуются 
9. Использование паттернов отказоустойчивости приветствуется (CircuitBreaker/Retry/Cache/ServiceRegistry/ConfigServer/ApiGateway/...)
10. Docker приветствуется

Для допуска к выполнению проектной работы, нужно чтобы были сданы ДЗ к следующим лекциям:
1. Чёрная магия" Spring Boot
2. DAO на Spring JDBC
3. JPQL, Spring ORM, DAO на основе Spring ORM + JPA и/или "Белая магия" Spring Data: Spring Data JPA
4. Spring MVC View и/или Современные приложения на Spring MVC
5. Spring Security: ACL

### Темы для работы:
- Онлайн-библиотека
- Блог с админкой/регистрацией пользователей
- Всё что угодно, к чему "душа лежит"

## Критерии оценки:
### Факт сдачи:

- 0 - задание не сдано
- 1 - задание сдано

### Степень выполнения (количество работающего функционала, что примет заказчик, что будет проверять тестировщик):

- 0 - ничего не работает или отсутствует основной функционал
- 1 - не работает или отсутствует большая часть критического функционала
- 2 - основной функционал есть, возможны небольшие косяки
- 3 - основной функционал есть, всё хорошо работает
- 4 - основной функционал есть, всё хорошо работает, тесты и/или задание перевыполнено

### Способ выполнения (качество выполнения, стиль кода, как ревью перед мержем):
- 0 - нужно править, мержить нельзя (нарушение соглашений, публичные поля)
- 1 - лучше исправить в рамках этого ДЗ для повышения оценки
- 2 - можно мержить, но в следующих ДЗ нужно поправить.
- 3 - можно мержить, мелкие недочёты
- 4 - отличная работа!
- 5 - экстра балл за особо красивый кусочек кода/решение целиком (ставится только после отличной работы, отдельно не ставится)

Статус **"Принято"** ставится от **6** и **выше** баллов.

Ниже 6, задание не принимается.

Идеальное, но не работающее, решение = 1 + 0 + 4 (+4 а не 5) = 5 - не принимается.

Если всё работает, но стилю не соответствует (публичные поля, классы капсом) = 1 + 4 + 0 = 5 - не принимается

# Выбранная тема:

## "Музыкальная онлайн-библиотека"

### Фукционал:

- CRUD метаинформации о песнях (связь с другими сущностями, например, исполнителях), исполнителях, альбомах, жанрах

### Сущности:
- песня (список альбомов, жанр, текст, время, список исполнителей (feat/vs))
- альбом (список песен, исполнитель (основной))
- исполнитель (название)
- пользователь (логин, пароль, роль)
- жанр (название)
- роль/права (админ/пользователь; чтение/запись)

### Технологии:

- PG
- Spring
- Java 17
- log4j2
- lombok
- junit5
- docker
- maven