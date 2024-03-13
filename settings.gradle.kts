pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://dl.bintray.com/sargunv/maven")
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Pokedex"
include(":app")
