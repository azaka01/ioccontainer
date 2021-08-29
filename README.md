# Simple IOC Container

[![](https://jitpack.io/v/azaka01/ioccontainer.svg)](https://jitpack.io/#azaka01/ioccontainer)

A sample library to demonstrate inversion of control and its usage in a demo app.

## About

The library is written in Kotlin (1.5.x) and requires minimum Android API 21+.

Developed using Android Studio Arctic Fox (2020.3.1 Patch 1) with Gradle Wrapper `7.0.2` and AGP `7.0.1`

## Usage

1. Create a new IOC Container `SimpleIOCContainer()` 
2. Add bindings by supplying the interface and class that implements the interface. 
   Specify the scope as either `Factory` (which creates a new class instance) or `Singleton`
   Optionally inject parameters to be used when creating the object
   
```
SimpleIOCContainer().apply {
       bind(ConsumerA::class, ConsumerAImpl::class, ClassScope.Factory)
       bind(ConsumerB::class, ConsumerBImpl::class, ClassScope.Factory)
       bind(ConsumerC::class, ConsumerCImpl::class, ClassScope.Factory)
    }
```
3. Create an instance of any class for which bindings exist. 
The type in angle brackets can be either the interface or the actual class.
```
val consumerA = container.create<ConsumerA>()
```

## Exceptions

```
UnresolvedClassException
```
Thrown if all dependencies cannot be resolved

```
CircularDependencyException
```
Thrown if a circular dependency is detected when resolving the bindings

## Sample App

