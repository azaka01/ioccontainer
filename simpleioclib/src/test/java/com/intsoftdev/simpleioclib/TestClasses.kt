package com.intsoftdev.simpleioclib

interface TestA

class TestAImpl(
    val msg: String?,
    val dataProperties: Map<String, String>?,
    val dataValues: List<Double> = listOf(1.1, 2.2, 4.4),
    val consumerA: ConsumerA
) : TestA

interface TestApi {
    fun getUsers(): Int
}

interface ConsumerA : TestApi
class ConsumerAImpl constructor(val consumerB: ConsumerB) : ConsumerA, TestApi {
    override fun getUsers() = 2 + consumerB.getUsers()
}

class AnotherConsumerAImpl : ConsumerA {
    override fun getUsers() = 98
}

interface ConsumerB : TestApi
class ConsumerBImpl constructor(val consumerC: ConsumerC) : ConsumerB {
    override fun getUsers() = 8 * consumerC.getUsers()
}

interface ConsumerC : TestApi
class ConsumerCImpl : ConsumerC {
    override fun getUsers() = 5
}

interface Circular
class CircularImpl constructor(val consumerA: ConsumerA, val circular: Circular) : Circular