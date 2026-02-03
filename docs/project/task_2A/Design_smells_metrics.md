# TASK 2A: DESIGN SMELLS ANALYSIS

Design smells represent architectural-level problems that affect maintainability, scalability, and testability. Unlike code smells reported by static analysis tools, design smells require interpretation of tool outputs combined with UML and dependency analysis.

## TOOLS USED
SonarQube (Code metrics: complexity, coupling, duplication)
Designite (Design smells)
CodeMR
Manual UML dependency analysis

## DESIGN SMELLS

### 1: GLOBAL STATE VIA STATIC SINGLETON FACTORY
Classification: Structural Design Smell
Severity: CRITICAL
Evidence:
WebloggerFactory maintains a static mutable WebloggerProvider.
Over 50 classes across UI, rendering, web services, and business layers call WebloggerFactory.getWeblogger().
SonarQube flags hidden dependencies and static access anti-patterns.
Why this is a design smell:
Introduces global mutable state.
Violates Dependency Inversion Principle.
Prevents unit testing without full application bootstrap.
Creates tight coupling across all layers.
UML Rationale:
All UI, rendering, and web service components directly depend on a single static factory, forming a star topology. This results in high coupling and zero substitutability.

### 2: GOD CLASS / BROKEN MODULARIZATION
Classification: Cohesion Design Smell
Severity: CRITICAL
Evidence:
Weblogger interface exposes 15+ manager accessors, lifecycle methods, URL strategy, and version metadata.
JPAWebloggerImpl injects more than 20 dependencies.
SonarQube and UML analysis show extremely high fan-out.
Why this is a design smell:
Violates Single Responsibility Principle.
Violates Interface Segregation Principle.
Changes to one manager ripple throughout the system.
Difficult to test due to excessive mocking requirements.
UML Rationale:
The Weblogger interface acts as a central hub controlling unrelated subsystems such as content, media, search, admin, and planet services.

### 3: STATIC CONFIGURATION UTILITY (IMPERATIVE ABSTRACTION)
Classification: Configuration Design Smell
Severity: HIGH
Evidence:
WebloggerConfig uses static initialization blocks.
30+ classes call static methods like WebloggerConfig.getProperty().
Over 50 magic configuration strings scattered across the codebase.
Why this is a design smell:
No type safety; configuration errors appear at runtime.
Global configuration state affects the entire JVM.
Configuration cannot be changed per test.
Static initialization order issues may cause startup failures.
Architectural Impact:
All layers are tightly coupled to a single static configuration source, preventing modularity and flexible configuration management.

### 4: ACTION CLASS PROLIFERATION (HIERARCHY SMELL)
Classification: Structural / Hierarchy Design Smell
Severity: HIGH
Evidence:
Around 25 Struts2 action classes exist for roughly 6 domain entities.
Separate classes exist for each CRUD operation.
Significant duplication of validation and workflow logic.
Why this is a design smell:
Class explosion reduces maintainability.
Poor cohesion among related behaviors.
Navigation and understanding of entity behavior becomes difficult.
Violates modern controller design practices.

### 5: FAT INTERFACES (INTERFACE SEGREGATION VIOLATION)
Classification: Interface Design Smell
Severity: HIGH
Evidence:
PlanetManager interface includes planet CRUD, group management, subscriptions, entries, aggregation, and feed generation.
SonarQube reports high interface complexity.
Why this is a design smell:
Clients depend on many unused methods.
Difficult to mock and test.
Interface contracts are unclear and unstable.

### 6: CROSS-PACKAGE TIGHT COUPLING (WEBLOGGER ↔ PLANET)
Classification: Dependency Design Smell
Severity: HIGH
Evidence:
JPAWebloggerImpl directly depends on PlanetManager.
Planet-related classes are referenced across weblogger business logic.
UML dependency analysis reveals cyclic dependencies.
Why this is a design smell:
Violates modular boundaries.
Prevents independent evolution of packages.
Increases ripple effects across subsystems.

### 7: GOD MANAGER CLASS
Classification: Cohesion Design Smell
Severity: MEDIUM
Evidence:
JPAWeblogEntryManagerImpl contains approximately 1394 lines of code.
Handles entry CRUD, comments, categories, tags, search, and paging logic.
SonarQube reports high complexity and low cohesion.
Why this is a design smell:
Multiple responsibilities combined into one class.
Hard to test and refactor.
High risk of regression during changes.

# TASK 2B: CODE METRICS ANALYSIS 

## TOOLS USED
SonarQube
CodeMR

## METRICS

### 1: EFFERENT COUPLING (Ce)
Definition:
Number of classes a given class depends on.
Tool:
SonarQube, manual CK calculation
Example:
JPAWeblogEntryManagerImpl
Ce = 18
Implications:
Exceeds critical threshold (>15).
Indicates strong coupling.
Refactoring becomes risky and expensive.

### 2: DEPTH OF INHERITANCE TREE (DIT)
Definition:
Maximum length from class to root of inheritance hierarchy.
Tool:
SonarQube
Measurement:
UIAction hierarchy has DIT = 3.
Implications:
Inheritance contracts are hard to understand.
Serialization and API issues propagate to all subclasses.
Approaches warning threshold for maintainability.

### 3: LINES OF CODE (LOC)
Tool:
SonarQube
Key measurements:
JPAWeblogEntryManagerImpl: ~1394 LOC
WeblogEntry: ~1031 LOC
DatabaseInstaller: ~900 LOC
Implications:
Exceeds recommended maintainability limits.
Strong indicator of God Class behavior.
Difficult to comprehend and test.

### 4: CYCLOMATIC COMPLEXITY (CC)
Definition:
Number of independent execution paths.
Tool:
SonarQube
Measurements:
Average CC per method: ~10
Some query builders exceed CC = 15
Implications:
High testing effort required.
Increased likelihood of defects.
Reduced code readability.

### 5: LACK OF COHESION OF METHODS (LCOM)
Definition:
Measures how closely methods relate to shared fields.
Tool:
Manual CK calculation
Example:
JPAWeblogEntryManagerImpl
LCOM ≈ 21%
Implications:
Indicates low cohesion.
Suggests class should be split into smaller components.
Supports God Class diagnosis.

### 6: COMMENT DENSITY
Definition:
Ratio of comment lines to code lines.
Tool:
PMD
Measurements:
Average comment density: 2–3%
Recommended range: 5–10%
Implications:
Poor readability and maintainability.
Higher onboarding cost for new developers.
Increased risk of API misuse.

# OVERALL QUALITY ASSESSMENT
Quality Risk Level: MEDIUM–HIGH
Key Risks:
Global static state prevents unit testing.
High coupling and low cohesion.
Architectural bottlenecks and cyclic dependencies.
Fragile inheritance and configuration mechanisms.
Metrics-Guided Refactoring Priorities:
Reduce coupling (Ce < 8)
Split God classes (LOC < 400)
Improve cohesion (LCOM > 40%)
Replace static utilities with dependency injection
Consolidate action hierarchies

# CONCLUSION
This analysis shows that although Apache Roller is functionally mature, its initial architecture suffers from critical design smells related to global state, over-centralization, and poor modularization. The selected metrics strongly justify refactoring and provide a clear, tool-supported roadmap for improving maintainability, testability, and architectural quality.

