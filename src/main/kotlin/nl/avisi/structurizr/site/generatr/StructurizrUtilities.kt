package nl.avisi.structurizr.site.generatr

import com.structurizr.documentation.Documentation
import com.structurizr.documentation.Format
import com.structurizr.documentation.Section
import com.structurizr.dsl.StructurizrDslParser
import com.structurizr.model.Element
import com.structurizr.model.Location
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import org.intellij.lang.annotations.Language
import java.io.File

@Language("markdown")
private const val DEFAULT_HOME_SECTION = """
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

fun parseStructurizrDslWorkspace(workspaceFile: File) =
    StructurizrDslParser()
        .apply { parse(workspaceFile) }
        .workspace
        ?: throw IllegalStateException("Workspace could not be parsed")

val Documentation.homeSection: Section
    get() =
        sections.firstOrNull { it.order == 1 } ?: Section("Home", Format.Markdown, DEFAULT_HOME_SECTION)

val Element.normalizedName: String
    get() = name.normalize()

fun String.normalize(): String = lowercase().replace("\\s+".toRegex(), "-")

val Model.internalSoftwareSystems: List<SoftwareSystem>
    get() = softwareSystems.filter { it.location == Location.Internal }
