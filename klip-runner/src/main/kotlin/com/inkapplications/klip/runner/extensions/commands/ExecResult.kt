package com.inkapplications.klip.runner.extensions.commands

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge

/**
 * Result from an [exec] command.
 */
sealed interface ExecResult
{
    /**
     * Final return code of the command executed.
     */
    val exitCode: Int

    /**
     * Whether the command run can be considered successful.
     */
    val isSuccess: Boolean

    /**
     * Results that ran the command successfully.
     *
     * These results have outputs from the run command.
     */
    sealed interface RunCompleted: ExecResult
    {
        override val exitCode: Int
        val standardOutput: Flow<String>
        val errorOutput: Flow<String>
        val output: Flow<String> get() = merge(standardOutput, errorOutput)
    }

    /**
     * Nominal return code from the executed command.
     */
    data class Success(
        override val exitCode: Int,
        override val standardOutput: Flow<String>,
        override val errorOutput: Flow<String>,
    ): RunCompleted {
        override val isSuccess: Boolean = true
    }

    /**
     * Result when the command was run, but exited with an unsuccesful code.
     */
    data class Failed(
        override val exitCode: Int,
        override val standardOutput: Flow<String>,
        override val errorOutput: Flow<String>,
    ): RunCompleted {
        override val isSuccess: Boolean = false
    }

    /**
     * Result when the command failed to run.
     *
     * This result type has no output, and always has a failed exit code
     * of 127.
     */
    data class Error(
        val error: Throwable,
    ): ExecResult {
        override val exitCode: Int = 127
        override val isSuccess: Boolean = false
    }
}