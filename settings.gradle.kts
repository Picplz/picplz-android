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
        maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/") }
        maven {
            url = uri("https://devrepo.kakao.com/nexus/repository/kakaomap-releases/")
        }
    }
}

rootProject.name = "picplz"

include(":app")

// Core modules
include(":core:domain")
include(":core:data")
include(":core:ui")
include(":core:common")

// Feature modules
include(":feature:auth")
include(":feature:photographer")
include(":feature:chat")
include(":feature:mypage")
include(":feature:feed")
include(":feature:main")
include(":feature:reservation")
