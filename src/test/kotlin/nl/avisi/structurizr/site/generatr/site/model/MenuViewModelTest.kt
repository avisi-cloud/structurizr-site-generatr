package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.structurizr.documentation.Decision
import com.structurizr.documentation.Format
import com.structurizr.documentation.Section
import com.structurizr.model.Location
import nl.avisi.structurizr.site.generatr.site.GeneratorContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class MenuViewModelTest : ViewModelTest() {
    @ParameterizedTest
    @ValueSource(strings = ["main", "branch-2"])
    fun `fixed general items`(currentBranch: String) {
        val generatorContext = generatorContext(branches = listOf("main", "branch-2"), currentBranch = currentBranch)
        val pageViewModel = createPageViewModel(generatorContext)
        val viewModel = MenuViewModel(generatorContext, pageViewModel)

        assertThat(viewModel.generalItems).containsExactly(
            LinkViewModel(pageViewModel, "Home", HomePageViewModel.url()),
            LinkViewModel(pageViewModel, "Software Systems", SoftwareSystemsPageViewModel.url())
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["main", "branch-2"])
    fun `decisions menu item if available`(currentBranch: String) {
        val generatorContext =
            generatorContext(branches = listOf("main", "branch-2"), currentBranch = currentBranch).apply {
                workspace.documentation.addDecision(Decision("1").apply {
                    title = "Decision 1"
                    status = "Proposed"
                    format = Format.Markdown
                    content = "Content"
                })
            }
        val pageViewModel = createPageViewModel(generatorContext)
        val viewModel = MenuViewModel(generatorContext, pageViewModel)

        assertThat(viewModel.generalItems[1]).isEqualTo(
            LinkViewModel(pageViewModel, "Decisions", WorkspaceDecisionsPageViewModel.url(), false)
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["main", "branch-2"])
    fun `workspace-level documentation in general section`(currentBranch: String) {
        val generatorContext = generatorContext(branches = listOf("main", "branch-2"), currentBranch = currentBranch)
        generatorContext.workspace.documentation.addSection(Section("Home", Format.Markdown, "content"))
        val section1 = Section("Doc 1", Format.Markdown, "content")
            .also { generatorContext.workspace.documentation.addSection(it) }
        val section2 = Section("Doc Title 2", Format.Markdown, "content")
            .also { generatorContext.workspace.documentation.addSection(it) }
        val pageViewModel = createPageViewModel(generatorContext)
        val viewModel = MenuViewModel(generatorContext, pageViewModel)

        assertThat(viewModel.generalItems.drop(2)).containsExactly(
            LinkViewModel(pageViewModel, "Doc 1", WorkspaceDocumentationSectionPageViewModel.url(section1)),
            LinkViewModel(pageViewModel, "Doc Title 2", WorkspaceDocumentationSectionPageViewModel.url(section2))
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["main", "branch-2"])
    fun `links to software system pages`(currentBranch: String) {
        val generatorContext = generatorContext(branches = listOf("main", "branch-2"), currentBranch = currentBranch)
        val system2 = generatorContext.workspace.model.addSoftwareSystem(Location.Internal, "System 2", "")
        val system1 = generatorContext.workspace.model.addSoftwareSystem(Location.Internal, "System 1", "")
        generatorContext.workspace.model.addSoftwareSystem(Location.External, "External", "")
        val pageViewModel = createPageViewModel(generatorContext)
        val viewModel = MenuViewModel(generatorContext, pageViewModel)

        assertThat(viewModel.softwareSystemItems).containsExactly(
            LinkViewModel(
                pageViewModel,
                "System 1",
                SoftwareSystemPageViewModel.url(system1, SoftwareSystemPageViewModel.Tab.HOME),
                false
            ),
            LinkViewModel(
                pageViewModel,
                "System 2",
                SoftwareSystemPageViewModel.url(system2, SoftwareSystemPageViewModel.Tab.HOME),
                false
            ),
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["main", "branch-2"])
    fun `active links`(currentBranch: String) {
        val generatorContext = generatorContext(branches = listOf("main", "branch-2"), currentBranch = currentBranch)
        val system = generatorContext.workspace.model.addSoftwareSystem(Location.Internal, "System 1", "")

        MenuViewModel(generatorContext, createPageViewModel(generatorContext, url = HomePageViewModel.url()))
            .let { assertThat(it.generalItems[0].active).isTrue() }
        MenuViewModel(
            generatorContext, createPageViewModel(
                generatorContext, url = SoftwareSystemPageViewModel.url(
                    system,
                    SoftwareSystemPageViewModel.Tab.HOME
                )
            )
        )
            .let { assertThat(it.softwareSystemItems[0].active).isTrue() }
    }

    private fun createPageViewModel(generatorContext: GeneratorContext, url: String = "/master/page"): PageViewModel {
        return object : PageViewModel(generatorContext) {
            override val url = url
            override val pageSubTitle = "subtitle"
        }
    }
}
