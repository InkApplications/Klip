package com.inkapplications.klip.runner.extensions.output

import com.inkapplications.klip.runner.KlipScript

fun KlipScript.verbose(message: String) {
    if (isVerbose) {
        println(message)
    }
}
