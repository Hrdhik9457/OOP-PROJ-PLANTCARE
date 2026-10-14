# PlantCare — Java OOP Viva & Project Guide

This guide is prepared specifically to help you explain the **PlantCare** smart gardening system to your college professors and external examiners during your Object-Oriented Programming (OOP) practical viva.

---

## 1. Project Overview

- **Project Title:** PlantCare — Smart Plant Care & Gardening System
- **Core Technology:** Java 21, Spring Boot 3.4.5, Thymeleaf, Maven
- **Design Philosophy:** Demonstrating pure Object-Oriented Programming principles using an in-memory collection architecture (no complex external database required).

---

## 2. Java OOP Concepts Mapping (Viva Q&A)

### Q1: Where is **Encapsulation** implemented?
**Answer:**
- In the `Plant` abstract base class and all entities (`IndoorPlant`, `OutdoorPlant`, `CareTask`, `JournalEntry`).
- All state variables are declared `private` (e.g., `private double soilMoisture;`).
- Public getters and setters enforce strict validation rules. If invalid data is supplied (such as soil moisture `< 0` or `> 100`, or watering frequency `< 1`), the setter throws an `InvalidPlantDataException`.
- Internal logic like health score calculation (`getHealthStatus()`) and sensor status badges are encapsulated within domain methods rather than leaked to the UI controllers.

### Q2: Where is **Inheritance** implemented?
**Answer:**
- `Plant` is the abstract base superclass containing common plant attributes (id, name, species, lastWateredDate, soilMoisture, etc.).
- `IndoorPlant extends Plant`: Inherits all base characteristics and adds indoor-specific attributes like `LightRequirement`, `isAirPurifying`, and `idealRoom`.
- `OutdoorPlant extends Plant`: Inherits all base characteristics and adds outdoor-specific attributes like `SunExposure`, `isEdible`, and `season`.

### Q3: Where is **Abstraction** implemented?
**Answer:**
- `Plant` is declared `abstract` because a generic "Plant" cannot exist without a specific habitat context.
- Declares abstract methods:
  - `public abstract String getCareRecommendation();`
  - `public abstract String getPlantType();`
- Subclasses are forced to provide concrete implementations tailored to their specific biological environment.

### Q4: Where is **Polymorphism** demonstrated?
**Answer:**
- **Dynamic Method Dispatch (Runtime Polymorphism):**
  When iterating over a polymorphic collection `List<Plant>`:
  ```java
  for (Plant plant : plants) {
      String advice = plant.getCareRecommendation(); // Polymorphic call!
  }
  ```
  If `plant` is an `IndoorPlant`, it checks indoor lighting and room drafts. If it is an `OutdoorPlant`, it evaluates direct sun exposure, frost warnings, and seasonal mulching.
- The same method invocation produces different, context-appropriate behavior depending on the runtime instance type.

### Q5: What **Interfaces** did you design and why?
**Answer:**
- `Waterable`: Defines the watering contract (`water()`, `getWateringFrequencyDays()`, `needsWatering()`).
- `Monitorable`: Defines telemetry observation behavior (`getSoilMoisture()`, `getLightLevel()`, `getTemperature()`, `getHealthStatus()`).
- `Plant` implements both `Waterable, Monitorable`, guaranteeing that all botanical entities support watering cycles and environmental monitoring.

### Q6: Where is **Composition & Association** demonstrated?
**Answer:**
- **Composition / Association:**
  - `CareTask` contains an association with a `Plant` (via `plantId`, `plantName`, `plantEmoji`).
  - `JournalEntry` records developmental growth milestones tied to a `Plant`.
- **OOP Coordination:**
  When a user marks a `WATERING` task complete in `CareTaskService`, the service automatically calls `plantService.waterPlant(plantId)`, which resets the plant's watering schedule and updates its soil moisture.

### Q7: What **Collections** are used?
**Answer:**
- In-memory `ArrayList<Plant>`, `ArrayList<CareTask>`, and `ArrayList<JournalEntry>` are used.
- Java Stream API (`stream().filter()`, `sorted()`, `toList()`) is used for querying overdue tasks, species filtering, and computing environmental averages.

### Q8: How is **Exception Handling** implemented?
**Answer:**
- Custom unchecked exception `InvalidPlantDataException` extends `RuntimeException`.
- Thrown whenever data bounds are violated (e.g. invalid name length, impossible sensor values).
- Handled gracefully in controllers to flash friendly user error alerts without crashing the server.

### Q9: How does the **Community (Reddit-style)** module demonstrate OOP concepts?
**Answer:**
- **Composition:** `CommunityPost` owns a collection of `List<PostComment>` and a `Map<String, Integer>` of reaction emojis (`🌿`, `💚`, `💡`, `🌱`).
- **Encapsulation:** Voting rules (upvotes, downvotes, net score calculation) and algorithms (`getHotScore()`, `getTrendingScore()`) are encapsulated within `CommunityPost`.
- **Collections & Strategy Sorting:** `CommunityService` dynamically applies `Comparator` sorting pipelines for **Hot**, **New**, **Top**, and **Trending** streams.

---

## 3. Class Hierarchy Diagram

```
                 <<Interface>>                    <<Interface>>
                   Waterable                       Monitorable
                 [+water()]                     [+getSoilMoisture()]
                 [+needsWatering()]             [+getHealthStatus()]
                       ^                                ^
                       |                                |
                       +----------------+---------------+
                                        | implements
                                        |
                               +-----------------+
                               |  abstract Plant |
                               +-----------------+
                                       ^
                                       | extends
                       +---------------+---------------+
                       |                               |
               +---------------+               +----------------+
               |  IndoorPlant  |               |  OutdoorPlant  |
               +---------------+               +----------------+
               - lightRequirement              - sunExposure
               - isAirPurifying                - isEdible
               - idealRoom                     - season
```

---

## 4. How to Run the Application

### Method 1: Using IntelliJ IDEA (Recommended)
1. Open IntelliJ IDEA.
2. Click **File -> Open** and select the folder `plantcare`.
3. Locate `src/main/java/com/plantcare/PlantCareApplication.java`.
4. Right-click and choose **Run 'PlantCareApplication'** (or click the green ▶ play icon).
5. Open your browser and navigate to: **`http://localhost:8080`**

### Method 2: Command Line (PowerShell)
```powershell
$mvn = "C:\Program Files\JetBrains\IntelliJ IDEA 2026.1.4\plugins\maven\lib\maven3\bin\mvn.cmd"
& $mvn spring-boot:run -f pom.xml
```

---

## 5. Summary of Completed Pages

1. **Dashboard (`/`)**: High-level garden summary, quick counts, and plant health score.
2. **My Plants (`/plants`)**: Card gallery displaying individual plants, sensor gauges, and watering status.
3. **Plant Details (`/plants/{id}`)**: Deep dive showing polymorphic care advice, sensor bars, schedule, and OOP class inspector.
4. **Plant Form (`/plants/add`, `/plants/edit/{id}`)**: Dynamic add/edit form with Jakarta validation and indoor/outdoor field switching.
5. **Care Tasks (`/tasks`)**: Care calendar with tabs (Due Today, Overdue, Upcoming, Completed) and task creation.
6. **Smart Monitoring (`/monitoring`)**: Simulated environmental telemetry with on-demand refresh and academic integrity disclaimer.
7. **Growth Journal (`/journal`)**: Chronological growth timeline with developmental stage badges and plant filtering.
8. **Plant Library (`/library`)**: Species reference encyclopedia and taxonomy guide.
