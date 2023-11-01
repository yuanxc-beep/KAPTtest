package com.xicai.lib_processor

import com.google.auto.service.AutoService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@AutoService(value = [Processor::class])
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class Processor:AbstractProcessor() {
    override fun process(p0: MutableSet<out TypeElement>?, p1: RoundEnvironment?): Boolean {
        //TODO("Not yet implemented")
        return true
    }
}