package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Section
import com.structurizr.model.Component
import com.structurizr.model.Container
import nl.avisi.structurizr.site.generatr.normalize
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemContainerComponentSectionPageViewModel(
    generatorContext: GeneratorContext,
    component: Component,
    section: Section
) : SoftwareSystemPageViewModel(generatorContext, component.container.softwareSystem, Tab.SECTIONS) {

    override val url = url(component, section)

    val content = toHtml(this, section.content, section.format, generatorContext.svgFactory)

    companion object {
        fun url(component: Component, section: Section) =
            "${SoftwareSystemContainerComponentSectionsPageViewModel.url(component)}/${section.contentTitle().normalize()}"
    }
}
