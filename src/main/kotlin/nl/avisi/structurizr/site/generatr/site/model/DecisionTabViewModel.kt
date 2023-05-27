package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.hasDecisions

data class DecisionTabViewModel(val pageViewModel: SoftwareSystemPageViewModel, val title: String, val url: String) {
    val link = LinkViewModel(pageViewModel, title, url, true)
}

fun SoftwareSystemPageViewModel.createDecisionsTabViewModel(softwareSystem: SoftwareSystem, tab: SoftwareSystemPageViewModel.Tab): List<DecisionTabViewModel> {
    val tabs = buildList<DecisionTabViewModel> {
        if (softwareSystem.hasDecisions()) {
            add(DecisionTabViewModel(
                    this@createDecisionsTabViewModel,
                    "System",
                    SoftwareSystemPageViewModel.url(softwareSystem, tab)
            ))
        }
        softwareSystem
                .containers
                .filter { it.hasDecisions() }
                .map {
                    DecisionTabViewModel(
                            this@createDecisionsTabViewModel,
                            it.name,
                            SoftwareSystemContainerDecisionsPageViewModel.url(it)
                    )
                }
                .forEach { add(it) }
    }
    return tabs
}
