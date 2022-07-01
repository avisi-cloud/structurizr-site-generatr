package nl.avisi.structurizr.site.generatr.site.model

import nl.avisi.structurizr.site.generatr.site.context.GeneratorContext

abstract class PageViewModel(protected val generatorContext: GeneratorContext) {
    val pageTitle by lazy {
        if (generatorContext.workspace.name.isNotBlank())
            "$pageSubTitle | ${generatorContext.workspace.name}"
        else
            pageSubTitle
    }
    val headerBar by lazy { HeaderBarViewModel(this, generatorContext) }
    val menu by lazy { MenuViewModel(generatorContext, this) }

    abstract val url: String
    abstract val pageSubTitle: String
}
