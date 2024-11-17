package com.inkapplications.klip.runner

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import kotlin.script.experimental.api.ResultValue
import kotlin.script.experimental.api.ScriptDiagnostic
import kotlin.script.experimental.api.onFailure
import kotlin.script.experimental.api.valueOr
import kotlin.system.exitProcess

internal object RunCommand: CliktCommand()
{
    private val source by argument(
        help = "File to be evaluated"
    ).file(
        mustExist = true,
        canBeFile = true,
        canBeDir = false,
        mustBeReadable = true,
    )

    private val verbose by option(
        help = "Print verbose output",
    ).flag()

    override fun run()
    {
        val script = KlipScript.evalFile(
            scriptFile = source,
            verbose = verbose,
        ).valueOr {
            it.reports
                .filter { it.severity == ScriptDiagnostic.Severity.ERROR }
                .forEach {
                    val location = it.location
                    val locationStr = when {
                        location != null -> "${location.start.line}:${location.start.col}"
                        else -> ""
                    }
                    println("${source.name}:$locationStr ${it.message}")
                }
                exitProcess(1)
        }.returnValue.also {
            if (it is ResultValue.Error) {
                it.error.message?.run(::println)
                it.error.printStackTrace()
                exitProcess(1)
            }
        }.scriptInstance.let { it as KlipScript }
    }
}
