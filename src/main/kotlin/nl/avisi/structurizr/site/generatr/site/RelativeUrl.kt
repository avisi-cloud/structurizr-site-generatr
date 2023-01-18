package nl.avisi.structurizr.site.generatr.site

import java.nio.file.Path
import kotlin.io.path.relativeTo

fun String.asUrlRelativeTo(otherUrl: String, appendSlash: Boolean = true) =
    if (otherUrl == this) "."
    else Path.of(this)
        .relativeTo(Path.of(otherUrl)).toString() + if (appendSlash) "/" else ""
