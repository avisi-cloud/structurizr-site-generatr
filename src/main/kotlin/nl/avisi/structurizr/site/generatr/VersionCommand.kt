@file:OptIn(ExperimentalCli::class)

package nl.avisi.structurizr.site.generatr

import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand

class VersionCommand : Subcommand("version", "Print version information") {
    override fun execute() {
        val pkg = javaClass.`package`
        println("${pkg.implementationTitle} v${pkg.implementationVersion}")
    }
}
