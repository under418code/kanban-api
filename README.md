# 🗂️ Kanban API - Backend Spring Boot

Backend REST développé avec **Java 21.0.4**, **Spring Boot 4.1.0**, **Maven** et **PostgreSQL 18.4**.

Ce projet a pour objectif de construire progressivement une **API Kanban complète**, avec une architecture claire, maintenable et extensible.

La première phase consiste à mettre en place les fondations techniques du backend avant d'implémenter progressivement les fonctionnalités métier : persistance des données, CRUD, validation, gestion des erreurs, sécurité, pagination, etc.

---

## 🎯 Objectifs du projet

L'objectif est de construire une API backend permettant de gérer un tableau Kanban composé de :

* **Boards** → tableaux Kanban
* **Lists** → colonnes d'un tableau
* **Cards** → tâches présentes dans les colonnes

L'architecture est volontairement construite **étape par étape** afin de comprendre le rôle de chaque couche d'une application Spring Boot.

### Architecture cible

```text
Client / Frontend
       │
       ▼
   Controller
       │
       ▼
     DTO
       │
       ▼
    Service
       │
       ▼
   Repository
       │
       ▼
     JPA
       │
       ▼
 PostgreSQL
```

La gestion des erreurs est transversale et centralisée grâce à `GlobalExceptionHandler`.

---

# 🚀 Technologies utilisées

| Technologie          | Version / rôle                            |
| -------------------- | ----------------------------------------- |
| Java                 | 21.0.4                                    |
| Spring Boot          | 4.1.0                                     |
| Maven                | Gestion du projet et des dépendances      |
| PostgreSQL           | 18.4                                      |
| Spring Web           | Création de l'API REST                    |
| Spring Data JPA      | Persistance des données                   |
| Hibernate            | ORM                                       |
| Validation           | Validation des données                    |
| Lombok               | Réduction du code boilerplate             |
| Spring Boot DevTools | Rechargement automatique en développement |
| VS Code              | Environnement de développement            |

---

# 🏁 Créer un nouveau projet Spring Boot avec VS Code

## 1. Ouvrir Spring Initializr

Dans VS Code :

```text
Ctrl + Shift + P
```

Puis :

```text
Spring Initializr: Create a Maven Project
```

---

## 2. Paramètres du projet

Sélectionner :

* **Java 21**
* **Spring Boot 4.1.0**
* **Maven**
* **Jar**

### Identifiants Maven

```text
GroupId    : com.undere418code
ArtifactId : kanban-api
```

---

## 3. Dépendances

Les dépendances principales sélectionnées dans Spring Initializr :

* **Spring Web**
* **Spring Data JPA**
* **PostgreSQL Driver**
* **Validation**
* **Lombok**
* **Spring Boot DevTools**

Certaines dépendances pourront être ajoutées ou retirées au fur et à mesure de l'évolution du projet.

---

# ▶️ Lancer le projet

## Depuis VS Code

Ouvrir :

```text
KanbanApiApplication.java
```

Puis cliquer sur :

```text
Run
```

---

## Avec Maven

Linux / macOS :

```bash
./mvnw spring-boot:run
```

Windows :

```bash
mvnw.cmd spring-boot:run
```

---

# 🎯 Modèle fonctionnel

L'application repose sur trois concepts principaux :

* **Board** → tableau Kanban
* **List** → colonne d'un tableau
* **Card** → tâche présente dans une colonne

La hiérarchie fonctionnelle est la suivante :

```text
Board
 ├── List
 │    ├── Card
 │    ├── Card
 │    └── Card
 │
 ├── List
 │    ├── Card
 │    └── Card
 │
 └── List
      └── Card
```

---

# 🗄️ Modèle PostgreSQL

Le schéma de la base de données est actuellement composé de trois tables :

```text
board
list
card
```

Les tables utilisent PostgreSQL avec des identifiants `BIGSERIAL`.

```text
┌──────────────────────┐
│        board         │
├──────────────────────┤
│ id          BIGSERIAL│
│ name        VARCHAR  │
│ description TEXT     │
│ created_at  TIMESTAMP│
│ updated_at  TIMESTAMP│
└──────────┬───────────┘
           │
           │ 1
           │
           │ N
┌──────────▼───────────┐
│         list         │
├──────────────────────┤
│ id          BIGSERIAL│
│ board_id    BIGINT   │
│ name        VARCHAR  │
│ position    INT      │
│ created_at  TIMESTAMP│
│ updated_at  TIMESTAMP│
└──────────┬───────────┘
           │
           │ 1
           │
           │ N
┌──────────▼───────────┐
│         card         │
├──────────────────────┤
│ id          BIGSERIAL│
│ board_id    BIGINT   │
│ list_id     BIGINT   │
│ title       VARCHAR  │
│ description TEXT     │
│ position    INT      │
│ status      VARCHAR  │
│ due_date    DATE     │
│ created_at  TIMESTAMP│
│ updated_at  TIMESTAMP│
└──────────────────────┘
```

---

# 🧱 Table `board`

La table `board` représente le tableau Kanban principal.

```sql
CREATE TABLE board (
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    description  TEXT,
    created_at   TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP NOT NULL DEFAULT NOW()
);
```

### Champs

| Champ         | Type         | Description             |
| ------------- | ------------ | ----------------------- |
| `id`          | BIGSERIAL    | Identifiant unique      |
| `name`        | VARCHAR(100) | Nom du tableau          |
| `description` | TEXT         | Description optionnelle |
| `created_at`  | TIMESTAMP    | Date de création        |
| `updated_at`  | TIMESTAMP    | Date de modification    |

Le champ `id` est la **clé primaire** du board.

---

# 📑 Table `list`

La table `list` représente une colonne appartenant à un board.

```sql
CREATE TABLE list (
    id           BIGSERIAL PRIMARY KEY,
    board_id     BIGINT NOT NULL,
    name         VARCHAR(100) NOT NULL,
    position     INT NOT NULL,
    created_at   TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP NOT NULL DEFAULT NOW()
);
```

Chaque liste possède une référence vers son board :

```text
list.board_id → board.id
```

La relation est donc :

```text
Board 1 ────────── N List
```

### Contrainte de clé étrangère

```sql
FOREIGN KEY (board_id)
REFERENCES board(id)
ON DELETE CASCADE
```

Cela signifie que lorsqu'un board est supprimé, ses listes sont également supprimées.

### Position

Chaque liste possède une position dans son board :

```text
(board_id, position)
```

Cette combinaison est unique grâce à :

```sql
UNIQUE (board_id, position)
```

Deux listes d'un même board ne peuvent donc pas avoir la même position.

Exemple :

```text
Board #1

position 0 → TODO
position 1 → IN PROGRESS
position 2 → DONE
```

---

# 🃏 Table `card`

La table `card` représente une tâche Kanban.

```sql
CREATE TABLE card (
    id             BIGSERIAL PRIMARY KEY,
    board_id       BIGINT NOT NULL,
    list_id        BIGINT NOT NULL,
    title          VARCHAR(200) NOT NULL,
    description    TEXT,
    position       INT NOT NULL,
    status         VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    due_date       DATE,
    created_at     TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (board_id) REFERENCES board(id) ON DELETE CASCADE,
    FOREIGN KEY (list_id)  REFERENCES list(id)  ON DELETE CASCADE,
    UNIQUE (list_id, position)
);
```

### Champs principaux

| Champ         | Type         | Description                        |
| ------------- | ------------ | ---------------------------------- |
| `id`          | BIGSERIAL    | Identifiant unique                 |
| `board_id`    | BIGINT       | Board auquel appartient la carte   |
| `list_id`     | BIGINT       | Liste contenant la carte           |
| `title`       | VARCHAR(200) | Titre de la tâche                  |
| `description` | TEXT         | Description optionnelle            |
| `position`    | INT          | Position de la carte dans la liste |
| `status`      | VARCHAR(50)  | État de la carte                   |
| `due_date`    | DATE         | Date limite optionnelle            |
| `created_at`  | TIMESTAMP    | Date de création                   |
| `updated_at`  | TIMESTAMP    | Date de modification               |

---

# 🔗 Relations de `Card`

Une carte possède deux références :

```text
card.board_id → board.id
card.list_id  → list.id
```

Cela permet de connaître directement :

* le board de la carte ;
* la liste dans laquelle elle se trouve.

La relation principale est :

```text
Board
  │
  └── List
       │
       └── Card
```

La présence de `board_id` directement dans `card` permet également d'identifier rapidement le board auquel appartient la carte sans devoir reconstruire systématiquement cette information à partir de `list`.

La cohérence entre les relations est toutefois une responsabilité qui devra être prise en compte dans la **couche Service**.

---

# 📍 Position des cartes

Comme pour les listes, chaque carte possède une position.

La contrainte :

```sql
UNIQUE (list_id, position)
```

garantit qu'une même liste ne peut pas contenir deux cartes ayant la même position.

Exemple :

```text
TODO

position 0 → Créer le backend
position 1 → Configurer PostgreSQL
position 2 → Créer les repositories
```

Lorsqu'une carte est déplacée, sa position pourra être recalculée par la couche métier.

---

# 🗑️ Suppression en cascade

Le schéma utilise `ON DELETE CASCADE`.

## Board → List

```text
DELETE Board
      ↓
DELETE Lists associées
```

## List → Card

```text
DELETE List
      ↓
DELETE Cards associées
```

## Board → Card (référence directe)

`card.board_id` référence également `board(id)` avec `ON DELETE CASCADE`. En pratique, la suppression d'un board déclenche déjà la suppression de ses listes, qui déclenche elle-même la suppression des cartes — la cascade directe `board → card` est donc redondante avec `board → list → card`, mais elle reste nécessaire pour garantir l'intégrité si jamais une carte se retrouvait orpheline de liste.

Cela permet de conserver automatiquement l'intégrité des données lors de la suppression d'un élément parent.

---

# 👤 Utilisateur PostgreSQL

Le script SQL crée également un utilisateur dédié à l'application :

```text
kanban_api
```

Cet utilisateur reçoit les permissions nécessaires pour travailler avec la base de données.

Les droits accordés comprennent notamment :

```text
SELECT
INSERT
UPDATE
DELETE
```

ainsi que les droits nécessaires sur les séquences PostgreSQL utilisées par les colonnes `BIGSERIAL`.

L'objectif est d'éviter que l'application utilise directement un compte PostgreSQL administrateur.

> En environnement de développement, le mot de passe utilisé dans le script peut être simplifié. En production, les identifiants doivent être externalisés et protégés.

---

# 🧩 Correspondance SQL ↔ Java

Les tables PostgreSQL seront représentées côté Java par les entités JPA :

```text
PostgreSQL                 Java

board              →       BoardEntity
list               →       ListEntity
card               →       CardEntity
```

Structure :

```text
com.undere418code.kanban_api.entity

├── BoardEntity.java
├── ListEntity.java
└── CardEntity.java
```

Les annotations JPA permettront ensuite de faire le mapping entre les classes Java et les tables PostgreSQL.

Exemple conceptuel :

```text
@Entity
   ↓
@Table(name = "board")
   ↓
BoardEntity
   ↓
PostgreSQL
board
```

---

# 🏗️ Architecture backend

Le projet suit une architecture en couches :

```text
┌──────────────────────────────┐
│          Controller           │
│       API REST / HTTP        │
└──────────────┬───────────────┘
               ↓
┌──────────────────────────────┐
│            DTO               │
│     Request / Response       │
└──────────────┬───────────────┘
               ↓
┌──────────────────────────────┐
│           Service            │
│        Logique métier        │
└──────────────┬───────────────┘
               ↓
┌──────────────────────────────┐
│         Repository           │
│       Spring Data JPA        │
└──────────────┬───────────────┘
               ↓
┌──────────────────────────────┐
│         PostgreSQL           │
│   board / list / card        │
└──────────────────────────────┘
```

La gestion des erreurs est transversale :

```text
Controller / Service / Repository
              ↓
    GlobalExceptionHandler
              ↓
        ErrorResponse
```

---

# ⚠️ Gestion des exceptions

Le backend possède une architecture dédiée aux erreurs :

```text
exception
│
├── ErrorResponse.java
├── GlobalExceptionHandler.java
├── NotFoundException.java
├── UnauthorizedException.java
└── ValidationException.java
```

### `ValidationException`

Utilisée lorsqu'une règle métier ou une validation n'est pas respectée.

### `NotFoundException`

Utilisée lorsqu'une ressource demandée n'existe pas.

### `UnauthorizedException`

Prévue pour gérer les accès non autorisés lors de l'ajout de la sécurité.

### `ErrorResponse`

Standardise le format des réponses d'erreur.

### `GlobalExceptionHandler`

Centralise le traitement des exceptions afin de retourner des réponses HTTP cohérentes.

---

# 📦 Structure actuelle

```text
src/main/java/com/undere418code/kanban_api
│
├── controller
│   └── → Endpoints REST
│
├── dto
│   └── → Objets Request / Response
│
├── entity
│   ├── BoardEntity.java
│   ├── CardEntity.java
│   └── ListEntity.java
│
├── exception
│   ├── ErrorResponse.java
│   ├── GlobalExceptionHandler.java
│   ├── NotFoundException.java
│   ├── UnauthorizedException.java
│   └── ValidationException.java
│
├── repository
│   └── → Spring Data JPA
│
├── service
│   └── → Logique métier
│
└── KanbanApiApplication.java
```

---

# 🛣️ Roadmap

## ✅ Étape 1 - Architecture de base

* [x] Création du projet Spring Boot
* [x] Java 21
* [x] Spring Boot
* [x] Maven
* [x] PostgreSQL
* [x] Configuration `application.yml`
* [x] Architecture des packages
* [x] Gestion centralisée des exceptions
* [x] Création du schéma PostgreSQL
* [x] Création des entités :

  * [x] `BoardEntity`
  * [x] `ListEntity`
  * [x] `CardEntity`

---

## 🔜 Étape 2 - Repository

Prochaine étape : mettre en place les repositories Spring Data JPA.

```text
repository
│
├── BoardRepository
├── ListRepository
└── CardRepository
```

Objectif :

```text
BoardEntity
     ↓
BoardRepository
     ↓
PostgreSQL
```

Même principe pour `ListEntity` et `CardEntity`.

Cette étape permettra de commencer à interagir réellement avec la base de données depuis l'application Spring Boot.

---

## 🔜 Étape 3 - Service

Mise en place de la logique métier :

```text
Controller
     ↓
Service
     ↓
Repository
```

Les services seront notamment responsables de la gestion des règles métier liées aux boards, listes et cartes.

---

## 🔜 Étape 4 - Controller

Création des endpoints REST :

```text
/api/boards
/api/lists
/api/cards
```

---

## 🔜 Étape 5 - DTO & Validation

Ajout des :

* Request DTO
* Response DTO
* validations
* règles métier
* gestion des erreurs de validation

---

## 🔜 Étape 6 - CRUD

Implémentation complète :

```text
CREATE
READ
UPDATE
DELETE
```

---

## 🔜 Étape 7 - Fonctionnalités avancées

* Pagination
* Tri
* Recherche
* Filtres
* Gestion des positions
* Déplacement des cartes
* Gestion des statuts

---

## 🔜 Étape 8 - Sécurité

Ajout progressif :

* Authentification
* Autorisation
* Gestion des utilisateurs
* Protection des endpoints

---

# 🧠 Objectif pédagogique

Le projet est construit progressivement afin de comprendre le fonctionnement d'un backend Spring Boot moderne.

Chaque couche possède une responsabilité précise :

```text
Entity
  ↓
Repository
  ↓
Service
  ↓
Controller
  ↓
API REST
```

Cette progression permet de comprendre concrètement le chemin d'une donnée depuis PostgreSQL jusqu'au client HTTP.

---

# 📌 État actuel

Le socle technique est maintenant en place :

```text
                 ┌──────────────┐
                 │ PostgreSQL   │
                 │              │
                 │ board        │
                 │ list         │
                 │ card         │
                 └──────▲───────┘
                        │
                  Prochaine étape
                        │
                 ┌──────┴───────┐
                 │  Repository  │
                 └──────▲───────┘
                        │
                 ┌──────┴───────┐
                 │    Entity    │
                 │              │
                 │ BoardEntity   │
                 │ ListEntity    │
                 │ CardEntity    │
                 └──────────────┘
```

### 🚀 Next step

**Créer les `Repository` Spring Data JPA et établir la première interaction entre les `Entity` et PostgreSQL.**
