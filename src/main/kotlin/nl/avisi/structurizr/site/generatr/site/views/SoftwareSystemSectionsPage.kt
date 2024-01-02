package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import kotlinx.html.div
import kotlinx.html.li
import kotlinx.html.ul
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemSectionsPageViewModel

fun HTML.softwareSystemSectionsPage(viewModel: SoftwareSystemSectionsPageViewModel) {
    if (viewModel.onlyContainerSectionsVisible) {
        redirectRelative(
            viewModel.sectionTabs.first().link.relativeHref
        )
    } else if (viewModel.visible)
        softwareSystemPage(viewModel) {
            div(classes = "tabs") {
                ul(classes = "m-0 is-flex-wrap-wrap is-flex-shrink-1 is-flex-grow-0") {
                    viewModel.sectionTabs
                        .forEach {
                            li(classes = if (it.link.active) "is-active" else null) {
                                link(it.link)
                            }
                        }
                }
            }

            if (viewModel.softwareSystemSectionsVisible) {
                table(viewModel.sectionsTable)
            }
        }
    else
        redirectUpPage()
}
