pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "ctos-pocket"
include(
    ":app",
    ":core:core-common",
    ":core:core-ui",
    ":core:core-audio",
    ":core:core-haptics",
    ":core:core-net",
    ":domain:discovery",
    ":domain:exploit",
    ":data:data-discovery",
    ":data:data-exploit",
    ":data:data-storage",
    ":feature:feature-radar",
    ":feature:feature-target",
    ":feature:feature-ar",
    ":feature:feature-terminal",
    ":feature:feature-settings",
    ":feature:feature-onboarding"
)
