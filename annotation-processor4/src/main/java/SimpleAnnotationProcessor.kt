package org.kotlin.annotationProcessor


import com.a.processor.ListFragmentAnnotation
import org.yanex.takenoko.*
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic.Kind.ERROR


@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("com.a.processor.ListFragmentAnnotation")
@SupportedOptions(SimpleAnnotationProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class SimpleAnnotationProcessor : AbstractProcessor() {
    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment
    ): Boolean {
        val annotatedElements =
            roundEnv.getElementsAnnotatedWith(ListFragmentAnnotation::class.java)
        if (annotatedElements.isEmpty()) return false

        val kaptKotlinGeneratedDir =
            processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME] ?: run {
                processingEnv.messager.printMessage(
                    ERROR,
                    "Can't find the target directory for generated Kotlin files."
                )
                return false
            }

        val generatedKtFile = kotlinFile("com.a.dproject") {

            var body = """

val map = HashMap<String,FragmentObject>()
            """.trimIndent()
//            classDeclaration("FragmentObject", DATA) {
//                primaryConstructor {
//                    param<String>("showName", VAL)
//                    param<String>("parentName", VAL)
//                    param<String>("fragmentName", VAL)
//                }
//            }

            import("com.a.processor.FragmentObject")

            val parentNameList = ArrayList<String>()

            for (element in annotatedElements) {
                val annotation: ListFragmentAnnotation =
                        element.getAnnotation(ListFragmentAnnotation::class.java)
                val showName = annotation.showName
                val parentName = annotation.parentName
                if (!parentNameList.contains(parentName) && parentName.isNotEmpty()) {
                    parentNameList.add(parentName)
                }

                val typeElement = element.toTypeElementOrNull() ?: continue

                property("simpleClassName") {
                    receiverType(typeElement.qualifiedName.toString())
                    getterExpression("this::class.java.simpleName")
                }

                body = """
                    ${body}
map.put("$showName", FragmentObject("$showName","$parentName","${typeElement.qualifiedName.toString()}"))
                """.trimIndent()

            }

            for (element in parentNameList) {
                body = """
                    ${body}
map.put("$element", FragmentObject("$element","","com.a.findfragment.ListFragment"))
                """.trimIndent()
            }


            function("getAnnotationMap") {
//                param<Array<String>>("args")
                returnType("HashMap<String,FragmentObject>")
                body(
                    """
${body}
return map
            """.trimIndent()
                )
            }

        }

        File(kaptKotlinGeneratedDir, "CodeGenerated.kt").apply {
            parentFile.mkdirs()
            writeText(generatedKtFile.accept(PrettyPrinter(PrettyPrinterConfiguration())))
        }

        return true
    }

    fun Element.toTypeElementOrNull(): TypeElement? {
        if (this !is TypeElement) {
            processingEnv.messager.printMessage(ERROR, "Invalid element type, class expected", this)
            return null
        }

        return this
    }
}