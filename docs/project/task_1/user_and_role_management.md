## User and Role Management System

Basic Classes involved:

`RollerPermission` is an abstract base class for all Roller permissions.  
It extends `java.security.Permission` and standardizes how permissions store and manipulate their *actions*:

- Forces subclasses to implement `setActions(String)` and `getActions()`.
- Provides utility methods to:
  - Convert the comma‑separated actions string to/from a `List<String>`.
  - Check if a permission has a given action or a set of actions.
  - Add or remove actions (merging action sets).
  - Check if the permission is “empty” (no actions).

Concrete permissions (`GlobalPermission`, `WeblogPermission`, etc.) inherit this logic and only need to define how they store the `actions` string and how implication works.

`GlobalPermission` models **application‑wide permissions for a user**.

- It extends `RollerPermission` and stores a set of global actions in `actions`.  
- The allowed actions are:
  - `LOGIN` – can log in and edit profile  
  - `WEBLOG` – can log in and do weblogging  
  - `ADMIN` – full site‑wide admin

**Construction**

- From a `User`: looks up the user’s roles via `WebloggerFactory.getWeblogger().getUserManager()`, reads `role.action.<role>` mappings from `WebloggerConfig`, and builds the union of all implied actions.
- From a `List<String>` (optionally plus a `User`): directly uses the given actions.

**Behavior**

- `implies(Permission perm)` checks whether this global permission is strong enough to cover another permission:
  - `ADMIN` implies everything.
  - `WEBLOG` implies all non‑admin `RollerPermission`s.
  - `LOGIN` only implies basic login/profile actions.
- It also provides standard `toString()`, `equals()`, `hashCode()`, and getters/setters for the `actions` string.

`ObjectPermission` is an abstract base class for **permissions tied to a specific domain object** (e.g., a weblog).

- It extends `RollerPermission` and adds common state:
  - `id` (UUID), `userName`
  - `objectType` (currently unused/commented), `objectId`
  - `pending` flag
  - `dateCreated`
  - `actions` (the permission’s action string)
- It provides:
  - Constructors calling the `RollerPermission` constructor.
  - Standard getters/setters for all fields.
  - Concrete implementations of `setActions(String)` and `getActions()` required by `RollerPermission`.

Concrete subclasses (like `WeblogPermission`) reuse these fields/behaviour to represent permissions on particular objects.


## 1) Summary of the class diagram

The class diagram for these three classes captures:

* **`User`** — domain entity (POJO) representing an account. Holds identity, credentials, profile attributes, and small behaviour: `resetPassword(...)` and permission check helpers (`hasGlobalPermission(s)`).
* **`UserRole`** — simple role-assignment entity mapping a `userName` → `role`. Lightweight POJO used to record global roles like `admin` or `editor`.
* **`WeblogPermission`** — permission object scoped to a specific weblog. It is an `ObjectPermission`/`RollerPermission` subclass (implements semantics for weblog-level actions such as `POST`, `EDIT_DRAFT`, `ADMIN`) and is used by the permission subsystem and persisted by the `UserManager` implementation.

Key relationships shown in the diagram:

* `User` — **1..*** `UserRole` (a user may have many roles).
* `UserRole` — references the user by `userName` (string) rather than a direct object reference.
* `WeblogPermission` — applies to a `Weblog` (its `objectId` is typically the weblog handle) and inherits from the permission hierarchy (`ObjectPermission` → `RollerPermission`).
* `User` uses the permission objects (creates `GlobalPermission` and calls `UserManager.checkPermission(...)`).

---

## 2) Responsibilities (per class)

**User**

* Store user information (id, username, email, screen name, password hash).
* Provide convenience actions:

  * `resetPassword(newPassword)` — delegates to `RollerContext.getPasswordEncoder()` for hashing.
  * `hasGlobalPermissions(...)` — builds a `GlobalPermission` and asks `UserManager` whether the user is allowed the action(s).

**UserRole**

* Represent a role assignment with fields: `id`, `userName`, `role`.
* Equality/hash based on `userName` + `role`.
* Used by role-management code (grant/revoke/list roles).

**WeblogPermission**

* Represent weblog-scoped permissions (which actions are allowed for a given user on a given weblog).
* Track pending/confirmed state (in the superclass `ObjectPermission`).
* Used by `UserManager` to grant/confirm/revoke weblog access and by `checkPermission` to evaluate access at runtime.

---

## 3) Design observations & smells (concise)

**Encapsulation / Associations**

* `UserRole` references user via `userName` `String` rather than `User` object → *deficient encapsulation*. This leaks identity handling and complicates object graphs / ORM navigation.
* `WeblogPermission` references weblog by handle/id (string) instead of a typed `Weblog` association in the POJO → similar encapsulation issue.

**Abstraction / Stringly-typed**

* Several parts of the permission system use comma-separated strings and configuration keys (e.g., mapping `role.action.<role>` to a list of actions). This is *stringly-typed* and fragile. Enums or typed classes would be safer.

**Modularization / God service**

* The `UserManager` implementation (not one of the three files but visible in the system) is large and holds many responsibilities (users, roles, weblog permissions), which makes the permission flow more coupled and harder to test in isolation.

**Practical consequences**

* Harder to write unit tests for domain behaviour that currently depends on `UserManager` (global factory usage is common in the codebase).
* Identity-as-string decisions require extra DB lookups or in-memory cache to map username → id (which the JPA implementation does).

---

## 4) Strengths (what’s good)

* **Clear separation** between domain objects (POJOs) and permission objects — permissions are explicit objects, not ad-hoc checks.
* **Permission hierarchy** (global vs weblog) allows expressing site-wide roles (`admin`, `weblog`, `login`) and weblog-scoped actions independently.
* `WeblogPermission` supports pending/confirm workflows (invite/confirm) — good model for real-world workflows.










