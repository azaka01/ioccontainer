package com.intsoftdev.simpleioclib

import timber.log.Timber
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

@Suppress("UNCHECKED_CAST")
class SimpleIOCContainer {

    /**
     * Collection of classes in the IOC container
     */
    private val containerClasses: ContainerClasses = mutableListOf()

    /**
     * Collection of cached singletons which get instantiated
     */
    private val singletonsCache = mutableMapOf<KClass<*>, Any>()

    /**
     * Track classes for circular dependency detection.
     */
    private var resolvedClasses = mutableSetOf<KClass<*>>()

    /**
     * Binds an interface to a class so it can be injected as dependency when creating an object.
     * @param kInterface The interface to bind
     * @param kClass The concrete class which implements the interface
     * @param classScope Scope of the binding
     * @param properties The properties to inject into [kClass]
     */
    fun <I : Any, C> bind(
        kInterface: KClass<I>,
        kClass: KClass<C>,
        classScope: ClassScope,
        properties: Map<String, Any> = emptyMap()
    ) where C : I {
        if (kInterface == kClass) {
            throw UnresolvedClassException("Binding to the same instance of ${kInterface.qualifiedName} not allowed")
        }
        containerClasses.insert(
            interfaceClass = kInterface,
            instanceClass = kClass,
            classScope = classScope,
            properties = properties
        )
    }

    /**
     * Creates an object its dependencies.
     * The type is provided within the angle brackets.
     * @return The type T which is specified
     */
    inline fun <reified T : Any> create() = create(T::class)

    /**
     * Entry point for the object creation.
     */
    @PublishedApi
    internal fun <T : Any> create(kClass: KClass<T>): T {
        // clear the circular dependencies before a new create
        resolvedClasses.clear()

        val resolvedClass = resolveBinding(kClass)

        return if (containerClasses.isSingleton(resolvedClass) && resolvedClass in singletonsCache)
            singletonsCache[resolvedClass] as T
        else {
            createInternal(resolvedClass)
        }
    }

    /**
     * @param kClass the class which is going to be created
     * @return The requested type with all the dependencies injected
     */
    private fun <T : Any> createInternal(kClass: KClass<*>): T {
        val resolvedKClass = resolveBinding(kClass)

        if (resolvedKClass in resolvedClasses) {
            Timber.e("Detected a circular dependency: ${kClass.qualifiedName}")
            throw CircularDependencyException("Detected a circular dependency: ${kClass.qualifiedName}")
        }

        resolvedClasses.add(resolvedKClass)

        val constructor = resolvedKClass.primaryConstructor ?: resolvedKClass.constructors.first()
        val params = constructor.parameters
        val args = params
            .associateWith { param ->
                containerClasses.getProperties(resolvedKClass)?.let { properties ->
                    val value = properties[param.name]
                    if (param.isOptional) {
                        /**
                         * optional value so return to the caller lambda
                         */
                        return@associateWith value
                    }
                    value
                } ?: createInternal<T>(param.type.jvmErasure) // recurse to build the dependency graph
            }.filterValues { it != null }

        return instantiate(constructor, args)
    }

    /**
     * Uses Kotlin reflection to call the constructor of type T.
     * https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-function/
     * @param constructor The callable constructor to call
     * @param args A map of parameter types and values used when instantiating the constructor
     */

    private fun <T : Any> instantiate(constructor: KFunction<*>, args: Map<KParameter, Any?>): T {
        val instance = constructor.callBy(args) as T
        val kClass = instance::class
        if (containerClasses.isSingleton(kClass)) {
            singletonsCache[kClass] = instance
        }
        return instance
    }

    /**
     * The object can be created by passing in the interface or the actual class
     * If using an interface, the implementing class must be available
     * @param kClass class to resolve the binding for
     * @return The resolved class to continue work with
     */
    private fun resolveBinding(kClass: KClass<*>): KClass<*> {
        val resolvedClass = containerClasses.getResolvedClass(kClass)
        if (resolvedClass.isInterface()) {
            throw UnresolvedClassException("No binding could be found for ${resolvedClass.qualifiedName}")
        }
        Timber.d("resolvedClass ${resolvedClass.qualifiedName}")
        return resolvedClass
    }
}