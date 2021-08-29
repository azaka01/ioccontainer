package com.intsoftdev.simpleioclib

import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.sameInstance
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Test

class SimpleIOCContainerTest {

    @Test
    fun testDependencyGraph() {
        val container = SimpleIOCContainer().apply {
            bind(ConsumerA::class, ConsumerAImpl::class, ClassScope.Factory)
            bind(ConsumerB::class, ConsumerBImpl::class, ClassScope.Factory)
            bind(ConsumerC::class, ConsumerCImpl::class, ClassScope.Factory)
        }

        val consumerA = container.create<ConsumerA>()
        assertThat(consumerA, instanceOf(ConsumerAImpl::class.java))

        val consumerAImpl = container.create<ConsumerAImpl>()
        assertThat(consumerAImpl, instanceOf(ConsumerAImpl::class.java))
        assertThat(consumerAImpl.consumerB, instanceOf(ConsumerBImpl::class.java))

        val consumerBImpl = container.create<ConsumerBImpl>()
        assertThat(consumerBImpl, instanceOf(ConsumerBImpl::class.java))
        assertThat(consumerBImpl.consumerC, instanceOf(ConsumerCImpl::class.java))
        assertThat(consumerA.getUsers(), CoreMatchers.equalTo(42))
    }

    @Test(expected = UnresolvedClassException::class)
    fun testIncompleteDependencyGraph() {
        val container = SimpleIOCContainer().apply {
            bind(ConsumerA::class, ConsumerAImpl::class, ClassScope.Factory)
        }
        container.create<ConsumerA>()
    }

    @Test(expected = UnresolvedClassException::class)
    fun testIncompleteBinding() {
        val container = SimpleIOCContainer().apply {
            bind(ConsumerA::class, ConsumerA::class, ClassScope.Factory)
        }
        container.create<ConsumerA>()
    }

    @Test
    fun testSingletonScope() {
        val container = SimpleIOCContainer().apply {
            bind(
                ConsumerA::class,
                AnotherConsumerAImpl::class,
                ClassScope.Singleton
            )
        }
        val consumerA = container.create<ConsumerA>()
        val consumerB = container.create<ConsumerA>()
        assertThat(consumerA, instanceOf(AnotherConsumerAImpl::class.java))
        assertThat(consumerB, instanceOf(AnotherConsumerAImpl::class.java))
        assertThat(consumerA, sameInstance(consumerB))
    }

    @Test
    fun testFactoryScope() {
        val container = SimpleIOCContainer().apply {
            bind(
                ConsumerA::class, AnotherConsumerAImpl::class, ClassScope.Factory
            )
        }
        val consumerA = container.create<ConsumerA>()
        val consumerB = container.create<ConsumerA>()
        assertThat(consumerA, instanceOf(AnotherConsumerAImpl::class.java))
        assertThat(consumerA, CoreMatchers.not(sameInstance(consumerB)))
    }

    @Test
    fun testPropertyInjection() {

        val propertiesToInject = mapOf(
            "msg" to "Hello world",
            "dataProperties" to mapOf("key1" to "val1")
        )

        val container = SimpleIOCContainer().apply {
            bind(
                TestA::class,
                TestAImpl::class,
                ClassScope.Factory,
                propertiesToInject
            )
            bind(ConsumerA::class, ConsumerAImpl::class, ClassScope.Factory)
            bind(ConsumerB::class, ConsumerBImpl::class, ClassScope.Factory)
            bind(ConsumerC::class, ConsumerCImpl::class, ClassScope.Factory)
        }

        val testA = container.create<TestA>() as TestAImpl
        assertThat(testA, instanceOf(TestAImpl::class.java))

        assertEquals(testA.msg, "Hello world")
        assertEquals(testA.dataProperties, mapOf("key1" to "val1"))
        assertEquals(testA.dataValues, listOf(1.1, 2.2, 4.4))
        assertThat(testA.consumerA, instanceOf(ConsumerAImpl::class.java))
    }

    @Test(expected = CircularDependencyException::class)
    fun testDetectingCircularDependencies() {
        val container = SimpleIOCContainer().apply {
            bind(ConsumerA::class, AnotherConsumerAImpl::class, ClassScope.Factory)
            bind(Circular::class, CircularImpl::class, ClassScope.Factory)
        }
        container.create<CircularImpl>()
    }
}