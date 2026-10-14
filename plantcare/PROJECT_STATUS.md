# PlantCare — Project Status

## Current Phase
Phase 9 — Gardening Community (Reddit-Style) ✅ COMPLETE

## Phase History
- Phase 1 — Foundation ✅
- Phase 2 — OOP Domain Model ✅
- Phase 3 — Plant Dashboard ✅
- Phase 4 — Plant Management ✅
- Phase 5 — Care Calendar & Tasks ✅
- Phase 6 — Simulated Smart Monitoring ✅
- Phase 7 — Growth Journal & Final Polish ✅
- Phase 8 — Progressive Web App (PWA) ✅
- Phase 9 — Gardening Community (Reddit-Style) ✅

## Completed Features
- **Phase 1 Foundation**: Spring Boot 3.4.5 + Maven, Thymeleaf, layout fragments, full CSS theme, responsive landing page.
- **Phase 2 OOP Model**: `Plant` (abstract), `IndoorPlant` & `OutdoorPlant` (inheritance & polymorphism), `Waterable` & `Monitorable` (interfaces), `InvalidPlantDataException`.
- **Phase 3 Dashboard & List**:
  - `/plants`: Responsive card grid showing health badge, watering status, mini sensor bars, and dynamic counts.
  - `/plants/{id}`: Plant detail page showing polymorphic care recommendation, sensor gauges, watering schedule, type-specific details, and viva helper table.
  - `/`: Dynamic dashboard quick stats powered by `PlantService`.
- **Phase 4 Plant Management (Full CRUD)**:
  - `PlantForm`: Form DTO with Jakarta validation annotations (`@NotBlank`, `@Min`, `@Max`, `@DecimalMin`, etc.).
  - `/plants/add` (GET & POST): Dynamic indoor/outdoor conditional form fields, validation error display, polymorphic instantiation.
  - `/plants/edit/{id}` (GET & POST): Form pre-filling via `PlantService.toForm()` and field updates.
  - `/plants/delete/{id}` (POST): Safe in-memory deletion.
  - `/plants/water/{id}` (POST): Quick-action watering that updates last-watered date and boosts soil moisture.
- **Phase 5 Care Calendar & Tasks (`/tasks`)**:
  - `TaskType` enum: WATERING, FERTILIZING, REPOTTING, PRUNING.
  - `CareTask` entity demonstrating composition and association with `Plant`.
  - Filter bar tabs: All Tasks, Due Today, Overdue, Upcoming, Completed with live counts.
  - Checkbox toggle (`POST /tasks/toggle/{id}`): Mark complete/pending. Completing a watering task automatically waters the plant in `PlantService` (OOP Coordination).
  - Add task form (`POST /tasks/add`): Schedule new care actions for any plant.
- **Phase 6 Simulated Smart Monitoring (`/monitoring`)**:
  - `EnvironmentTelemetry` DTO capturing snapshot readings, explicitly stamped with `simulated = true`.
  - Prominent academic integrity disclaimer stating telemetry is simulated in Java memory and not connected to physical microcontrollers.
  - Environmental overview metrics: Average soil moisture, average ambient temp, average light level, and active alerts count.
  - Individual plant telemetry cards with real-time gauges and formatted timestamps.
  - On-demand simulated refresh (`POST /monitoring/refresh`): dynamically varies soil moisture, temperature, and light levels within realistic bounds.
- **Phase 7 Growth Journal, Library & Polish (`/journal`, `/library`)**:
  - `JournalEntry` entity for developmental milestones and observations.
  - `JournalService` with in-memory collection, chronological sorting, and plant filtering.
  - Interactive timeline view with developmental stage badges and status mood pills.
  - Botanical Plant Library (`/library`) providing complete taxonomy profiles for all plants.
  - `VIVA_GUIDE.md`: Comprehensive college viva questions and answers covering all OOP concepts.
  - `README.md`: Setup and execution guide.
- **Phase 8 Progressive Web App (PWA)**:
  - `manifest.json`: Web app manifest configured with name, icons, standalone display mode, background & theme colors.
  - `icon.svg`: High-resolution vector icon for desktop and mobile home screen installation.
  - `sw.js`: Service worker with pre-caching for application shell, static assets, and navigation fallback.
  - `offline.html` & `/offline` route: Dedicated offline fallback page displayed when network is unavailable.
  - `main.js`: Automatic Service Worker registration on load.
- **Phase 9 Gardening Community (`/community`)**:
  - Reddit-style feed with vertical voting column (▲ / ▼) and net score counter.
  - Sorting algorithms: **Hot** (time decay score), **New** (chronological), **Trending** (reaction & reply velocity), **Top** (highest net upvotes).
  - Categorical topic chips: "Indoor Care", "Pest & Disease", "Soil & Fertilizer", "Propagation", "Plant ID".
  - One-click reactions (`🌿`, `💚`, `💡`, `🌱`), Repost counter, and Share link copy to clipboard.
  - Discussion threads (`/community/post/{id}`) with threaded answers/replies and answer upvoting.
  - "Ask a Question" query publishing form.

## Important Files
| File | Role |
|------|------|
| `model/Plant.java` | Abstract base with encapsulation, sensor statuses, helper flags |
| `model/IndoorPlant.java` | Subclass with indoor care logic, light requirement, air purifying |
| `model/OutdoorPlant.java` | Subclass with outdoor care logic, sun exposure, edible flag |
| `model/CareTask.java` | Entity for plant care tasks & schedules (Composition) |
| `model/JournalEntry.java` | Growth observation milestones tied to plants |
| `model/EnvironmentTelemetry.java` | Simulated sensor telemetry data structure |
| `model/CommunityPost.java` | Reddit-style forum post with voting, reactions, comments (Composition) |
| `model/PostComment.java` | Threaded answer/reply entity for community questions |
| `service/PlantService.java` | In-memory `ArrayList<Plant>` storage with CRUD & form conversions |
| `service/CareTaskService.java` | Manages tasks collection and coordinates with `PlantService` |
| `service/MonitoringService.java` | Computes environment averages and generates simulated fluctuations |
| `service/JournalService.java` | In-memory growth observations with chronological sorting |
| `service/CommunityService.java` | Manages community posts, sorting strategies (Hot/New/Top/Trending), comments |
| `controller/CommunityController.java` | MVC controller for `/community` feed, discussion threads, votes, reactions |
| `templates/community/index.html` | Reddit-style community feed with voting, sorting, and post form |
| `templates/community/detail.html` | Individual discussion thread with answers and reply form |
| `static/manifest.json` | Web App Manifest for PWA installation |
| `static/sw.js` | Service Worker caching and offline routing |
| `static/images/icon.svg` | App icon for mobile/desktop install |
| `VIVA_GUIDE.md` | Complete Java OOP viva preparation guide with Q&A |
| `README.md` | Project documentation and running instructions |

## Verification
- `mvn compile` → BUILD SUCCESS (26 source files)
- Community routes verified live (200 OK):
  - `GET /community`
  - `GET /community?sort=new`
  - `GET /community?sort=trending`
  - `GET /community?sort=top`
  - `GET /community?tag=Indoor+Care`
  - `GET /community/post/1`
- Community mutation flows verified (302 Redirect):
  - Upvote post (`/community/vote/1`)
  - Add reaction (`/community/react/1`)
  - Repost (`/community/repost/1`)
  - Add answer/reply (`/community/comment/1` -> verified rendered)
  - Ask new question (`/community/add` -> verified rendered)

## How to Run
```powershell
$mvn = "C:\Program Files\JetBrains\IntelliJ IDEA 2026.1.4\plugins\maven\lib\maven3\bin\mvn.cmd"
& $mvn spring-boot:run -f C:\Users\hrdhi\.gemini\antigravity\scratch\plantcare\pom.xml
```
Visit: http://localhost:8080/community
