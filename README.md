# Testez une application full-stack

![alt text](https://user.oc-static.com/upload/2022/10/25/16667162692336_P5_banner-numdev.png)

[![forthebadge](https://forthebadge.com/images/badges/built-by-developers.svg)](https://forthebadge.com)

# Spécifications techniques

## Stack Technique

### Front-end
- Angular

### Back-end
- Java Spring Boot
- MySQL

## Cloner le projet

> git clone https://github.com/Ibra768/Testez-une-application-full-stack.git

## Lancer la partie Front

1. Se rendre dans le dossier front

> cd front

2. Installer les dépendances

> npm install

3. Lancer le serveur

> npm run start

## Lancer la partie Back

1. Se rendre dans le dossier back

> cd back

2. Installer les dépendances

> mvn clean install

3. Lancer le script SQL

> mysql -u root -p

> CREATE DATABASE test;

Lancer le script ressources/sql/script.sql

4. Lancer le serveur

> mvn spring-boot:run

## Lancer les tests sur la partie front

1. Se rendre dans le dossier front

> cd front

2. Installer les dépendances

> npm install

3. Lancer les tests

Technologies utilisées : Jest & Cypress

### Lancer les tests Jest

> npm run test

> npm run test:coverage

### Lancer les tests Cypress

> npm run e2e

> npm run e2e:coverage

## Lancer les tests sur la partie back

1. Se rendre dans le dossier back

> cd back

2. Installer les dépendances

> mvn clean install

3. Lancer le script SQL

Lancer le script de données de test ressources/sql/script.sql

4. Lancer les tests

Technologies utilisées : JUnit, Mockito, Jacoco

> mvn test

> mvn jacoco:report