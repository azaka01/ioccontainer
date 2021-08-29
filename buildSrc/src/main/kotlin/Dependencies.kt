import Versions.junitVersion
import Versions.mockitoKotlinVersion
import Versions.mockitoVersion
import Versions.timber

object Versions {
    const val timber = "5.0.1"
    const val junitVersion = "4.13"
    const val mockitoKotlinVersion = "2.2.0"
    const val mockitoVersion = "3.2.4"
    const val libVersion = "0.21"
}

object Deps {
    val timberLib = "com.jakewharton.timber:timber:$timber"

    val testjunit = "junit:junit:$junitVersion"
    val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:$mockitoKotlinVersion"
    val mockitoInline = "org.mockito:mockito-inline:$mockitoVersion"

    val groupId = "com.intsoftdev"
    val artifactId = "simpleioclib"
}