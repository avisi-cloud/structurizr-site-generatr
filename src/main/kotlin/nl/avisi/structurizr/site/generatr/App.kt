@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
@file:OptIn(ExperimentalCli::class)

package nl.avisi.structurizr.site.generatr

import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli

fun main(args: Array<String>) {
    val parser = ArgParser("structurizr-site-generatr")

    parser.subcommands(ServeCommand(), GenerateSiteCommand())
    parser.parse(args)
}
