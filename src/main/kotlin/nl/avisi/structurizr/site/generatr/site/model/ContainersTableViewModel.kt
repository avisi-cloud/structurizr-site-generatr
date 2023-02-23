package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.Container

fun PageViewModel.createContainerTableViewModel(containers: Collection<Container>, hrefFactory: (Container) -> String) =
    TableViewModel.create {
        headerRow(headerCell("#"), headerCell("Container Decisions"))
        containers
            .sortedBy { it.name }
            .filter { it.documentation.decisions.isNotEmpty() }
            .forEachIndexed { index, container ->
                bodyRow(
                    cellWithIndex((index+1).toString()),
                    cellWithLink(this@createContainerTableViewModel, container.name, hrefFactory(container))
                )
            }
    }
