package nl.avisi.structurizr.site.generatr.site

import java.nio.file.Path
import kotlin.io.path.relativeTo

fun String.asUrlToDirectory(otherUrl: String) = "${this.asUrlToFile(otherUrl)}/"

fun String.asUrlToFile(otherUrl: String) =
    if (otherUrl == this) "."
    else Path.of(this)
        .relativeTo(Path.of(otherUrl)).toString()

