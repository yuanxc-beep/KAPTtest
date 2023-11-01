buildscript {
    val agp_version by extra("7.4.2")
    val agp_version1 by extra("4.2.2")
    val agp_version2 by extra("7.4.2")
    //gradleVersion = "7.0.4" // 或者你想要的版本号
    //classpath("com.android.tools.build:gradle:$gradleVersion")
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.0.1" apply false  //这个就是com.android.tools.build:gradle
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id("org.jetbrains.kotlin.jvm") version "1.8.0" apply false

}
