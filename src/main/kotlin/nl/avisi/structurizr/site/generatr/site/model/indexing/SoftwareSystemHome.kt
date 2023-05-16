package nl.avisi.structurizr.site.generatr.site.model.indexing

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.site.asUrlToDirectory
import nl.avisi.structurizr.site.generatr.site.model.PageViewModel
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemPageViewModel
import nl.avisi.structurizr.site.generatr.site.model.contentText
import nl.avisi.structurizr.site.generatr.site.model.contentTitle

fun softwareSystemHome(softwareSystem: SoftwareSystem, viewModel: PageViewModel): Document? {
    val (contentTitle, contentText) = softwareSystem.section()
    val propertyValues = softwareSystem.propertyValues()

    if (contentTitle.isBlank() && contentText.isBlank() && propertyValues.isBlank())
        return null

    return Document(
        SoftwareSystemPageViewModel.url(softwareSystem, SoftwareSystemPageViewModel.Tab.HOME)
            .asUrlToDirectory(viewModel.url),
        "Software System Info",
        "${softwareSystem.name} | ${contentTitle.ifEmpty { "Info" }}",
        "$contentTitle $contentText $propertyValues".trim(),
    )
}

private fun SoftwareSystem.section() = documentation.sections.firstOrNull()?.let {
    it.contentTitle() to it.contentText()
} ?: ("" to "")

private fun SoftwareSystem.propertyValues() = properties.values.joinToString(" ")
