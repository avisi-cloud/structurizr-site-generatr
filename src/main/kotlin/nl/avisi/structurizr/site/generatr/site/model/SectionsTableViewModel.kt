package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Section

fun PageViewModel.createSectionsTableViewModel(sections: Collection<Section>, hrefFactory: (Section) -> String) =
    TableViewModel.create {
        headerRow(headerCell("#"), headerCell("Title"))
        sections
            .sortedBy { it.order }
            .drop(1)
            .associateWith { it.order - 1 }
            .forEach { (section, index) ->
                bodyRow(
                    cellWithIndex(index.toString()),
                    cellWithLink(this@createSectionsTableViewModel, section.title(), hrefFactory(section))
                )
            }
    }
