package nl.avisi.structurizr.site.generatr.site.model

import nl.avisi.structurizr.site.generatr.includedSoftwareSystems
import nl.avisi.structurizr.site.generatr.site.GeneratorContext
import nl.avisi.structurizr.site.generatr.site.views.CDN

abstract class PageViewModel(protected val generatorContext: GeneratorContext) {
    val pageTitle: String by lazy {
        if (pageSubTitle.isNotBlank() && generatorContext.workspace.name.isNotBlank())
            "$pageSubTitle | ${generatorContext.workspace.name}"
        else if (generatorContext.workspace.name.isNotBlank())
            generatorContext.workspace.name
        else
            pageSubTitle
    }
    val cdn by lazy { CDN(generatorContext.workspace) }
    val favicon by lazy { FaviconViewModel(generatorContext, this) }
    val customStylesheet by lazy { CustomStylesheetViewModel(generatorContext, this) }
    val headerBar by lazy { HeaderBarViewModel(this, generatorContext) }
    val menu by lazy { MenuViewModel(generatorContext, this) }
    val includeAutoReloading = generatorContext.serving
    val flexmarkConfig by lazy { buildFlexmarkConfig(generatorContext) }
    val includeAdmonition = flexmarkConfig.selectedExtensionMap.containsKey("Admonition")
    val includeKatex = flexmarkConfig.selectedExtensionMap.containsKey("GitLab")
    val includedSoftwareSystems = generatorContext.workspace.includedSoftwareSystems
    val configuration = generatorContext.workspace.views.configuration.properties
    val includeTreeview = configuration.getOrDefault("generatr.site.nestGroups", "false").toBoolean()

    abstract val url: String
    abstract val pageSubTitle: String
}
