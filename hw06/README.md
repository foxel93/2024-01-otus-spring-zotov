# Домашнее задание

### Переписать приложение для хранения книг на ORM

## Цель:

Полноценно работать с JPA + Hibernate для подключения к реляционным БД посредством ORM-фреймворка

## Результат

Высокоуровневое приложение с JPA-маппингом сущностей

## Описание/Пошаговая инструкция выполнения домашнего задания:

Домашнее задание выполняется переписыванием предыдущего на JPA.

Требования:

1. Использовать JPA, Hibernate только в качестве JPA-провайдера
2. Spring Data пока использовать нельзя
3. Загрузка связей сущностей не должна приводить к большому количеству запросов к БД или избыточному по объему набору данных (проблема N+1 и проблема произведения таблиц)
4. Добавить сущность "комментария к книге", реализовать CRUD для новой сущности. Получение всех комментариев делать не нужно. Только конкретного комментария по id и всех комментариев по конкретной книге по ее id
5. Покрыть репозитории тестами, используя H2 базу данных и соответствующий H2 Hibernate-диалект для тестов.
6. Не забудьте отключить DDL через Hibernate
7. Аннотация @Transactional должна присутствовать только на методах сервиса.

Это домашнее задание будет использоваться в качестве основы для других ДЗ

Данная работа не засчитывает предыдущую!

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