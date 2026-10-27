package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.model.HeaderBarViewModel
import nl.avisi.structurizr.site.generatr.site.model.LinkViewModel
import nl.avisi.structurizr.site.generatr.site.model.MenuNodeViewModel
import nl.avisi.structurizr.site.generatr.site.model.MenuViewModel

fun DIV.menu(viewModel: MenuViewModel, nestGroups: Boolean, headerBar: HeaderBarViewModel) {
    aside(classes = "menu p-3") {
        id = "site-menu"
        searchSection(headerBar)
        generalSection(viewModel.generalItems)
        softwareSystemsSection(viewModel, nestGroups)
        branchesSection(headerBar)
        footerSection(headerBar)
    }
}

// Mobile-only sections (hidden on desktop, where the navbar provides this functionality)
private fun ASIDE.searchSection(headerBar: HeaderBarViewModel) {
    div(classes = "mobile-menu-search is-hidden-desktop") {
        input(classes = "input is-small is-rounded") {
            type = InputType.search
            placeholder = "Search..."
            onKeyUp = "redirect(event, value, '${headerBar.searchLink.relativeHref}')"
        }
    }
}

private fun ASIDE.branchesSection(headerBar: HeaderBarViewModel) {
    if (headerBar.branches.isEmpty()) return
    div(classes = "is-hidden-desktop") {
        p(classes = "menu-label") { +"Branches" }
        ul(classes = "menu-list has-site-branding") {
            headerBar.branches.forEach { branchLink ->
                li {
                    a(href = branchLink.relativeHref) { +branchLink.title }
                }
            }
        }
    }
}

private fun ASIDE.footerSection(headerBar: HeaderBarViewModel) {
    div(classes = "is-hidden-desktop") {
        if (headerBar.allowToggleTheme) {
            ul(classes = "menu-list has-site-branding") {
                li {
                    a {
                        role = "button"
                        onClick = "toggleTheme()"
                        +"Toggle theme"
                    }
                }
            }
        }
        p(classes = "menu-label") {
            span { +"v" }
            span { +headerBar.version }
        }
    }
}

private fun ASIDE.generalSection(items: List<LinkViewModel>) {
    p(classes = "menu-label") { +"General" }
    menuItemLinks(items)
}

private fun ASIDE.softwareSystemsSection(viewModel: MenuViewModel, nestGroups: Boolean) {
    p(classes = "menu-label") { +"Software systems" }
    if (nestGroups) {
        ul(classes = "listree menu-list has-site-branding") {
            buildHtmlTree(viewModel.softwareSystemNodes(), viewModel).invoke(this)
        }
    } else {
        menuItemLinks(viewModel.softwareSystemItems)
    }
}

private fun ASIDE.menuItemLinks(items: List<LinkViewModel>) {
    ul(classes = "menu-list has-site-branding") {
        li {
            items.forEach {
                link(it)
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
