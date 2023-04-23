package nl.avisi.structurizr.site.generatr.site.model

import nl.avisi.structurizr.site.generatr.site.GeneratorContext
import nl.avisi.structurizr.site.generatr.site.model.FlexmarkConfig

abstract class PageViewModel(protected val generatorContext: GeneratorContext) {
    val pageTitle: String by lazy {
        if (pageSubTitle.isNotBlank() && generatorContext.workspace.name.isNotBlank())
        "$pageSubTitle | ${generatorContext.workspace.name}"
        else if (generatorContext.workspace.name.isNotBlank())
        generatorContext.workspace.name
        else
        pageSubTitle
    }
    val favicon by lazy { FaviconViewModel(generatorContext, this) }
    val headerBar by lazy { HeaderBarViewModel(this, generatorContext) }
    val menu by lazy { MenuViewModel(generatorContext, this) }
    val includeAutoReloading = generatorContext.serving
    
    val flexmarkConfig:FlexmarkConfig by lazy { 
        buildFlexmarkConfig(generatorContext) 
    }
    val includeAdmonition = flexmarkConfig.selectedExtensionMap.containsKey("Admonition")
    val includeKatex = flexmarkConfig.selectedExtensionMap.containsKey("GitLab")
    val includeMermaid = flexmarkConfig.selectedExtensionMap.containsKey("GitLab")

    abstract val url: String
    abstract val pageSubTitle: String
}
