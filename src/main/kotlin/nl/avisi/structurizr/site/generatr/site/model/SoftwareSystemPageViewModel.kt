package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.*
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

open class SoftwareSystemPageViewModel(
    generatorContext: GeneratorContext,
    private val softwareSystem: SoftwareSystem,
    tab: Tab
) : PageViewModel(generatorContext) {
    enum class Tab { HOME, SYSTEM_CONTEXT, CONTAINER, COMPONENT, DEPLOYMENT, DEPENDENCIES, DECISIONS, SECTIONS }

    inner class TabViewModel(val tab: Tab, exactLink: Boolean = true) {
        val link = LinkViewModel(this@SoftwareSystemPageViewModel, title, url(softwareSystem, tab), exactLink)

        private val title
            get() = when (tab) {
                Tab.HOME -> "Info"
                Tab.SYSTEM_CONTEXT -> "Context views"
                Tab.CONTAINER -> "Container views"
                Tab.COMPONENT -> "Component views"
                Tab.DEPLOYMENT -> "Deployment views"
                Tab.DEPENDENCIES -> "Dependencies"
                Tab.DECISIONS -> "Decisions"
                Tab.SECTIONS -> "Documentation"
            }

        val visible
            get() = when (tab) {
                Tab.HOME -> true
                Tab.DEPENDENCIES -> true
                Tab.SYSTEM_CONTEXT -> generatorContext.workspace.views.hasSystemContextViews(softwareSystem)
                Tab.CONTAINER -> generatorContext.workspace.views.hasContainerViews(softwareSystem)
                Tab.COMPONENT -> generatorContext.workspace.views.hasComponentViews(softwareSystem)
                Tab.DEPLOYMENT -> generatorContext.workspace.views.hasDeploymentViews(softwareSystem)
                Tab.DECISIONS -> softwareSystem.hasDecisions() or softwareSystem.hasContainerDecisions()
                Tab.SECTIONS -> softwareSystem.hasDocumentationSections() or softwareSystem.hasContainerDocumentationSections()
            }
    }

    override val url: String = url(softwareSystem, tab)
    override val pageSubTitle: String = softwareSystem.name

    val tabs = listOf(
        TabViewModel(Tab.HOME),
        TabViewModel(Tab.SYSTEM_CONTEXT),
        TabViewModel(Tab.CONTAINER),
        TabViewModel(Tab.COMPONENT),
        TabViewModel(Tab.DEPLOYMENT),
        TabViewModel(Tab.DEPENDENCIES),
        TabViewModel(Tab.DECISIONS, exactLink = false),
        TabViewModel(Tab.SECTIONS, exactLink = false)
    )

    val description: String = softwareSystem.description

    companion object {
        fun url(softwareSystem: SoftwareSystem, tab: Tab): String {
            val home = "/${softwareSystem.name.normalize()}"
            return when (tab) {
                Tab.HOME -> home
                Tab.SYSTEM_CONTEXT -> "$home/context"
                Tab.CONTAINER -> "$home/container"
                Tab.COMPONENT -> "$home/component"
                Tab.DEPLOYMENT -> "$home/deployment"
                Tab.DEPENDENCIES -> "$home/dependencies"
                Tab.DECISIONS -> "$home/decisions"
                Tab.SECTIONS -> "$home/sections"
            }
        }
    }
}
