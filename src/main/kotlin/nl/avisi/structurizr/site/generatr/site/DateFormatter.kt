package nl.avisi.structurizr.site.generatr.site

import java.time.ZoneId
import java.util.*

fun formatDate(date: Date): String {
    val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    return String.format("%02d-%02d-%04d", localDate.dayOfMonth, localDate.monthValue, localDate.year)
}
