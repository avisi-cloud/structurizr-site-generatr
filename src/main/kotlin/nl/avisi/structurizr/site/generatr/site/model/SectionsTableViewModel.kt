package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Section
import com.structurizr.model.SoftwareSystem
import com.structurizr.model.StaticStructureElement
import nl.avisi.structurizr.site.generatr.hasDocumentationSections
import nl.avisi.structurizr.site.generatr.hasSections

fun PageViewModel.createSectionsTableViewModel(sections: Collection<Section>, dropFirst: Boolean = true, hrefFactory: (Section) -> String) =
    TableViewModel.create {
        headerRow(
            headerCellSmall("#"),
            headerCell("Title")
        )

        sections
            .sortedBy { it.order }
            .drop(if (dropFirst) 1 else 0)
            .associateWith { if (dropFirst) it.order - 1 else it.order }
            .forEach { (section, index) ->
                bodyRow(
                    cellWithIndex(index.toString()),
                    cellWithLink(this@createSectionsTableViewModel, section.contentTitle(), hrefFactory(section))
                )
            }
    }

fun SoftwareSystemPageViewModel.createSectionsTabViewModel(
    softwareSystem: SoftwareSystem,
    tab: SoftwareSystemPageViewModel.Tab,
    linkMatch: (StaticStructureElement) -> Match = { Match.EXACT }
) = buildList {
    if (softwareSystem.hasDocumentationSections()) {
        add(
            SectionTabViewModel(
                this@createSectionsTabViewModel,
                "System",
                SoftwareSystemPageViewModel.url(softwareSystem, tab),
                linkMatch(softwareSystem)
            )
        )
    }
    softwareSystem
        .containers
        .filter { it.hasSections() }
        .map {
            SectionTabViewModel(
                this@createSectionsTabViewModel,
                it.name,
                SoftwareSystemContainerSectionsPageViewModel.url(it),
                linkMatch(it)
            )
        }
        .forEach { add(it) }
}
