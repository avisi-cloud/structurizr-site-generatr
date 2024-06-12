package nl.avisi.structurizr.site.generatr.site.views

import com.structurizr.Workspace
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.lang.IllegalStateException

class CDN(val workspace: Workspace) {
    private val cdnBaseURL = getCdnBaseUrl()
    private val json = Json { ignoreUnknownKeys = true }

    private val packageJson = object {}.javaClass.getResource("/package.json")?.readText()
        ?: throw IllegalStateException("package.json not found")

    private val dependencies = json.parseToJsonElement(packageJson).jsonObject["dependencies"]
        ?.jsonObject
        ?.map { Dependency(cdnBaseURL, it.key, it.value.jsonPrimitive.content) }
        ?: throw IllegalStateException("dependencies element not found in package.json")

    fun bulmaCss() = dependencies.single { it.name == "bulma" }.let {
        "${it.baseUrl()}/css/bulma.min.css"
    }

    fun katexJs() = dependencies.single { it.name == "katex" }.let {
        "${it.baseUrl()}/dist/katex.min.js"
    }

    fun katexCss() = dependencies.single { it.name == "katex" }.let {
        "${it.baseUrl()}/dist/katex.min.css"
    }

    fun lunrJs() = dependencies.single { it.name == "lunr" }.let {
        "${it.baseUrl()}/lunr.min.js"
    }

    fun lunrLanguagesStemmerJs() = dependencies.single { it.name == "lunr-languages" }.let {
        "${it.baseUrl()}/min/lunr.stemmer.support.min.js"
    }

    fun lunrLanguagesJs(language: String) = dependencies.single { it.name == "lunr-languages" }.let {
        "${it.baseUrl()}/min/lunr.$language.min.js"
    }

    fun mermaidJs() = dependencies.single { it.name == "mermaid" }.let {
        "${it.baseUrl()}/dist/mermaid.esm.min.mjs"
    }

    fun svgpanzoomJs() = dependencies.single { it.name == "svg-pan-zoom" }.let {
        "${it.baseUrl()}/dist/svg-pan-zoom.min.js"
    }

    fun webfontloaderJs() = dependencies.single { it.name == "webfontloader" }.let {
        "${it.baseUrl()}/webfontloader.js"
    }

    fun fontAwesomeCss() = dependencies.single { it.name == "font-awesome"}.let {
        "${it.baseUrl()}/css/font-awesome.min.css"
    }

    fun getCdnBaseUrl() = workspace.views.configuration.properties
        .getOrDefault("generatr.site.cdn", "https://cdn.jsdelivr.net/npm")
        .trimEnd('/')

    @Serializable
    data class Dependency(val cdnBaseURL: String, val name: String, val version: String) {
        fun baseUrl() = "$cdnBaseURL/$name@$version"
    }
}
