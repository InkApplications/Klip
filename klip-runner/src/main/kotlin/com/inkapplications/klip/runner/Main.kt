package  com.inkapplications.klip.runner

import java.io.File
import java.lang.IllegalArgumentException
import kotlin.script.experimental.api.ScriptDiagnostic
import kotlin.script.experimental.api.valueOr
import kotlin.script.experimental.api.ResultValue
import kotlin.system.exitProcess

private val VERBOSE_FLAGS = setOf("--verbose", "-v")

fun main(args: Array<String>)
{
    val isVerbose = args.any { it in VERBOSE_FLAGS }
    val file = args.firstOrNull { it !in VERBOSE_FLAGS } ?: throw IllegalArgumentException("No File Arg")
    val source = File(file)
    val commandArgs = args.filter { it !in VERBOSE_FLAGS }.minus(file)

    val script = KlipScript.evalFile(
        scriptFile = source,
        verbose = isVerbose,
        args = commandArgs,
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
