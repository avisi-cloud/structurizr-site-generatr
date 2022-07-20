package nl.avisi.structurizr.site.generatr

fun String.normalize(): String = lowercase().replace("\\s+".toRegex(), "-")
