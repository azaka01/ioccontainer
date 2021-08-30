import Versions.junitVersion
import Versions.mockitoKotlinVersion
import Versions.mockitoVersion
import Versions.timber

object Versions {
    const val timber = "5.0.1"
    const val junitVersion = "4.13"
    const val mockitoKotlinVersion = "2.2.0"
    const val mockitoVersion = "3.2.4"
    const val libVersion = "0.1"
}

object Deps {
    const val timberLib = "com.jakewharton.timber:timber:$timber"

    const val testjunit = "junit:junit:$junitVersion"
    const val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:$mockitoKotlinVersion"
    const val mockitoInline = "org.mockito:mockito-inline:$mockitoVersion"

    const val groupId = "com.github.azaka01"
    const val artifactId = "simpleioclib"
}