package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.avisi.structurizr.site.generatr.site.asUrlToFile
import nl.avisi.structurizr.site.generatr.site.model.SearchViewModel

fun HTML.searchPage(viewModel: SearchViewModel) {
    page(viewModel) {
        contentDiv {
            h2 { +viewModel.pageSubTitle }
            script(
                type = ScriptType.textJavaScript,
                src = "https://cdn.jsdelivr.net/npm/lunr@2.3.9/lunr.min.js"
            ) { }
            script(
                type = ScriptType.textJavaScript,
                src = "https://cdn.jsdelivr.net/npm/lunr-languages@1.10.0/min/lunr.stemmer.support.min.js"
            ) { }

            script(type = ScriptType.textJavaScript) {
                unsafe {
                    +"const documents = ${Json.encodeToString(viewModel.documents)};"
                    +"""
                      const idx = lunr(function () {
                        this.ref('href')
                        this.field('text')

                        documents.forEach(function (doc) {
                          this.add(doc)
                        }, this)
                      });
                    """
                }
            }
            script(
                type = ScriptType.textJavaScript,
                src = "../" + "/search.js".asUrlToFile(viewModel.url)
            ) { }
            ul { id = "search-results" }
        }
    }
}
