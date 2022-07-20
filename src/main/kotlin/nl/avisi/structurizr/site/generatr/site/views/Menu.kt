package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.model.LinkViewModel
import nl.avisi.structurizr.site.generatr.site.model.MenuViewModel

fun DIV.menu(viewModel: MenuViewModel) {
    aside(classes = "menu p-3") {
        generalSection(viewModel.generalItems)
        softwareSystemsSection(viewModel.softwareSystemItems)
    }
}

private fun ASIDE.generalSection(items: List<LinkViewModel>) {
    p(classes = "menu-label") { +"General" }
    menuItemLinks(items)
}

private fun ASIDE.softwareSystemsSection(items: List<LinkViewModel>) {
    p(classes = "menu-label") { +"Software systems" }
    menuItemLinks(items)
}

private fun ASIDE.menuItemLinks(items: List<LinkViewModel>) {
    ul(classes = "menu-list") {
        li {
            items.forEach {
                link(it)
            }
        }
    }
}
