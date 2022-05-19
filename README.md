[![](https://jitpack.io/v/ILIYANGERMANOV/ivy-frp.svg)](https://jitpack.io/#ILIYANGERMANOV/ivy-frp) [![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0) ![PRs welcome!](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)

# Ivy FRP

Ivy FRP is a Functional Reactive Programming framework for declarative-style programming for
Android. :rocket:

> Minimalistic and light-weight implementation of the [Ivy FRP Architecture](https://github.com/ILIYANGERMANOV/ivy-wallet/blob/main/docs/Developer-Guidelines.md).

_Recommendation: Use _it alongside Jetpack Compose_._

## Demo

### Imaginary Weather app :cloud::sunny::umbrella:

> TL;DR: You'll find the code of the entire weather app below. If you're already sold to use Ivy FRP => skip to Installation.

#### Data (boring)
```Kotlin
data class Temperature(
    val value: Float,
    val unit: TemperatureUnit
)

enum class TemperatureUnit {
    CELSIUS, FAHRENHEIT
}

data class UserPreference(
    val temperatureUnit: TemperatureUnit
)
```

#### IO (boring)
```Kotlin
data class WeatherResponse(
    val temperature: Temperature
)

interface WeatherService {
    @GET("/api/weather")
    suspend fun getWeather(): WeatherResponse
}

interface UserPreferenceService {
    @GET("/preference/temp-unit")
    suspend fun getTempUnit(): UserPreference

    @POST("/preference/temp-unit")
    suspend fun updateTempUnit(
        @Query("unit") temperatureUnit: TemperatureUnit
    )
}
```

#### Actions
**ConvertTempAct.kt**
```Kotlin
class ConvertTempAct @Inject constructor() : FPAction<ConvertTempAct.Input, Temperature>() {

    override suspend fun Input.compose(): suspend () -> Temperature =
        this asParamTo ::convertValue then { convertedValue ->
            Temperature(convertedValue, toUnit)
        }

    private fun convertValue(input: Input): Float = with(input.temperature) {
        if (unit == input.toUnit) value else {
            when (input.toUnit) {
                TemperatureUnit.CELSIUS -> fahrenheitToCelsius(value)
                TemperatureUnit.FAHRENHEIT -> celsiusToFahrenheit(value)
            }
        }
    }

    //X°F = (Y°C × 9/5) + 32
    private fun celsiusToFahrenheit(celsius: Float): Float = (celsius * 9 / 5) + 32

    //X°C = (Y°F - 32) / (9/5)
    private fun fahrenheitToCelsius(fahrenheit: Float): Float = (fahrenheit - 32) / (9 / 5)

    data class Input(
        val temperature: Temperature,
        val toUnit: TemperatureUnit
    )
}
```

**CurrentTempAct.kt**
```Kotlin
class CurrentTempAct @Inject constructor(
    private val weatherService: WeatherService,
    private val convertTempAct: ConvertTempAct
) : FPAction<TemperatureUnit, Res<String, Temperature>>() {
    override suspend fun TemperatureUnit.compose(): suspend () -> Res<String, Temperature> = tryOp(
        operation = weatherService::getWeather
    ) mapSuccess { response ->
        ConvertTempAct.Input(
            temperature = response.temperature,
            toUnit = this //TemperatureUnit
        )
    } mapSuccess convertTempAct mapError {
        "Failed to fetch weather: ${it.message}"
    }
}
```

**UserPreferencesAct.kt**
```Kotlin
class UserPreferenceAct @Inject constructor(
    private val userPreferenceService: UserPreferenceService
) : FPAction<Unit, Res<String, UserPreference>>() {
    override suspend fun Unit.compose(): suspend () -> Res<String, UserPreference> = tryOp(
        operation = userPreferenceService::getTempUnit
    ) mapError {
        "Failed to fetch user's preference: ${it.message}"
    }
}
```

**UpdateUserPreferencesAct.kt**
```Kotlin
class UpdateUserPreferenceAct @Inject constructor(
    private val userPreferenceService: UserPreferenceService
) : FPAction<TemperatureUnit, Res<String, Unit>>() {
    override suspend fun TemperatureUnit.compose(): suspend () -> Res<String, Unit> = tryOp(
        operation = this asParamTo userPreferenceService::updateTempUnit
    ) mapError {
        "Failed to update user preference: ${it.message}"
    }
}
```

#### ViewModel

**WeatherState.kt**
```Kotlin
sealed class WeatherState {
    object Loading : WeatherState()

    data class Error(val errReason: String) : WeatherState()

    data class Success(
        val tempUnit: TemperatureUnit,
        val temp: Float
    ) : WeatherState()
}
```

**WeatherEvent.kt**
```Kotlin
sealed class WeatherEvent {
    object LoadWeather : WeatherEvent()

    data class UpdateTempUnit(val unit: TemperatureUnit) : WeatherEvent()
}
```

**WeatherViewModel.kt**
```Kotlin
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val userPreferenceAct: UserPreferenceAct,
    private val updateUserPreferenceAct: UpdateUserPreferenceAct,
    private val currentTempAct: CurrentTempAct,
) : FRPViewModel<WeatherState, WeatherEvent>() {
    override val _state: MutableStateFlow<WeatherState> = MutableStateFlow(WeatherState.Loading)

    override suspend fun handleEvent(event: WeatherEvent): suspend () -> WeatherState =
        when (event) {
            is WeatherEvent.LoadWeather -> loadWeather()
            is WeatherEvent.UpdateTempUnit -> updateTempUnit(event)
        }

    private suspend fun loadWeather(): suspend () -> WeatherState = suspend {
        updateState { WeatherState.Loading }
        Unit
    } then userPreferenceAct mapSuccess {
        it.temperatureUnit //loadTempFor() expects TemperatureUnit
    } then ::loadTempFor

    private suspend fun updateTempUnit(
        event: WeatherEvent.UpdateTempUnit
    ): suspend () -> WeatherState = suspend {
        updateState { WeatherState.Loading }
        event.unit
    } then updateUserPreferenceAct mapSuccess {
        event.unit
    } then ::loadTempFor

    private suspend fun loadTempFor(tempRes: Res<String, TemperatureUnit>) =
        tempRes.lambda() thenIfSuccess currentTempAct thenInvokeAfter {
            when (it) {
                is Res.Ok -> WeatherState.Success(
                    temp = it.data.value,
                    tempUnit = it.data.unit
                )
                is Res.Err -> WeatherState.Error(errReason = it.error)
            }
        }
}
```

#### UI

**WeatherScreen.kt**
```Kotlin
data class WeatherScreen(
    val title: String
) : Screen

@Composable
fun BoxWithConstraintsScope.WeatherScreen(screen: WeatherScreen) {
    FRP<WeatherState, WeatherEvent, WeatherViewModel>(
        initialEvent = WeatherEvent.LoadWeather
    ) { state, onEvent ->
        UI(title = screen.title, state = state, onEvent = onEvent)
    }
}

@Composable
private fun UI(
    title: String,
    state: WeatherState,

    onEvent: (WeatherEvent) -> Unit
) {
    //UI goes here.....
    //When Celsius is clicked:
    // onEvent(WeatherEvent.UpdateTempUnit(TemperatureUnit.CELSIUS))
}
```

Find the full sample **[here](sample/src/main/java/com/ivy/sample/demo/weather)**.

### Key Features

- Function composition using `then`. _(supporting suspend functions, `Action & FPAction)_
- `FPAction`: declaritive-style "use-case" which can be composed.
- `FRPViewModel`: functional-reactive ViewModel implementation,
  see [Ivy FRP Architecture](https://github.com/ILIYANGERMANOV/ivy-wallet/blob/main/docs/Developer-Guidelines.md)
  .
- `@Composable FRP<State, Event>(){ UI() }`: functional-reactive UI implementation in Jetpack
  Compose.
- `Res.Ok / Res.Err` result type: monadic result type supporting success and error composition.
    - `thenIfSuccess`: calls the next function only on success (OK)
    - `mapSuccess`: maps only the success type if the result is OK
    - `mapError`: maps only the error type if the result is Err

- (optional) `NavigationRoot` + `Navigation`: navigation component for Jetpack Compose
    - :warning: to use it with proper back handling you must **override onBackPressed()**

**if you use `Navigation`:**

```Kotlin
//required only for "NavigationRoot" and "Navigation"
override fun onBackPressed() {
    if (!navigation.onBackPressed()) {
        super.onBackPressed()
    }
}
```

## Installation

### Gradle KTS

#### 1. Add `maven(url = "https://jitpack.io")` to repositories.

**settings.gradle.kts** _(at the top)_

```Kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

//...
```

**build.gradle.kts (buildSrc)** _(right below "plugins")_

```Kotlin
plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    maven(url = "https://jitpack.io")
}

//...
```

#### 2. Add `com.github.ILIYANGERMANOV:ivy-frp:0:0:0` dependency.

[![](https://jitpack.io/v/ILIYANGERMANOV/ivy-frp.svg)](https://jitpack.io/#ILIYANGERMANOV/ivy-frp) _<
-- latest version_

**build.gradle.kts (app)**

```Kotlin
implementation("com.githubILIYANGERMANOV:ivy-frp:0.0.0")
```

> Replace 0.0.0 with the latest version from **[Jitpack](https://jitpack.io/#ILIYANGERMANOV/ivy-frp)**.

### Gradle

#### 1. Add `maven(url = "https://jitpack.io")` to repositories.

#### 2. Add `com.github.ILIYANGERMANOV:ivy-frp:0:0:0` dependency.

[![](https://jitpack.io/v/ILIYANGERMANOV/ivy-frp.svg)](https://jitpack.io/#ILIYANGERMANOV/ivy-frp) _<
-- latest version_

**build.gradle.kts (app)**

```Groovy
implementation 'com.githubILIYANGERMANOV:ivy-frp:0.0.0'
```

## Docs

### _:construction: WIP... :construction:_

For samples and usage in a real-world scenario please refer to
the **[Ivy Wallet's GitHub repo](https://github.com/ILIYANGERMANOV/ivy-wallet/tree/develop)**.