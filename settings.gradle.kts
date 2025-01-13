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
        //maven { url=uri("https://mirrors.tencent.com/nexus/repository/maven-public/") }
        //maven { url=uri("https://repo.maven.apache.org") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        //maven { url=uri("https://mirrors.tencent.com/nexus/repository/maven-public/") }
        //maven { url=uri("https://repo.maven.apache.org") }
    }
}

rootProject.name = "RSS Viewer"
include(":app")
include(":core:model")
include(":core:designsystem")
include(":core:network")
include(":feature:bookmark")
include(":core:localcache")
include(":core:database")
include(":core:datastore")
include(":feature:viewdownloaded")
include(":feature:articledownload")
include(":feature:categories")
include(":feature:searchcategories")
include(":feature:searcharticles")
include(":feature:articlesviewer")
include(":feature:articledescription")
include(":feature:uploadedarticles")
include(":feature:settings")
include(":feature:infonavbar")
