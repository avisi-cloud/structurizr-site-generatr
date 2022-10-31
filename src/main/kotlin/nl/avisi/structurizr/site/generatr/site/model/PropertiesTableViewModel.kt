package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.util.Url

fun createPropertiesTableViewModel(properties: Map<String, String>) =
    TableViewModel.create {
        headerRow(headerCell("Name"), headerCell("Value"))
        properties
            .toSortedMap()
            .forEach {
                bodyRow(
                    cell(it.key),
                    if (Url.isUrl(it.value))
                        cellWithExternalLink(it.value, it.value)
                    else
                        cell(it.value)
                )
            }
    }
