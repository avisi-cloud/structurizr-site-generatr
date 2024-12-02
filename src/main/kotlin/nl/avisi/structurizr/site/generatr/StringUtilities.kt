package nl.avisi.structurizr.site.generatr

// based on https://stackoverflow.com/questions/1976007/what-characters-are-forbidden-in-windows-and-linux-directory-names
private const val reservedChars = "|\\?*<\":>+[]/'"
private val reservedNames = setOf(
    "CON", "PRN", "AUX", "NUL",
    "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9",
    "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"
)

fun String.normalize(): String =
    lowercase()
        .replace("\\s+".toRegex(), "-")
        .filterNot { reservedChars.contains(it) }
        .trim()
        .let {
            if (reservedNames.contains(it.uppercase()))
                "${it}-"
            else
                it
        }
