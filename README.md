[![](https://jitpack.io/v/ILIYANGERMANOV/ivy-frp.svg)](https://jitpack.io/#ILIYANGERMANOV/ivy-frp) [![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0) ![PRs welcome!](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)

# Ivy FRP

Ivy FRP is a Functional Reactive Programming framework for declarative-style programming for
Android. :rocket:

> Minimalistic and light-weight implementation of the [Ivy FRP Architecture](https://github.com/ILIYANGERMANOV/ivy-wallet/blob/main/docs/Developer-Guidelines.md).

_Recommendation: Use _it alongside Jetpack Compose_._

## Demo

_:construction: WIP :construction:_

### Key Features

- Function composition using `then`. _(supporting suspend functions, `Action & FPAction)_
- `FPAction`: declaritive-style "use-case" which can be composed.
- `FRPViewModel`: functional-reactive ViewModel implementation,
  see [Ivy FRP Architecture](https://github.com/ILIYANGERMANOV/ivy-wallet/blob/main/docs/Developer-Guidelines.md)
  .
- `@Composable FRP<State, Event>(){ UI() }`: functional-reactive UI implementation in Jetpack
  Compose.
- `Res.Ok / Res.Err` result type: monadic result type supporting success and error composition.
    - `thenR`: calls the next function only on success (OK)
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

## Usage

### _:construction: WIP... :construction:_

For samples and usage in a real-world scenario please refer to
the **[Ivy Wallet's GitHub repo](https://github.com/ILIYANGERMANOV/ivy-wallet/tree/develop)**.