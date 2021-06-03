package com.a.processor

@Target(AnnotationTarget.CLASS)
annotation class ListFragmentAnnotation(val showName: String = "", val parentName: String = "")

data class FragmentObject(val showName: String, val parentName: String, val fragmentName: String)
