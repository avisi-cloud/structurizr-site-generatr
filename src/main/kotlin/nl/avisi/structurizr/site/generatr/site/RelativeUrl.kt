package nl.avisi.structurizr.site.generatr.site

import java.io.File

fun String.asUrlToDirectory(otherUrl: String) = "${this.asUrlToFile(otherUrl)}/"

fun String.asUrlToFile(relativeTo: String): String =
    if (relativeTo == this) "."
    else File(this)
        .relativeTo(File(relativeTo))
        .invariantSeparatorsPath
