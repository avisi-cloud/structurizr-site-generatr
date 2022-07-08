package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem

data class SoftwareSystemTabBarViewModel(val tabs: List<TabViewModel>) {
    companion object {
        fun create(pageViewModel: PageViewModel, softwareSystem: SoftwareSystem): SoftwareSystemTabBarViewModel {
            return SoftwareSystemTabBarViewModel(
                listOf(
                    TabViewModel(
                        LinkViewModel(pageViewModel, "Info", SoftwareSystemHomePageViewModel.url(softwareSystem)),
                        true
                    )
                )
            )
        }
    }
}

data class TabViewModel(val link: LinkViewModel, val active: Boolean)
