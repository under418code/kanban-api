# 🗂️ Kanban API - Backend Spring Boot

API REST de gestion de tableaux Kanban, développée avec **Java 21**, **Spring Boot 4.1.1**, **Maven** et **PostgreSQL 18.4**.

Le projet est construit **étape par étape**, avec une architecture en couches claire, testée et extensible. Il sert aussi pour ma chaîne.

## 🔗 Liens

* 🎥 YouTube : [www.youtube.com/@Under418Code](https://www.youtube.com/@Under418Code)
* 💼 LinkedIn : [linkedin.com/in/vincent-bollot](https://www.linkedin.com/in/vincent-bollot/)

---

## 🛣️ Roadmap

- [x] Entités JPA (`Board`, `List`, `Card`)
- [x] Repositories Spring Data
- [x] Services + logique métier
- [x] Controllers REST
- [x] DTOs + validation
- [x] MapStruct + JsonNullable
- [x] Repositionnement (contrainte unique + double passe)
- [x] Tests à 3 niveaux
- [x] `@Transactional`
- [x] `@ControllerAdvice` (gestion d'erreurs centralisée)
- [ ] User + JWT (authentification)
- [ ] Docker + CI/CD
- [ ] Tests de concurrence (`@Version`)
- [ ] Pagination

---

## 🎯 Objectifs du projet

Gérer un tableau Kanban composé de :

* **Boards** → tableaux Kanban
* **Lists** → colonnes d'un tableau
* **Cards** → tâches présentes dans les colonnes

```text
Board
 ├── List
 │    ├── Card
 │    └── Card
 └── List
      └── Card
```

---

## 🚀 Technologies utilisées

| Technologie                | Rôle                                                    |
| -------------------------- | ------------------------------------------------------- |
| Java 21                    | Langage                                                 |
| Spring Boot 4.1.1          | Framework                                               |
| Maven                      | Gestion du projet et des dépendances                    |
| PostgreSQL 18.4            | Base de données                                         |
| Spring Web MVC             | API REST                                                |
| Spring Data JPA            | Persistance (Hibernate)                                 |
| Bean Validation            | Validation des entrées (`@Valid`)                       |
| MapStruct 1.6.3            | Mapping Entity ↔ DTO                                    |
| jackson-databind-nullable  | `JsonNullable` pour les `PATCH` partiels                |
| Spring Boot Actuator       | Supervision                                             |
| Spring Boot DevTools       | Rechargement automatique en développement               |
| JUnit, Mockito, H2         | Tests                                                   |
| Testcontainers             | Dépendances déclarées dans le `pom.xml`                 |

---

## 🏗️ Architecture

```text
Client / Frontend
       │
       ▼
  Controller  ──►  DTO (Request / Response)
       │
       ▼
    Service      (logique métier, @Transactional)
       │
       ▼
  Repository     (Spring Data JPA)
       │
       ▼
  PostgreSQL     (board / list / card)
```

La gestion des erreurs est transversale et centralisée dans `GlobalExceptionHandler` (`@RestControllerAdvice`).

### Structure

```text
src/main/java/com/undere418code/kanban_api
│
├── controller   → BoardController, ListController, CardController
├── dto          → Create*DTO, Update*DTO (JsonNullable), *DTO de réponse
├── entity       → BoardEntity, ListEntity, CardEntity
├── enums        → CardStatus (ACTIVE, ARCHIVED, DELETED)
├── exception    → GlobalExceptionHandler, ErrorResponse,
│                  NotFoundException, ValidationException, UnauthorizedException
├── mapper       → BoardMapper, ListMapper, CardMapper (MapStruct)
├── repository   → BoardRepository, ListRepository, CardRepository
├── service      → interfaces + implémentations (*ServiceImpl)
├── util         → FieldCheckUtils, JsonNullablePatch
└── KanbanApiApplication.java
```

---

## 🌐 Endpoints REST

### Boards — `/boards`

| Méthode  | Route          | Description               |
| -------- | -------------- | ------------------------- |
| `POST`   | `/boards`      | Créer un board            |
| `GET`    | `/boards`      | Lister tous les boards    |
| `GET`    | `/boards/{id}` | Récupérer un board        |
| `PATCH`  | `/boards/{id}` | Modifier un board         |
| `DELETE` | `/boards/{id}` | Supprimer un board        |

### Lists — `/lists`

| Méthode  | Route                    | Description                    |
| -------- | ------------------------ | ------------------------------ |
| `POST`   | `/lists`                 | Créer une liste dans un board  |
| `GET`    | `/lists/{id}`            | Récupérer une liste            |
| `GET`    | `/lists/board/{boardId}` | Lister les listes d'un board   |
| `PATCH`  | `/lists/{id}`            | Modifier / repositionner       |
| `DELETE` | `/lists/{id}`            | Supprimer une liste            |

### Cards — `/cards`

| Méthode  | Route                  | Description                    |
| -------- | ---------------------- | ------------------------------ |
| `POST`   | `/cards`               | Créer une carte dans une liste |
| `GET`    | `/cards/{id}`          | Récupérer une carte            |
| `GET`    | `/cards/list/{listId}` | Lister les cartes d'une liste  |
| `PATCH`  | `/cards/{id}`          | Modifier / repositionner       |
| `DELETE` | `/cards/{id}`          | Supprimer une carte            |

---

## 🗄️ Modèle PostgreSQL

Trois tables, identifiants `BIGSERIAL` :

```text
board (1) ──── (N) list (1) ──── (N) card
```

```sql
CREATE TABLE board (
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    description  TEXT,
    created_at   TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE list (
    id           BIGSERIAL PRIMARY KEY,
    board_id     BIGINT NOT NULL REFERENCES board(id) ON DELETE CASCADE,
    name         VARCHAR(100) NOT NULL,
    position     INT NOT NULL,
    created_at   TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (board_id, position)
);

CREATE TABLE card (
    id           BIGSERIAL PRIMARY KEY,
    list_id      BIGINT NOT NULL REFERENCES list(id) ON DELETE CASCADE,
    title        VARCHAR(200) NOT NULL,
    description  TEXT,
    position     INT NOT NULL,
    status       VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    due_date     DATE,
    created_at   TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (list_id, position)
);
```

* `ON DELETE CASCADE` : supprimer un board supprime ses listes, supprimer une liste supprime ses cartes.
* `UNIQUE (board_id, position)` et `UNIQUE (list_id, position)` : deux éléments d'un même parent ne peuvent pas avoir la même position.
* `status` est mappé sur l'enum Java `CardStatus` (`ACTIVE`, `ARCHIVED`, `DELETED`).
* `ddl-auto: none` : le schéma est géré côté base, pas par Hibernate.

> Un utilisateur PostgreSQL dédié (`kanban_api`) est utilisé par l'application, avec uniquement les droits `SELECT`, `INSERT`, `UPDATE`, `DELETE` et ceux sur les séquences. En production, les identifiants doivent être externalisés (variables d'environnement) et non écrits dans `application.yml`.

---

## 📍 Gestion des positions

Chaque liste et chaque carte possède une `position` (0, 1, 2…).

* **Création** : l'élément est ajouté à la fin (position = nombre d'éléments existants).
* **Déplacement** (`PATCH` avec `position`) : les éléments du parent sont réordonnés en **deux passes**.
  1. Positions temporaires négatives, pour éviter toute collision avec la contrainte `UNIQUE`.
  2. `flush`, puis positions finales `0..n`.
* **Suppression** : les positions des éléments suivants sont décalées de −1 pour ne laisser aucun trou.

---

## ✏️ PATCH partiel avec `JsonNullable`

Les DTOs `Update*DTO` utilisent `JsonNullable` pour distinguer un champ **absent** (inchangé) d'un champ **présent**.
Après application du patch, l'entité finale est validée (`FieldCheckUtils`) : un titre ne peut pas devenir vide, une date limite ne peut pas être dans le passé, etc.

Exemple :

```http
PATCH /cards/12
Content-Type: application/json

{ "title": "Configurer PostgreSQL", "position": 0 }
```

---

## ⚠️ Gestion des erreurs

`GlobalExceptionHandler` (`@RestControllerAdvice`) renvoie un JSON uniforme (`ErrorResponse`) :

```json
{ "message": "Board 42 not found" }
```

| Exception                          | Statut HTTP |
| ---------------------------------- | ----------- |
| `NotFoundException`                | 404         |
| `ValidationException`              | 400         |
| `MethodArgumentNotValidException`  | 400         |
| `UnauthorizedException`            | 401 (prévue pour la future couche sécurité) |
| `Exception` (fallback)             | 500         |

---

## 🧪 Tests

Tests à trois niveaux :

| Niveau       | Outils                          | Dossier                |
| ------------ | ------------------------------- | ---------------------- |
| Controllers  | `@WebMvcTest` + MockMvc         | `src/test/.../controller` |
| Services     | JUnit 5 + Mockito               | `src/test/.../service`    |
| Repositories | `@DataJpaTest` (H2 en mémoire)  | `src/test/.../repository` |

Lancer les tests :

```bash
./mvnw test
```

---

## ▶️ Lancer le projet

### Prérequis

* Java 21
* PostgreSQL avec une base `kanban_db` et un utilisateur `kanban_api`
* Le schéma SQL ci-dessus appliqué à la base

### Configuration

`src/main/resources/application.yml` :

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/kanban_db
    username: kanban_api
    password: <votre_mot_de_passe>
server:
  port: 8080
```

### Démarrage

Linux / macOS :

```bash
./mvnw spring-boot:run
```

Windows :

```bash
mvnw.cmd spring-boot:run
```

L'API est alors disponible sur `http://localhost:8080`.

---

## 🧠 Objectif pédagogique

Comprendre concrètement le chemin d'une donnée, de PostgreSQL jusqu'au client HTTP, couche par couche :

```text
Entity → Repository → Service → Controller → API REST
```

Ce projet sert à la chaîne [Under418Code](https://www.youtube.com/@Under418Code)
