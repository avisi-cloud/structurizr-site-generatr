package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.normalize
import nl.avisi.structurizr.site.generatr.site.context.GeneratorContext

open class SoftwareSystemPageViewModel(
    generatorContext: GeneratorContext,
    private val softwareSystem: SoftwareSystem,
    tab: Tab
) : PageViewModel(generatorContext) {
    enum class Tab { HOME, SYSTEM_CONTEXT }

    inner class TabViewModel(val tab: Tab) {
        val link = LinkViewModel(this@SoftwareSystemPageViewModel, title, url(softwareSystem, tab))

        private val title
            get() = when (tab) {
                Tab.HOME -> "Info"
                Tab.SYSTEM_CONTEXT -> "Context"
            }

        val visible
            get() = when (tab) {
                Tab.HOME -> true
                Tab.SYSTEM_CONTEXT -> generatorContext.workspace.views.systemContextViews.any { it.softwareSystem == softwareSystem }
            }
    }

    override val url: String = url(softwareSystem, tab)
    override val pageSubTitle: String = softwareSystem.name

    val tabs = listOf(
        TabViewModel(Tab.HOME), TabViewModel(Tab.SYSTEM_CONTEXT),
    )

    val description: String = softwareSystem.description

    companion object {
        fun url(softwareSystem: SoftwareSystem, tab: Tab) =
            when (tab) {
                Tab.HOME -> "/${softwareSystem.name.normalize()}"
                Tab.SYSTEM_CONTEXT -> "/${softwareSystem.name.normalize()}/context"
            }
    }
}
