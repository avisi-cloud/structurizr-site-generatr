package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.model.LinkViewModel
import nl.avisi.structurizr.site.generatr.site.model.MenuViewModel

fun DIV.menu(viewModel: MenuViewModel) {
    nav(classes = "menu p-3") {
        generalSection(viewModel.generalItems)
        softwareSystemsSection(viewModel.softwareSystemItems)
    }
}

private fun NAV.generalSection(items: List<LinkViewModel>) {
    p(classes = "menu-label") { +"General" }
    menuItemLinks(items)
}

private fun NAV.softwareSystemsSection(items: List<LinkViewModel>) {
    p(classes = "menu-label") { +"Software systems" }
    menuItemLinks(items)
}

private fun NAV.menuItemLinks(items: List<LinkViewModel>) {
    ul(classes = "menu-list has-site-branding") {
        li {
            items.forEach {
                link(it)
            }
        }
    }
}
