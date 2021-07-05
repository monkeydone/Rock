package com.a.findfragment

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class FragmentAnnotation(val showName: String = "", val parentName: String = "")

data class FragmentAnnotationData(
    val showName: String,
    val parentName: String,
    val fragmentName: String
)
