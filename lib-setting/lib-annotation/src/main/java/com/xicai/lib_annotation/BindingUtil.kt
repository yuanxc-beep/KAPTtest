package com.xicai.lib_annotation

import java.io.File
import java.net.URLClassLoader

val bindingClassPackage = "com.example.databinding"
val generatedDir = "build/generated/data_binding_base_class_source_out"

fun loadBindingClass(className: String): Class<*>? {
    val loader = URLClassLoader(arrayOf(File(generatedDir).toURI().toURL()))
    return try {
        loader.loadClass("$bindingClassPackage.$className")
    } catch (e: ClassNotFoundException) {
        e.printStackTrace()
        null
    }
}

fun main() {
    // 替换为你自己的布局文件生成的类名
    val layoutBindingClassName = "ExampleBinding"

    val bindingClass = loadBindingClass(layoutBindingClassName)
    if (bindingClass != null) {
        // 这里可以根据获取到的绑定类进行操作
        println("成功加载绑定类：${bindingClass.canonicalName}")
    } else {
        println("无法加载绑定类：$layoutBindingClassName")
    }
}
