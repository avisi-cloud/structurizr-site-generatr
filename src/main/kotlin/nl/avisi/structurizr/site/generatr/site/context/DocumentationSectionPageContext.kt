package nl.avisi.structurizr.site.generatr.site.context

import com.structurizr.documentation.Section
import nl.avisi.structurizr.site.generatr.normalize

class DocumentationSectionPageContext(generatorContext: GeneratorContext, val section: Section)
    : AbstractPageContext(generatorContext, section.title, "${section.title.normalize()}/index.html")
