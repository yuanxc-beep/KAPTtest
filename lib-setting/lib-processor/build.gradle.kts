
plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("kotlin-kapt")
}


java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
dependencies{
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.10")
    kapt(project(":lib-setting:lib-annotation"))
    //kapt("org.jetbrains.kotlin:kotlin-annotation-processing:1.5.31")  // 添加 Kotlin Kapt 依
   // kapt(kotlin("kapt"))
    implementation("org.jetbrains.kotlin.kapt:org.jetbrains.kotlin.kapt.gradle.plugin:1.8.21")
    implementation("com.google.auto.service:auto-service:1.0-rc6")
    annotationProcessor ("com.google.auto.service:auto-service:1.0-rc6")
    implementation("com.squareup:javapoet:1.13.0")
}