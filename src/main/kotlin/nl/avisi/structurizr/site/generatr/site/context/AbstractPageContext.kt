package nl.avisi.structurizr.site.generatr.site.context

import java.io.File

abstract class AbstractPageContext(val generatorContext: GeneratorContext, val title: String, val htmlFile: String) {
    val workspace get() = generatorContext.workspace
    val currentBranch get() = generatorContext.currentBranch
    val branches get() = generatorContext.branches
    val url: String

    init {
        val parent = File(htmlFile).parent
        url = if (parent == null)
            "/${generatorContext.currentBranch}"
        else
            "/${generatorContext.currentBranch}/$parent/"
    }
}
