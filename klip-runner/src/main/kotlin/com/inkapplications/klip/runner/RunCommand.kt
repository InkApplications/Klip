package com.inkapplications.klip.runner

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.file
import kotlin.script.experimental.api.ScriptDiagnostic
import kotlin.script.experimental.api.onFailure
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

    override fun run()
    {
        KlipScript.evalFile(source)
            .onFailure {
                it.reports
                    .filter { it.severity >= ScriptDiagnostic.Severity.ERROR }
                    .forEach {
                        val location = it.location
                        val locationStr = when {
                            location != null -> "${location.start.line}:${location.start.col}"
                            else -> ""
                        }
                        println("${source.name}:$locationStr ${it.message}")

                        it.exception?.printStackTrace()
                    }
                exitProcess(1)
            }
    }
}
