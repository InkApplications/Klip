package com.inkapplications.klip.runner.extensions.output

import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextStyles.*
import kimchi.logger.LogLevel
import kimchi.logger.LogLevel.*
import kimchi.logger.LogWriter

/**
 * Prints logs to console, with color formatting.
 */
internal class FormattedLogWriter(
    private val isVerbose: Boolean,
): LogWriter {
    override fun log(level: LogLevel, message: String, cause: Throwable?) {
        when (level) {
            TRACE -> println("[$level]: $message")
            DEBUG -> println(cyan("[$level]: $message"))
            INFO -> println(magenta("> [$level]: $message"))
            WARNING -> println(bold(yellow(">> [$level]: $message")))
            ERROR -> println(bold(red(">>> [$level]: $message")))
        }
        cause?.run {
            printStackTrace()
        }
    }

    override fun shouldLog(level: LogLevel, cause: Throwable?): Boolean {
        return when {
            isVerbose -> true
            else -> level >= WARNING
        }
    }
}