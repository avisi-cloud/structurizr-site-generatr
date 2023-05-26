package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.Container

fun PageViewModel.createContainerDecisionsTableViewModel(containers: Collection<Container>, hrefFactory: (Container) -> String) =
    TableViewModel.create {
        headerRow(headerCell("#"), headerCell("Container Decisions"))
        containers
            .sortedBy { it.name }
            .filter { it.documentation.decisions.isNotEmpty() }
            .forEachIndexed { index, container ->
                bodyRow(
                    cellWithIndex((index+1).toString()),
                    cellWithLink(this@createContainerDecisionsTableViewModel, container.name, hrefFactory(container))
                )
            }
    }
