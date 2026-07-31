# Kanban API — Backend Spring Boot (Architecture de base)

Backend développé en **Java 21.0.4**, **Spring Boot 4.1.0** et **PostgreSQL 18.4**.

Cette première étape pose une architecture propre, maintenable et extensible avant l'ajout des fonctionnalités métier (CRUD, sécurité, pagination, etc.).

## 🚀 Ce dépôt contient

- Structure complète des packages backend
- Gestion centralisée des erreurs
- Exceptions métier dédiées
- Configuration PostgreSQL via `application.yml`
- Spring Boot DevTools pour le rechargement automatique

---

## 🏁 Créer un nouveau projet Spring Boot avec VS Code

### 1. Palette de commandes VS Code
**Ctrl + Shift + P**  
→ `Spring Initializr: Create a Maven Project`

### 2. Paramètres du projet
- Java 21  
- Spring Boot 4.1.0  
- Maven  
- Jar  
- GroupId : com.undere418code  
- ArtifactId : kanban-api  

### 3. Dépendances
- Spring Web  
- Spring Data JPA  
- PostgreSQL Driver  
- Validation (optionnel)  
- Lombok (optionnel)  
- **Spring Boot DevTools** (coché directement dans Spring Initializr)

### 4. Lancer le projet
#### Via VS Code  
Ouvrir `KanbanApiApplication.java` → **Run**

#### Via Maven  
```bash
./mvnw spring-boot:run
```

## 📦 Structure du projet

```text
src/main/java/com/undere418code/kanban_api
│
├── controller      → Endpoints REST (à venir)
├── dto             → Objets de transfert (à venir)
├── entity          → Entités persistées (à venir)
│
├── exception       → Architecture des erreurs
│   ├── ErrorResponse.java
│   ├── GlobalExceptionHandler.java
│   ├── NotFoundException.java
│   ├── UnauthorizedException.java
│   └── ValidationException.java
│
├── repository      → Interfaces JPA (à venir)
├── service         → Logique métier (à venir)
│
└── KanbanApiApplication.java
```

---

## ⚠️ Architecture des exceptions

Le backend utilise une gestion d'erreurs professionnelle :

- `ValidationException` → Erreurs de validation métier
- `NotFoundException` → Ressource introuvable
- `UnauthorizedException` → Accès non autorisé
- `ErrorResponse` → Format standardisé des réponses d'erreur
- `GlobalExceptionHandler` → Centralise toutes les exceptions et renvoie des réponses cohérentes

### Objectifs

- API lisible et cohérente
- Erreurs uniformes pour le frontend
- Architecture maintenable
- Base solide pour les futures fonctionnalités (CRUD, sécurité, pagination)

---

## ⚙️ Configuration (`application.yml`)

Le fichier `application.yml` contient la configuration de base :

- Connexion PostgreSQL 18.4
- Port de l'application
- Paramètres Spring Boot 4.1.0
- Configuration Hibernate / JPA

---

## 🧩 Pourquoi cette architecture ?

Cette structure permet :

- Une séparation claire des responsabilités
- Une base solide pour les futurs contrôleurs et services
- Une gestion d'erreurs professionnelle
- Une montée en compétences progressive et structurée
- Une transmission pédagogique du fonctionnement du backend