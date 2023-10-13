package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.GeneratorContext
import nl.avisi.structurizr.site.generatr.site.model.LinkViewModel
import nl.avisi.structurizr.site.generatr.site.model.MenuNodeViewModel
import nl.avisi.structurizr.site.generatr.site.model.MenuViewModel

fun DIV.menu(viewModel: MenuViewModel, nestGroups:Boolean) {
    aside(classes = "menu p-3") {
        generalSection(viewModel.generalItems)
        softwareSystemsSection(viewModel, nestGroups)
    }
}

private fun ASIDE.generalSection(items: List<LinkViewModel>) {
    p(classes = "menu-label") { +"General" }
    menuItemLinks(items)
}

private fun ASIDE.softwareSystemsSection(viewModel: MenuViewModel, nestGroups:Boolean) {
    p(classes = "menu-label") { +"Software systems" }
    if(nestGroups){
        ul(classes = "listree menu-list has-site-branding"){
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

    if (node.name.isNotEmpty()  && node.children.isNotEmpty()) {
        li {
            if(node.name == "null"){
                div(classes = "listree-submenu-heading") {
                    +"No Group"
                }
            } else {
                div(classes = "listree-submenu-heading") {
                    +node.name
                }
            }

            ul(classes = "listree-submenu-items") {
                for (child in node.children) {
                    buildHtmlTree(child,viewModel).invoke(this)
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
