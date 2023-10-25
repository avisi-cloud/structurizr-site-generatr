package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Format
import nl.avisi.structurizr.site.generatr.site.GeneratorContext
import org.intellij.lang.annotations.Language

class HomePageViewModel(generatorContext: GeneratorContext) : PageViewModel(generatorContext) {
    override val pageSubTitle = if (generatorContext.workspace.name.isNotBlank()) "" else "Home"
    override val url = url()

    val content = toHtml(
        this,
        content = generatorContext.workspace.documentation.sections
            .firstOrNull { it.order == 1 }?.content ?: DEFAULT_HOMEPAGE_CONTENT,
        format = generatorContext.workspace.documentation.sections
            .firstOrNull { it.order == 1 }?.format ?: Format.Markdown,
        svgFactory = generatorContext.svgFactory
    )

    companion object {
        fun url() = "/"

        @Language("markdown")
        const val DEFAULT_HOMEPAGE_CONTENT = """
## Home

This is the default home page. You can customize this home page by adding documentation pages to your workspace. For
example (see the [Structurizr DSL](https://github.com/structurizr/dsl/blob/master/docs/language-reference.md#documentation)
documentation for more information):

```text
workspace "Example workspace" {
    !docs docs
}
```

In the above example, the first document in the `docs` directory (after sorting alphabetically), will be used as the
homepage of the generated site.
"""
    }
}
