<h1 align="center">Bienvenue dans le projet java-concurrency-training 👋</h1>

> Atelier de formation autour de la programmation concurrente en java.<br />

## Objectifs

- Comprendre ce qu'est la programmation asynchrone
- Comprendre ce qu'est la programmation concurrente
- Pourquoi en avons-nous besoin
- Quand en avons-nous besoin
- Masteriser la programmation concurrente et asynchrone en java
- Les pièges des virtual threads

### Démonstrations et exercices

- Optimiser les temps de réponse d'une API
- Optimiser le nombre d'appels concurrent
- Optimiser les resources allouées à l'application
  - Réduction des pod sur GKE
  - Temps de traitements optimisé
  - Parallélisation des traitements asynchrones
- Utilisation du profiler Intellij
  - Identification des tâches bloquantes

### Technologies

Cette API est développée avec les technologies suivantes :

- `Java 21 :` le langage

- `SpringBoot 3.2 :` le cadre de développement.

- `Wiremock :` <a href="https://wiremock.org/docs/">Documentation</a>

- `TestContainers`: <a href="https://testcontainers.com/">PostgreSQL conteneurisé</a>

- `Instancio`: <a href="https://www.instancio.org/user-guide/">Générateur d'objets Java</a>

## Getting started

### Tester les endpoints
- Démarrer Wiremock depuis src/test/java/fr/concurrency/training/StartWiremock
- Démarrer l'application en mode dev

### Tests d'intégration
Lors de l'exécution des tests d'intégration, il n'y a pas besoin de démarrer Wiremock, il se lance automatiquement en mode embedded.