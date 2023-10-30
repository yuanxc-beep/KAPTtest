import com.android.utils.forEach
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.TypeSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.ClassName
import javax.lang.model.element.Modifier


plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.xicai.cfgtest"
    compileSdk = 33

    viewBinding {
         enable = true
    }


    defaultConfig {
        applicationId = "com.xicai.cfgtest"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}



tasks.register("generateFiles11") {
    doLast {
        // 在这里定义任务的具体逻辑
        println("Running generateFiles task")
    }
}
tasks.register("generateFiles") {
    doLast {
        val layoutDir = File("src/main/res/layout")
        layoutDir.listFiles { file -> file.name.endsWith(".xml") }
            ?.filter { hasSettingRootAttribute(it) }
            ?.forEach { file ->
                val xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
                val settingRootName = xmlDoc.documentElement.getAttribute("settingRoot")

                generateSettingRootClass(settingRootName, file)
                    traverseNode(element =  xmlDoc.documentElement,
                        isOk = {element ->
                            element.attributes.getNamedItem("settingName") != null
                        },
                        block = { element ->

                        })

                  /*  .filter { it is Element && it.getAttribute("settingName").isNotEmpty() }
                    .forEach { settingName ->
                        generateSettingItemClass(
                            settingRootName,
                            settingName.getAttribute("settingName"),
                            file
                        )
                    }*/
            }
        generateClass()
    }
}

// 假设 rootElement 是 XML 文档的根节点
fun traverseNode(element: Element,isOk:(Element)->Boolean,block:(Element)->Unit){
    val childNodes = element.childNodes
    for (i in 0 until childNodes.length) {
        val node = childNodes.item(i)
        if (node is Element) {
            // 对每个子节点进行操作
            // 如果该子节点还有子节点，则递归调用 traverseNode 方法
            if (isOk(node)){
                block(node)
            }
            traverseNode(node,isOk,block)
        }
    }
}


fun hasSettingRootAttribute(file: File): Boolean {
    val xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
    return xmlDoc.documentElement.attributes.getNamedItem("settingRoot") != null
}

fun generateSettingRootClass(settingRootName: String, file: File) {
    val className = "SettingRoot${file.name.removeSuffix(".xml")}"
    val classContent = """
        class $className : Node() {
            val node: Node = $settingRootName()
        }
    """.trimIndent()
    File("src/main/java/com/example/$className.kt").writeText(classContent)
}

fun generateSettingItemClass(settingRootName: String, settingName: String, file: File) {
    val settingItemClassName = "SettingItem$settingName"
    val classContent = """
        class $settingItemClassName : Node() {
            val node: Node = $settingName()
        }
    """.trimIndent()
    File("src/main/java/com/example/$settingItemClassName.kt").writeText(classContent)
}

fun generateClass() {
    val settingUserClass = ClassName.get("com.xicai.cfgtest", "SettingUser")
    val settingNodeClass = ClassName.get("com.xicai.cfgtest", "SettingNode")

    val nodeNameField = FieldSpec.builder(String::class.java, "nodeName")
        .addModifiers(Modifier.PUBLIC)
        .initializer("null")
        .build()

    val userField = FieldSpec.builder(settingUserClass, "user")
        .addModifiers(Modifier.PUBLIC)
        .initializer("\$T.Operator", settingUserClass)
        .build()

    val parentField = FieldSpec.builder(settingNodeClass, "parent")
        .addModifiers(Modifier.PUBLIC)
        .initializer("null")
        .build()

    val settingNode = TypeSpec.classBuilder("SettingNode")
        .addModifiers(Modifier.PUBLIC)
        .addField(nodeNameField)
        .addField(userField)
        .addField(parentField)
        .build()

    val outputPath = "build${File.separator}generated${File.separator}settingtree${File.separator}output"
    val outputDir = File(outputPath)
    outputDir.mkdirs()

    val javaFile = JavaFile.builder("com.xicai.cfgtest", settingNode)
        .build()

    // 将生成的 Java 文件写入指定目录
    javaFile.writeTo(outputDir)
    println("Running generateFiles task  finish output = ${outputDir.absoluteFile}")
}
dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.android.tools.build:gradle:7.0.2")
    implementation("androidx.databinding:databinding-compiler-common:7.0.2")
    implementation("androidx.databinding:databinding-common:7.0.2")
    implementation("com.android.databinding:baseLibrary:7.0.2")


}