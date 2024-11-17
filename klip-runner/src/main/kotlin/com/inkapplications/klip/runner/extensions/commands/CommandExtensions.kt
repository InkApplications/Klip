package com.inkapplications.klip.runner.extensions.commands

import com.inkapplications.klip.runner.KlipScript
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.runBlocking
import kotlin.streams.asSequence
import kotlin.system.exitProcess

/**
 * Executes a simple shell command and capture/handle the output.
 */
fun KlipScript.exec(
    command: String,
    exitOnFail: Boolean = true,
    printOutput: Boolean = true,
): ExecResult {
    val processResult = runCatching {
        ProcessBuilder(command.split(" "))
            .directory(workingDir)
            .redirectErrorStream(false)
            .start()
            .also { it.waitFor() }
    }
    val result = processResult.toExecResult()

    runBlocking {
        if (printOutput && result is ExecResult.RunCompleted) {
            result.output.collect {
                println(it)
            }
        }
    }

    when {
        exitOnFail && result is ExecResult.Error -> {
            if (printOutput) {
                result.error.message?.run(logger::error)
                result.error.printStackTrace()
            }
            exitProcess(result.exitCode)
        }
        exitOnFail && result is ExecResult.Failed -> {
            exitProcess(result.exitCode)
        }
    }

    return result
}

private fun Result<Process>.toExecResult(): ExecResult
{
    val process = getOrElse { error ->
        return ExecResult.Error(
            error = error,
        )
    }
    val standardOutput = process.inputStream.bufferedReader()
        .lines()
        .asSequence()
        .asFlow()
    val errorOutput = process.errorStream.bufferedReader()
        .lines()
        .asSequence()
        .asFlow()

    return when (val exitCode = process.exitValue()) {
        0 -> ExecResult.Success(
            exitCode = exitCode,
            standardOutput = standardOutput,
            errorOutput = errorOutput,
        )
        else -> ExecResult.Failed(
            exitCode = exitCode,
            standardOutput = standardOutput,
            errorOutput = errorOutput,
        )
    }
}

