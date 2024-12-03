package com.inkapplications.klip.runner

import com.inkapplications.klip.runner.extensions.output.FormattedLogWriter
import kimchi.logger.ConsolidatedLogger
import kimchi.logger.KimchiLogger
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
abstract class KlipScript(
    val scriptFile: File,
    val isVerbose: Boolean,
    val args: List<String>,
) {
    private val logWriter = FormattedLogWriter(isVerbose)
    val logger: KimchiLogger = ConsolidatedLogger(logWriter)
    val scriptDir = scriptFile.parent
    val workingDir = File(".")

    companion object
    {
        fun evalFile(
            scriptFile: File,
            verbose: Boolean,
            args: List<String>,
        ): ResultWithDiagnostics<EvaluationResult> {
            val source = scriptFile.toScriptSource()

            val evaluationConfiguration = createJvmEvaluationConfigurationFromTemplate<KlipScript> {
                constructorArgs(scriptFile, verbose, args)
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