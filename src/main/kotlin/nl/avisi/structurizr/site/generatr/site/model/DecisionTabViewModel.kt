package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.hasDecisions

data class DecisionTabViewModel(val pageViewModel: SoftwareSystemPageViewModel, val title: String, val visible: Boolean, val url: String) {
    val link = LinkViewModel(pageViewModel, title, url, true)
}

fun SoftwareSystemPageViewModel.createDecisionsTabViewModel(softwareSystem: SoftwareSystem, tab: SoftwareSystemPageViewModel.Tab): List<DecisionTabViewModel> {
    val softwareTab = DecisionTabViewModel(
            this,
            "System",
            softwareSystem.hasDecisions(),
            SoftwareSystemPageViewModel.url(softwareSystem, tab)
    )
    val containerTabs = softwareSystem
            .containers
            .map {
                DecisionTabViewModel(
                        this,
                        it.name,
                        it.hasDecisions(),
                        SoftwareSystemContainerDecisionsPageViewModel.url(it)
                )
            }
    return listOf(softwareTab).plus(containerTabs)
}
