dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenLocal()
        mavenCentral()
    }
}
rootProject.name = "IOCConsumerApp"
include(":app")
include(":simpleioclib")
