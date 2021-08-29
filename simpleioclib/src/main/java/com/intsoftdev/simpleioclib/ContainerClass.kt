package com.intsoftdev.simpleioclib

import kotlin.reflect.KClass

typealias ContainerClasses = MutableList<ContainerClass>

data class ContainerClass(
    val interfaceClass: KClass<*>,
    val instanceClass: KClass<*>,
    val classScope: ClassScope,
    val properties: Map<String, Any>
)

fun ContainerClasses.insert(
    interfaceClass: KClass<*>,
    instanceClass: KClass<*>,
    classScope: ClassScope,
    properties: Map<String, Any>
) = add(
        ContainerClass(
            interfaceClass = interfaceClass,
            instanceClass = instanceClass,
            classScope = classScope,
            properties = properties
        )
    )

fun ContainerClasses.getResolvedClass(kClass: KClass<*>): KClass<*> {
    return find { it.interfaceClass == kClass }?.instanceClass ?: kClass
}

fun ContainerClasses.get(kClass: KClass<*>): ContainerClass {
    return getClass(kClass) ?: throw IllegalStateException("class instance not located")
}

fun ContainerClasses.getProperties(kClass: KClass<*>): Map<String, Any>? =
    getClass(kClass)?.properties

fun ContainerClasses.isSingleton(kClass: KClass<*>) =
    getClass(kClass)?.classScope == ClassScope.Singleton

fun ContainerClasses.getClass(kClass: KClass<*>) = find { it.instanceClass == kClass }

internal fun KClass<*>.isInterface() = this.java.isInterface