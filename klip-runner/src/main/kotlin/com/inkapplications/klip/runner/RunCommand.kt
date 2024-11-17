package com.inkapplications.klip.runner

import com.github.ajalt.clikt.core.CliktCommand

internal object RunCommand: CliktCommand()
{
    override fun run()
    {
        echo("Hello Wald")
    }
}
