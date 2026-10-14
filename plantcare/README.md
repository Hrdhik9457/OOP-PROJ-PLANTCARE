# PlantCare — Smart Plant Care & Gardening System

PlantCare is a server-rendered Java web application built for an Object-Oriented Programming (OOP) college project. It demonstrates fundamental and advanced OOP concepts including encapsulation, inheritance, polymorphism, abstraction, interfaces, composition, collections, and custom exception handling.

---

## Tech Stack

- **Language:** Java 21 (LTS)
- **Framework:** Spring Boot 3.4.5
- **Template Engine:** Thymeleaf
- **Build Tool:** Maven 3.9
- **Storage:** In-memory collections (`ArrayList`, `HashMap`)
- **Frontend:** Semantic HTML5, Custom CSS3 (Forest green, Sage, Cream, Terracotta theme), Vanilla JavaScript

---

## Key Features

- **Plant Collection Dashboard (`/plants`)**: Visual overview of all indoor and outdoor plants with health badges and watering indicators.
- **Polymorphic Care Advice (`/plants/{id}`)**: Dynamic care recommendations generated differently for indoor vs outdoor species at runtime.
- **Plant Management (`/plants/add`, `/plants/edit/{id}`, `/plants/delete/{id}`)**: Full CRUD operations with validation.
- **Care Calendar & Tasks (`/tasks`)**: Task tracking with status filtering (Overdue, Today, Upcoming, Completed) and automatic plant watering synchronization.
- **Smart Environmental Monitoring (`/monitoring`)**: Simulated soil moisture, temperature, and light telemetry with on-demand refresh and academic integrity disclaimers.
- **Growth Journal (`/journal`)**: Chronological milestone log with developmental stages and plant-specific filtering.
- **Botanical Library (`/library`)**: Comprehensive species profiles and growing requirements.

- **Community Forum (`/community`)**: Reddit-style community feed with upvoting/downvoting, reaction emojis (🌿, 💚, 💡, 🌱), sort filters (Hot, New, Trending, Top), link sharing, and threaded Q&A discussions.
- **PWA & Offline Support**: Progressive Web App capabilities (`manifest.json`, Service Worker, offline fallback page).

---

## What You Need on the Other PC (Prerequisites)

You only need **one single thing** installed:
- **Java Development Kit (JDK 17 or JDK 21+)**
  - Download from [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [Eclipse Temurin](https://adoptium.net/).
  - To verify, open terminal / PowerShell and type: `java -version`
- **NO database installation needed!** (MySQL/PostgreSQL/MongoDB are NOT required because the application uses standard in-memory Java OOP collections).
- **NO separate Maven install needed!** The project includes the Maven Wrapper (`mvnw` / `mvnw.cmd`).

---

## How to Run on Another PC

### Method 1: IntelliJ IDEA (Easiest & Recommended)
1. Download or clone this repository to the PC.
2. Open **IntelliJ IDEA** → Click **File** → **Open...**
3. Select the folder containing `pom.xml`.
4. IntelliJ will automatically detect it as a Maven project and download any dependencies in a few seconds.
5. In the project explorer on the left, navigate to:
   `src/main/java/com/college/plantcare/PlantCareApplication.java`
6. Right-click `PlantCareApplication.java` and click **Run 'PlantCareApplication'** (or click the green **▶ Play** button at the top right).
7. Open your browser and navigate to:
   👉 **http://localhost:8080**

---

### Method 2: Command Line using Maven Wrapper (No Maven Install Required)

Open Command Prompt or PowerShell in the project directory (where `pom.xml` is located):

#### On Windows:
```cmd
mvnw.cmd spring-boot:run
```
*(Or in PowerShell: `.\mvnw.cmd spring-boot:run`)*

#### On macOS / Linux:
```bash
./mvnw spring-boot:run
```

Once the terminal outputs `Started PlantCareApplication in X.XXX seconds`, open:
👉 **http://localhost:8080**

---

### Method 3: Standard Maven (If Maven is already installed)

```bash
mvn spring-boot:run
```

---

## Common Issues & Fixes

1. **Port 8080 is already in use:**
   If another application is using port 8080, open `src/main/resources/application.properties` and change:
   ```properties
   server.port=8081
   ```
   Then visit `http://localhost:8081`.

2. **Java version error:**
   Ensure you have JDK 17 or JDK 21+ installed:
   ```bash
   java -version
   ```
   If using IntelliJ, ensure **Project SDK** under *File → Project Structure → Project* is set to JDK 17 or 21.

---

## College OOP Viva Reference

See [`VIVA_GUIDE.md`](./VIVA_GUIDE.md) for detailed explanations, answers to common examiner questions, class hierarchy diagrams, and code snippets demonstrating each OOP pillar.

