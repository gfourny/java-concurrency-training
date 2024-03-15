<h1 align="center">Bienvenue dans l'atelier java-concurrency-training 👋</h1>

> Atelier de formation autour de la programmation concurrente en java.<br />

## Objectifs 🎯

- Comprendre ce qu'est la programmation asynchrone
- Comprendre ce qu'est la programmation concurrente
- Pourquoi en avons-nous besoin
- Quand en avons-nous besoin
- Masteriser la programmation concurrente et asynchrone en java
- Les pièges des virtual threads

### Démonstrations et exercices 🎬

[Déroulé de l'atelier](doc/training.md) 📘

- Optimiser les temps de réponse d'une API
- Optimiser le nombre d'appels concurrent
- Optimiser les resources allouées à l'application
    - Réduction des pod sur GKE
    - Temps de traitements optimisé
    - Parallélisation des traitements asynchrones
- Utilisation du profiler Intellij
    - Identification des tâches bloquantes

### Ce qui ne sera pas abordé ❌

- Programmation réactive
  - Spécification réactive Streams
  - Spring WebFlux
  - Reactor / Mutiny / RxJava
- Abstraction Spring
  - Annotations @Async: <a href="https://spring.io/guides/gs/async-method">Documentation</a>

## Technologies 🔭

Cette API est développée avec les technologies suivantes :

- `Java 21 :` le langage

- `SpringBoot 3.2 :`  <a href="https://spring.io/projects/spring-boot">Le framework</a>

- `Wiremock :` <a href="https://wiremock.org/docs/">Documentation</a>

- `TestContainers :` <a href="https://testcontainers.com/">PostgreSQL conteneurisé</a>

- `Instancio :` <a href="https://www.instancio.org/user-guide/">Générateur d'objets Java</a>

- `Toxiproxy :` <a href="https://github.com/Shopify/toxiproxy">Proxy utilisé avec Testcontainers permettant de rajouter de la latence / simuler des coupures
  réseau</a>

## Getting started 🚀

### Prérequis 🛠

- JDK 21
- Démon docker (Rancher Desktop / Docker Desktop / WSL)
- Activer les features preview du JDK dans l'IDE

### Démarrer l'application 🚄

Pour lancer l'application en mode dev, exécuter la méthode main de la classe suivante :<br/>
> src/test/java/fr/concurrency/training/StartTrainingApplication <br/>

Cette classe permet de monter automatiquement l'environnement nécessaire : <br/>

- Wiremock pour mocker les API REST
- Conteneur PostgreSQL avec Testcontainer

### Tests d'intégration 🐲

Un conteneur proxy Toxiproxy est présent afin de rajouter de la latence sur les appels à la base de données pour se rapprocher de la réalité.

## Littérature - liens utiles 📄

- https://dev.java/learn/new-features/virtual-threads/
- <a href="https://docs.oracle.com/en/java/javase/21/core/structured-concurrency.html#GUID-AA992944-AABA-4CBC-8039-DE5E17DE86DB">Documentation Structured Concurrency</a>
- https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/CompletableFuture.html
- https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/ExecutorService.html
- https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/Executors.html
- https://www.baeldung.com/java-completablefuture
- https://www.baeldung.com/java-completablefuture-allof-join
- https://www.baeldung.com/java-executor-service-tutorial
- <a href="https://www.youtube.com/watch?v=AQjTUxjMg78&list=PLuZ_sYdawLiUHU4E1i5RrFsRN_lQcgPwT&index=9"> 🎦 DevFest Nantes - José Paumard</a>
- <a href="https://www.youtube.com/watch?v=wx7t69DylsI"> 🎦 Devoxx FR - José Paumard / Rémi Forax - Loom, Structured Concurrency</a>
