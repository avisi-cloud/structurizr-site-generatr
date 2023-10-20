package nl.avisi.structurizr.site.generatr

import kotlin.random.Random

fun String.normalize(): String = lowercase().replace("\\s+".toRegex(), "-")

fun randomId(): String {
    val charPool: List<Char> = ('a'..'z') + ('A'..'Z')
    return (1..15)
        .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
        .joinToString("")
}
