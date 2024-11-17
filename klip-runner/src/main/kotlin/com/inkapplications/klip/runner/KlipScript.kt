package com.inkapplications.klip.runner

import java.io.File
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.EvaluationResult
import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.api.constructorArgs
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate
import kotlin.script.experimental.jvmhost.createJvmEvaluationConfigurationFromTemplate

@KotlinScript(
    fileExtension = "klip.kts",
    compilationConfiguration = KlipScriptConfig::class,
)
abstract class KlipScript
{
    fun hello()
    {
        println("Hello Wald")
    }

    companion object
    {
        fun evalFile(
            scriptFile: File,
        ): ResultWithDiagnostics<EvaluationResult> {
            val source = scriptFile.toScriptSource()

            val evaluationConfiguration = createJvmEvaluationConfigurationFromTemplate<KlipScript> {
                constructorArgs()
            }

            val compilationConfiguration = createJvmCompilationConfigurationFromTemplate<KlipScript>()

            return BasicJvmScriptingHost().eval(
                script = source,
                compilationConfiguration = compilationConfiguration,
                evaluationConfiguration = evaluationConfiguration,
            )
        }
    }
}