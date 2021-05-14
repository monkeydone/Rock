package com.bn.simpleplugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project


class SimplePlugin : Plugin<Project> {
    override fun apply(p0: Project) {
        val appExtension = p0.extensions.getByType(
            AppExtension::class.java
        )
        appExtension.registerTransform(SimpleTransform(p0))
    }

}