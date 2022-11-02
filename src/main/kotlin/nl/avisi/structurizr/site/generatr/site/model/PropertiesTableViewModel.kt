package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.util.Url

fun createPropertiesTableViewModel(properties: Map<String, String>) =
    TableViewModel.create {
        headerRow(headerCell("Name"), headerCell("Value"))
        properties
            .toSortedMap()
            .forEach { (name, value) ->
                bodyRow(
                    cell(name),
                    if (Url.isUrl(value))
                        cellWithExternalLink(value, value)
                    else
                        cell(value)
                )
            }
    }
