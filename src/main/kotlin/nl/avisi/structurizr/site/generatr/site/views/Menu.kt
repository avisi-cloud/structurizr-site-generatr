package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.model.LinkViewModel
import nl.avisi.structurizr.site.generatr.site.model.MenuNodeViewModel
import nl.avisi.structurizr.site.generatr.site.model.MenuViewModel

fun DIV.menu(viewModel: MenuViewModel, nestGroups: Boolean) {
    nav(){
        generalSection(viewModel.generalItems)
        softwareSystemsSection(viewModel, nestGroups)
    }
}

private fun NAV.generalSection(items: List<LinkViewModel>) {
    h2("app-side-nav__heading") { +"General" }
    menuItemLinks(items)
}

private fun NAV.softwareSystemsSection(viewModel: MenuViewModel, nestGroups: Boolean) {
    h2("app-side-nav__heading") { +"Software systems" }
    if (nestGroups) {
        ul(classes = "nhsuk-list app-side-nav__list") {
            buildHtmlTree(viewModel.softwareSystemNodes(), viewModel).invoke(this)
        }
    } else {
        menuItemLinks(viewModel.softwareSystemItems)
    }
}

private fun NAV.menuItemLinks(items: List<LinkViewModel>) {
    ul(classes = "nhsuk-list app-side-nav__list") {
        items.forEach {
            li(classes=if (it.active) "app-side-nav__item--current" else "app-side-nav__item")  {
                link(it, "app-side-nav__link")
            }
        }
    }
}

private fun buildHtmlTree(node: MenuNodeViewModel, viewModel: MenuViewModel): UL.() -> Unit = {
    if (node.name.isNotEmpty() && node.children.isEmpty()) {
        val itemLink = viewModel.softwareSystemItems.find { it.title == node.name }
        li {
            if (itemLink != null) {
                link(itemLink)
            }
        }
    }

    if (node.name.isNotEmpty() && node.children.isNotEmpty()) {
        li {
            div(classes = "listree-submenu-heading") {
                +node.name
            }
            ul(classes = "listree-submenu-items") {
                for (child in node.children) {
                    buildHtmlTree(child, viewModel).invoke(this)
                }
            }
        }
    }

    if (node.name.isEmpty() && node.children.isNotEmpty()) {
        ul(classes = "listree-submenu-items") {
            for (child in node.children) {
                buildHtmlTree(child, viewModel).invoke(this)
            }
        }
    }
}
