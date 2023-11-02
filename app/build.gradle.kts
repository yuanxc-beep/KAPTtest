import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.TypeSpec
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
        minSdk = 26
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


tasks.register("abc") {
    doLast {
        // 在这里定义任务的具体逻辑
        println("Running generateFiles task")
    }
}
//val classMap = kotlin.collections.hashMapOf<String,String>()
tasks.register("generateFiles") {
    doLast {
        val layoutDir = project.projectDir.resolve("src/main/res/layout")
        layoutDir.listFiles { file -> file.name.endsWith(".xml") }
            ?.filter { hasSettingRootAttribute(it) }
            ?.forEach { file ->
                val xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
                val settingRootName = xmlDoc.documentElement.getAttribute("settingRoot") //找到xml的根节点

               // generateSettingRootClass(settingRootName, file)
                traverseNode(element =  xmlDoc.documentElement,
                    isOk = {element ->
                        val node =   element.attributes.getNamedItem("app:settingItem")   //找到一个节点
                        println("traverseNode check node is OK node = ${node.nodeValue}")
                        node!=null
                    },
                    block = { element ->
                        generateNodeClass(className = element.attributes.getNamedItem("app:settingItem").nodeValue,
                            userType = element.attributes.getNamedItem("app:user").nodeValue,
                            route = element.attributes.getNamedItem("app:route").nodeValue)
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
       // generateNodeClass()
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
    val node =  xmlDoc.documentElement.attributes.getNamedItem("app:settingRoot")
    println("check file ${file.absoluteFile} hasRoot:${node != null}")
    return node != null
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

fun generateNodeClass(className:String, //类名
                      userType:String,  //类权限
                      route:String,//route
                      ) {
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
        .superclass(settingNodeClass)
        .build()

    val outputDir =  project.projectDir.resolve("build${File.separator}generated${File.separator}settingtree${File.separator}output")
    outputDir.mkdirs()

    val javaFile = JavaFile.builder("com.xicai.cfgtest", settingNode)
        .build()

    // 将生成的 Java 文件写入指定目录
    val javaFileOutput = File(outputDir, "setting_node")
    javaFile.writeTo(javaFileOutput)
    println("Running generateFiles task  finish output = ${outputDir.absoluteFile}")
}


dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
   // implementation("com.android.tools.build:gradle:7.0.2")
   // implementation("androidx.databinding:databinding-compiler-common:8.1.0")
   // implementation("androidx.databinding:databinding-common:8.1.0")
   // implementation("com.android.databinding:baseLibrary:8.1.0")


}