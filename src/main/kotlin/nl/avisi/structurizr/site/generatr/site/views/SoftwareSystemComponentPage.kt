package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import kotlinx.html.a
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemComponentPageViewModel

fun HTML.softwareSystemComponentPage(viewModel: SoftwareSystemComponentPageViewModel) {
    if (viewModel.visible)
        softwareSystemPage(viewModel) {
            viewModel.diagrams.forEach {
                a{
                    attributes["id"] = it.key.lowercase()
                }
                diagram(it)
            }
        }
    else
        redirectUpPage()
}
