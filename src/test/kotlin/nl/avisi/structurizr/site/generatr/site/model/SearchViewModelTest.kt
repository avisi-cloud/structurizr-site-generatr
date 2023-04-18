package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import com.structurizr.documentation.Format
import com.structurizr.documentation.Section
import org.junit.jupiter.api.Test

class SearchViewModelTest : ViewModelTest() {

    @Test
    fun `no index language configured`() {
        val viewModel = SearchViewModel(generatorContext())

        assertThat(viewModel.language).isEmpty()
    }

    @Test
    fun `index language configured`() {
        val generatorContext = generatorContext().apply {
            workspace.views.configuration.addProperty("structurizr.style.search.language", "nl")
        }
        val viewModel = SearchViewModel(generatorContext)

        assertThat(viewModel.language).isEqualTo("nl")
    }

    @Test
    fun `nothing to index`() {
        val viewModel = SearchViewModel(generatorContext())

        assertThat(viewModel.documents).hasSize(0)
    }

    @Test
    fun `indexes global pages`() {
        val generatorContext = generatorContext()
        generatorContext.workspace.apply {
            documentation.addSection(createSection("# Home\nSome info"))
            documentation.addSection(createSection("# Landscape\nMore info"))
            documentation.addDecision(createDecision())
        }
        val viewModel = SearchViewModel(generatorContext)

        assertThat(viewModel.documents.map { it.type })
            .containsExactly("Home", "Workspace Decision", "Workspace Documentation")
    }

    @Test
    fun `indexes software system without additional information`() {
        val generatorContext = generatorContext()
        generatorContext.workspace.apply {
            model.addSoftwareSystem("Software system")
        }
        val viewModel = SearchViewModel(generatorContext)

        assertThat(viewModel.documents.map { it.type })
            .containsExactly("Context views")
    }

    @Test
    fun `indexes all software system information`() {
        val generatorContext = generatorContext()
        generatorContext.workspace.apply {
            model.addSoftwareSystem("Software system").apply {
                documentation.addSection(Section(Format.Markdown, "# Introduction\nSome info"))
                addContainer("Container 1", "a container").apply {
                    addComponent("Component 1", "a component")
                }
                documentation.addDecision(createDecision("1"))
                documentation.addSection(Section(Format.Markdown, "# Usage\nThat's how it works"))
            }
        }
        val viewModel = SearchViewModel(generatorContext)

        assertThat(viewModel.documents.map { it.type })
            .containsExactly(
                "Software System Info",
                "Context views",
                "Container views",
                "Component views",
                "Decision",
                "Documentation"
            )
    }

    @Test
    fun `indexes software systems with relations`() {
        val generatorContext = generatorContext()
        generatorContext.workspace.apply {
            val system1 = model.addSoftwareSystem("Software System 1", "One system to rule them all")
            val system2 = model.addSoftwareSystem("Software System 2", "One system to rule the others")
            system1.uses(system2, "reads")
            system2.uses(system1, "writes", "tech 1")
        }
        val viewModel = SearchViewModel(generatorContext)

        assertThat(viewModel.documents.map { it.type })
            .containsExactly("Context views", "Dependencies", "Context views", "Dependencies")
    }
}
