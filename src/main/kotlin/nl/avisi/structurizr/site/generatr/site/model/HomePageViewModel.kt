package nl.avisi.structurizr.site.generatr.site.model

import nl.avisi.structurizr.site.generatr.site.GeneratorContext
import org.intellij.lang.annotations.Language

class HomePageViewModel(generatorContext: GeneratorContext) : PageViewModel(generatorContext) {
    override val pageSubTitle = "Home"
    override val url = url()

    val content = MarkdownViewModel(
        markdown = generatorContext.workspace.documentation.sections
            .firstOrNull { it.order == 1 }?.content ?: DEFAULT_HOMEPAGE_CONTENT
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
