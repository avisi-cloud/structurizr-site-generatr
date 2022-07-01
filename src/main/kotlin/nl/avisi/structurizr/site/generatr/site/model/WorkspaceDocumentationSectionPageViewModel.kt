package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Section
import nl.avisi.structurizr.site.generatr.normalize
import nl.avisi.structurizr.site.generatr.site.context.GeneratorContext

class WorkspaceDocumentationSectionPageViewModel(generatorContext: GeneratorContext, section: Section) :
    PageViewModel(generatorContext) {
    override val url = "/${generatorContext.currentBranch}/${section.title.normalize()}"
    override val pageSubTitle: String = section.title

    val markdown = MarkdownViewModel(section.content, generatorContext.currentBranch)
}
