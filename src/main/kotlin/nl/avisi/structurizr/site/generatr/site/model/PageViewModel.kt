package nl.avisi.structurizr.site.generatr.site.model

import nl.avisi.structurizr.site.generatr.site.GeneratorContext

abstract class PageViewModel(protected val generatorContext: GeneratorContext) {
    val pageTitle by lazy {
        if (generatorContext.workspace.name.isNotBlank())
            "$pageSubTitle | ${generatorContext.workspace.name}"
        else
            pageSubTitle
    }
    val favicon by lazy { FaviconViewModel(generatorContext, this) }
    val headerBar by lazy { HeaderBarViewModel(this, generatorContext) }
    val menu by lazy { MenuViewModel(generatorContext, this) }
    val includeAutoReloading = generatorContext.serving

    abstract val url: String
    abstract val pageSubTitle: String
}
