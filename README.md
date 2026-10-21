## 1. Project structure

The app follows a layered structure. These layer names are common in Android and Clean Architecture projects, but Android does not require these exact folders. Choose the structure that fits the size and needs of an app.

The section numbers also suggest an implementation order: **(1)** plan the structure, **(2)** define domain models, repository contracts, and use cases, **(3)** implement those contracts in the data layer, then **(4)** build the presentation layer on top of the use cases. Defining the domain contract first gives the data layer a clear target.

```text
app/src/main/java/com/medioka/guideline/app/
├── data/                         # External data sources and implementations
│   ├── di/                       # Data-layer dependency registration
│   ├── mapper/                   # API/error models ↔ domain conversion
│   ├── remote/
│   │   ├── request/              # Request/query representations
│   │   └── response/             # API response DTOs
│   └── repository/               # Repository implementations
├── domain/                       # App rules and business-facing contracts
│   ├── di/                       # Domain dependency registration
│   ├── error/                    # Domain-level failures
│   ├── model/                    # Weather and forecast entities
│   ├── payload/                  # Inputs passed to domain operations
│   ├── repository/               # Repository interfaces
│   └── usecase/                  # Individual app operations
├── presentation/                 # Compose UI, screen state, and navigation
│   ├── common/
│   │   ├── components/           # Shared UI components
│   │   ├── extension/            # Presentation helpers
│   │   ├── location/             # Android location and permission handling
│   │   ├── model/                # UI-facing models and state
│   │   └── previews/             # Preview sample data
│   ├── detail/                   # Forecast detail screen
│   ├── home/                     # Home screen and its presentation logic
│   ├── main/                     # Main destinations and tab navigation
│   ├── random/                   # Mock Random screen
│   └── settings/                 # Mock Settings screen
├── di/                           # App-level dependency setup
├── theme/                        # Compose colors, typography, and theme
├── MainActivity.kt               # Android activity entry point
└── GuidelineApplication.kt       # Application and Koin startup
```

### Common vs. optional conventions

- **Common layer names:** `data`, `domain`, and `presentation` are a widely used way to separate responsibilities. They are architectural conventions, not Android requirements.
- **Often optional:** a `domain` layer can be unnecessary in a very small app. `di`, `mapper`, `error`, `payload`, `remote/request`, and `remote/response` are also organizational choices; they help as responsibilities grow but are not required folder names.
- **Common, but flexible:** `model` is used for data types, but the model's purpose depends on its layer. Domain models describe app concepts; presentation models are shaped for the UI; response DTOs match the server API.
- **Project-specific:** `presentation/common` groups UI pieces shared by multiple screens. Other projects may place shared components or platform helpers elsewhere.

## 2. Domain layer

The **domain layer** describes what the app does without depending on Android, Compose, Retrofit, or a particular database. It contains business rules and contracts used by other layers.

- **`domain/model/` — domain entities.** Types such as `Weather`, `CurrentWeather`, and `DailyWeather` describe weather in terms meaningful to the app. They are not shaped around a particular JSON response or screen layout.
- **`domain/repository/` — repository interfaces.** `WeatherRepository` says what data the app needs (for example, `getWeather`) without saying how it is fetched. The data layer supplies the implementation. This keeps use cases independent of Retrofit and makes the contract straightforward to fake in tests.
- **`domain/usecase/` — app operations.** `GetWeatherUseCase` represents the operation of obtaining weather. It validates coordinates and delegates data access through `WeatherRepository`. A use case is useful when an operation has a clear purpose or rules; not every trivial method needs its own use case.
- **`domain/payload/` — operation inputs.** `WeatherPayload` carries the coordinates needed to request weather. A payload represents input to an operation; it is distinct from the resulting `Weather` entity.
- **`domain/error/` — domain-level failures (optional package).** `WeatherException` gives callers meaningful failure categories without exposing Retrofit or JSON exceptions.
- **`domain/di/` — dependency registration (optional package).** Registers domain services, such as the weather use case, with Koin.

Typical direction of use:

```text
Presentation → Use case → Repository interface
```

## 3. Data layer

The **data layer** connects the app to external or local data sources and implements the domain repository contracts. It translates between external formats and domain models.

- **`data/remote/WeatherApi.kt` — API contract.** Retrofit annotations describe the HTTP endpoint and its parameters.
- **`data/remote/request/` — request representations (optional package).** `ForecastRequest` prepares the query values expected by the weather service.
- **`data/remote/response/` — response DTOs (optional package).** These types mirror the JSON returned by the API. Keeping them separate prevents server-specific fields from becoming the app's domain model.
- **`data/mapper/` — conversions (optional package).** `ForecastMapper` converts API response data into domain weather models. `WeatherErrorMapper` translates infrastructure failures into domain-level errors.
- **`data/repository/` — implementations.** `WeatherRepositoryImpl` calls the API, maps its response, and translates failures. It implements the `WeatherRepository` interface from the domain layer.
- **`data/di/` — dependency registration (optional package).** Provides the API, Retrofit, OkHttp, and repository implementation to Koin. Network-specific setup such as timeouts and the debug Chucker interceptor lives here.

Typical data flow:

```text
Use case → WeatherRepository → WeatherRepositoryImpl → WeatherApi
                                             ↓
                                  response DTO → domain model
```

## Dependency injection with Koin

Dependency injection (DI) lets a class receive the objects it needs instead of constructing them itself. Koin stores these definitions in modules and resolves dependencies when the app requests them.

```kotlin
val weatherModule = module {
    singleOf(::GetWeatherUseCase)
    singleOf(::WeatherRepositoryImpl) bind WeatherRepository::class
    factoryOf(::SomeServiceImpl) bind SomeService::class
}
```

- **`singleOf(::Type)`** registers a class or constructor reference as a singleton. Koin creates it once and reuses it; constructor parameters are resolved from the graph.
- **`factoryOf(::Type)`** also resolves constructor parameters, but creates a new instance each time it is requested. `singleOf` and `factoryOf` can both bind an implementation to an interface with `bind<Interface>()` (the `SomeService` example is illustrative).
- **`single { ... }` / `factory { ... }`** register custom creation logic when a constructor reference is not enough. Use **`get()`** inside the block to resolve another registered dependency, such as `single { WeatherRepositoryImpl(get()) }` or `factory { SomeServiceImpl(get()) }`.
- **`viewModelOf(::HomeViewModel)`** registers a ViewModel with its constructor dependencies.
- **`module { ... }`** groups related definitions. Modules can be included in an app-level module, then started with Koin when the application starts.

In this project, the data module provides networking and repository implementations, the domain module provides use cases, and the presentation module provides screen ViewModels. Koin connects these objects so, for example, a ViewModel can receive a use case, which receives a repository interface implementation.

## 4. Presentation layer: MVVM and MVI

The **presentation layer** renders screens and translates user interaction into application actions. Compose components should primarily display state and report interactions; formatting and UI-specific models belong here rather than in the domain layer.

### Shared presentation areas

- **`presentation/home/`, `detail/`, `main/`, etc.** organize code by screen or feature.
- **`presentation/common/components/`** contains reusable Compose elements such as loading, error, and weather-background UI.
- **`presentation/common/model/`** contains models intended for rendering, such as `CurrentWeather`, `DailyForecast`, and `WeatherUiState`. These can differ from domain models to suit screen needs.
- **`presentation/home/HomeUiMapper.kt`** converts domain weather into UI models, including display formatting.
- **`presentation/common/location/`** handles Android-specific location and permission behavior, which is kept out of the domain layer.
- **Navigation** connects screen destinations; screen-specific navigation data belongs near the screen that uses it.

### MVVM

In MVVM, the ViewModel owns screen state and exposes methods for UI actions. `HomeScreen` observes its `StateFlow` and calls methods such as `onRefresh()` or `onRetry()`.

- **ViewModel dependencies:** prefer injecting use cases, such as `GetWeatherUseCase`, rather than repositories directly. This keeps business rules in the domain layer and makes ViewModels consistent about how they request data.
- **Actions:** the ViewModel commonly has several named methods (`onRefresh()`, `onRetry()`, `handleLocationResult()`, and so on). Each handles an action and updates state.
- **State:** this example uses `MutableStateFlow` internally and exposes `StateFlow<WeatherUiState>`. The state is relatively small and describes the main screen modes: `Loading`, `Success`, or `Error`.
- **View:** Compose renders the current state and forwards callbacks to the matching ViewModel method.

Typical flow:

```text
User action → onRefresh() / onRetry() → ViewModel work → new UI state
```

### MVI

In MVI, UI actions enter the ViewModel through one `onIntent()` method. `HomeContract.kt` groups the Home screen's state, intents, and effects.

- **State (`HomeState`):** one immutable snapshot of the screen, with fields such as loading, refreshing, current weather, forecast, and error. The ViewModel updates it; Compose renders it.
- **Intent (`HomeIntent`):** an action sent from the UI to the ViewModel, for example `Refresh` or `Retry`. These inputs are also often called events.
- **Effect (`HomeEffect`):** a one-time instruction from the ViewModel to the UI, for example request a permission, navigate, or show a Snackbar. It is not lasting screen state.
- **ViewModel:** handles every intent in `onIntent()`, calls use cases, updates the single `HomeState`, and emits effects when the UI must do one-time work.
- **View:** sends intents and renders state. It performs effects that require Android or UI APIs.

Typical flow:

```text
User action → HomeIntent → HomeViewModel → HomeState
                                      ↘ HomeEffect → one-time UI action
```

Both patterns use a ViewModel and observable state. MVVM typically has a smaller set of state variants and separate action methods; MVI collects the screen's data in one state object and routes inputs through intents.
