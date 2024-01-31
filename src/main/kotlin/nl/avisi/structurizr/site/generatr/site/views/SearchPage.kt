package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.avisi.structurizr.site.generatr.site.asUrlToFile
import nl.avisi.structurizr.site.generatr.site.model.SearchViewModel

fun HTML.searchPage(viewModel: SearchViewModel) {
    val language = if (viewModel.language == "en") "" else viewModel.language
    if (!supportedLanguages.contains(language))
        throw IllegalArgumentException(
            "Indexing language $language is not supported, available languages are $supportedLanguages"
        )

    page(viewModel) {
        contentDiv {
            h2 { +viewModel.pageSubTitle }
            script(
                type = ScriptType.textJavaScript,
                src = CDN.lunrJs()
            ) { }
            script(
                type = ScriptType.textJavaScript,
                src = CDN.lunrLanguagesStemmerJs()
            ) { }
            if (language.isNotBlank())
                script(
                    type = ScriptType.textJavaScript,
                    src = CDN.lunrLanguagesJs(language)
                ) { }

            script(type = ScriptType.textJavaScript) {
                unsafe {
                    +"const documents = ${Json.encodeToString(viewModel.documents)};"
                    +"const idx = lunr(function () {"
                    if (viewModel.language.isNotBlank())
                        +"this.use(lunr.${viewModel.language});"
                    +"""
                        this.ref('id')
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
            div { id = "search-results" }
        }
    }
}

// From https://github.com/olivernn/lunr-languages
private val supportedLanguages = listOf(
    "",
    "ar",
    "da",
    "de",
    "du",
    "es",
    "fi",
    "fr",
    "hi",
    "hu",
    "it",
    "ja",
    "jp",
    "ko",
    "nl",
    "no",
    "pt",
    "ro",
    "ru",
    "sv",
    "ta",
    "th",
    "tr",
    "vi",
    "zh",
)
