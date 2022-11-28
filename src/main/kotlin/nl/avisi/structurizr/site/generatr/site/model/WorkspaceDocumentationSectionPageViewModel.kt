package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Section
import nl.avisi.structurizr.site.generatr.normalize
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class WorkspaceDocumentationSectionPageViewModel(generatorContext: GeneratorContext, section: Section) :
    PageViewModel(generatorContext) {
    override val url = url(section)
    override val pageSubTitle: String = section.title

    val content = markdownToHtml(this, section.content, generatorContext.svgFactory)

    companion object {
        fun url(section: Section): String {
            return "/${section.title.normalize()}"
        }
    }
}
