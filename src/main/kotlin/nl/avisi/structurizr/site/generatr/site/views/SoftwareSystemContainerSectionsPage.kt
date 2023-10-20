package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import kotlinx.html.div
import kotlinx.html.li
import kotlinx.html.ul
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemContainerSectionsPageViewModel

fun HTML.softwareSystemContainerSectionsPage(viewModel: SoftwareSystemContainerSectionsPageViewModel) {
    if (viewModel.visible)
        softwareSystemPage(viewModel) {
            div(classes = "tabs") {
                ul(classes = "m-0") {
                    viewModel.sectionsTabs
                        .forEach {
                            li(classes = if (it.link.active) "is-active" else null) {
                                link(it.link)
                            }
                        }
                }
            }
            table(viewModel.sectionsTable)
        }
    else
        redirectUpPage()
}
