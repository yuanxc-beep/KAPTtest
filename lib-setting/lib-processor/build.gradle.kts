
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
    implementation(project(":lib-setting:lib-annotation"))
    kapt("org.jetbrains.kotlin:kotlin-annotation-processing:1.5.31")  // 添加 Kotlin Kapt 依
    kapt(kotlin("kapt"))

    implementation("com.google.auto.service:auto-service:1.0-rc6")
    annotationProcessor ("com.google.auto.service:auto-service:1.0-rc6")
    implementation("com.squareup:javapoet:1.13.0")
}