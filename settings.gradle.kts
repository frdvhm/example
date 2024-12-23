pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // Mapbox Maven repository with authentication
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            credentials {
                username = "mapbox" // Username selalu "mapbox"
                password = System.getenv("pk.eyJ1IjoiZnJkdmFobWkiLCJhIjoiY200emJsMGl0MHY1ZzJpcjA1bHMxNWo3ciJ9.kW37gd0csvJMvOzGFNP8Dw") ?: "YOUR_SECRET_TOKEN"
            }
        }
    }
}

rootProject.name = "App"
include(":app")
