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