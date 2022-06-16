package nl.avisi.structurizr.site.generatr.site.context

import com.structurizr.documentation.Section

class HomePageContext(generatorContext: GeneratorContext, val section: Section) :
    AbstractPageContext(generatorContext, "Home", "index.html")
