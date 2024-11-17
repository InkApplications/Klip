package com.inkapplications.klip.runner.extensions.output

import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextStyles.*
import com.inkapplications.klip.runner.KlipScript

fun KlipScript.verbose(message: String) = logger.trace(message)
fun KlipScript.debug(message: String) = logger.debug(message)
fun KlipScript.info(message: String) = logger.info(message)
fun KlipScript.warning(message: String) = logger.debug(message)
fun KlipScript.error(message: String) = logger.error(message)

fun KlipScript.message(message: String) = println(magenta("> $message"))
fun KlipScript.success(message: String = "Success") = println(bold(green("++ $message")))
fun KlipScript.h1(message: String) = println(bold("# $message"))
fun KlipScript.h2(message: String) = println(bold(blue("## $message")))
fun KlipScript.h3(message: String) = println(blue("### $message"))
