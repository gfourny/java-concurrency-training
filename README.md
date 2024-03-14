<h1 align="center">Bienvenue dans le projet java-concurrency-training 👋</h1>

> Atelier de formation autour de la programmation concurrente en java.<br />

## Objectifs 🎯

- Comprendre ce qu'est la programmation asynchrone
- Comprendre ce qu'est la programmation concurrente
- Pourquoi en avons-nous besoin
- Quand en avons-nous besoin
- Masteriser la programmation concurrente et asynchrone en java
- Les pièges des virtual threads

### Démonstrations et exercices 🎬

- Optimiser les temps de réponse d'une API
- Optimiser le nombre d'appels concurrent
- Optimiser les resources allouées à l'application
    - Réduction des pod sur GKE
    - Temps de traitements optimisé
    - Parallélisation des traitements asynchrones
- Utilisation du profiler Intellij
    - Identification des tâches bloquantes

### Technologies 🔭

Cette API est développée avec les technologies suivantes :

- `Java 21 :` le langage

- `SpringBoot 3.2 :` le cadre de développement.

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