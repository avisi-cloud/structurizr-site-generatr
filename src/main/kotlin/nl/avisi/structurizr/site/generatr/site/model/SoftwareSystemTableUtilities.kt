package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem

fun TableViewModel.TableViewInitializerContext.softwareSystemCell(
    pageViewModel: PageViewModel,
    system: SoftwareSystem
) = if (pageViewModel.includedSoftwareSystems.contains(system))
    cellWithSoftwareSystemLink(
        pageViewModel,
        system.name,
        SoftwareSystemPageViewModel.url(system, SoftwareSystemPageViewModel.Tab.HOME)
    )
else
    cellWithExternalSoftwareSystem("${system.name} (External)")
