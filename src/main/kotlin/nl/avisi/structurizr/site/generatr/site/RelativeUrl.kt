package nl.avisi.structurizr.site.generatr.site

import kotlin.io.path.Path
import kotlin.io.path.relativeTo

fun makeUrlRelative(url: String, relativeTo: String): String {
    val a = Path(relativeTo)
    val b = Path(url)
    return b.relativeTo(a).toString()
}
