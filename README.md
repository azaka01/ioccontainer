# Simple IOC Container

[![](https://jitpack.io/v/azaka01/ioccontainer.svg)](https://jitpack.io/#azaka01/ioccontainer)

A sample library to demonstrate inversion of control and its usage in a demo app.

## About

The library is written in Kotlin (1.5.x) and requires minimum Android API 21+.

Developed using Android Studio Arctic Fox (2020.3.1 Patch 1) with Gradle Wrapper `7.0.2` and AGP `7.0.1`

## Setup

1. Add this to the repositories block in `settings.gradle.kts`
```Kotlin
 repositories {
       ...
        maven(url = "https://jitpack.io")
    }
```
2. In the project `build.gradle.kts` add the following dependency
```Kotlin
dependencies {
    implementation("com.github.azaka01:ioccontainer:<version>")
```

## Usage

The API is fairly simple as only two methods suffice to add and create classes.

```Kotlin
fun <I : Any, C> bind(
        kInterface: KClass<I>,
        kClass: KClass<C>,
        classScope: ClassScope,
        properties: Map<String, Any> = emptyMap()
    ) where C : I 
```

```Kotlin
inline fun <reified T : Any> create() 
```

To use the Container in an app, the following steps are typically performed:


1. Create a new IOC Container 
```Kotlin 
SimpleIOCContainer()
```
2. Add bindings by supplying the interface and class that implements the interface. 
   Specify the scope as either `Factory` (which creates a new class instance) or `Singleton`
   Optionally inject properties to be used as parameters when creating the object
   
```Kotlin
SimpleIOCContainer().apply {
       bind(ConsumerA::class, ConsumerAImpl::class, ClassScope.Factory)
       bind(ConsumerB::class, ConsumerBImpl::class, ClassScope.Factory)
       bind(ConsumerC::class, ConsumerCImpl::class, ClassScope.Factory)
    }
```
3. Create an instance of any class for which bindings exist. 
The type in angle brackets can be either the interface or the actual class.
```Kotlin 
val consumerA = container.create<ConsumerA>()
```
## Unit tests

All the code in `simpleioclib` has unit test coverage.

Test classes are available in `TestClasses.kt`

<img width="552" alt="Screenshot 2021-08-29 at 22 39 26" src="https://user-images.githubusercontent.com/1098487/131266200-87776717-4938-4446-ad4e-37bbb068d8f4.png">

<img width="525" alt="Screenshot 2021-08-29 at 22 31 35" src="https://user-images.githubusercontent.com/1098487/131266216-e2b9dd39-bb19-47a2-b230-a1d9fb808ba9.png">


## Exceptions

```Kotlin
UnresolvedClassException
```
Thrown if all dependencies cannot be resolved

```Kotlin
CircularDependencyException
```
Thrown if a circular dependency is detected when resolving the bindings

## Sample App

The sample Android app demonstrates ViewModel injection using `SimpleIOCContainer`

`SimplePlayer.kt` has a hierarchy of player test classes. 

The `SimplePlayerService` is injected into the `PlayerIOCViewModel` and used to call player, ads and analytics related methods.

The injection methods in `DI.kt` do the following:
1. An instance of `SimpleIOCContainer` is created which receives bindings for `SimplePlayer` classes.
2. A view model instance is created 
```Kotlin
internal inline fun <reified T : Any> getViewModel(): T? =
    containerFactory()?.let { iocContainer ->
        runCatching<T> {
            iocContainer.create()
        }.onFailure {
            Timber.e("unable to create VM $it")
        }.getOrNull()
    }
```
3. The activity calls view model methods to control the player.
