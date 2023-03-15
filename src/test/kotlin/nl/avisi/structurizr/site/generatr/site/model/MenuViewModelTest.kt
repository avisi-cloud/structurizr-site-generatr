package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.structurizr.documentation.Decision
import com.structurizr.documentation.Format
import com.structurizr.model.Location
import nl.avisi.structurizr.site.generatr.site.GeneratorContext
import org.junit.jupiter.api.Test
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
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["main", "branch-2"])
    fun `software systems menu item if available`(currentBranch: String) {
        val generatorContext = generatorContext(branches = listOf("main", "branch-2"), currentBranch = currentBranch)
            .apply {
                workspace.model.addSoftwareSystem("Software system 1")
            }
        val pageViewModel = createPageViewModel(generatorContext)
        val viewModel = MenuViewModel(generatorContext, pageViewModel)

        assertThat(viewModel.generalItems[1]).isEqualTo(
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
        generatorContext.workspace.documentation.addSection(createSection("# Home"))
        val section1 = createSection("# Doc 1")
            .also { generatorContext.workspace.documentation.addSection(it) }
        val section2 = createSection(" Doc Title 2")
            .also { generatorContext.workspace.documentation.addSection(it) }
        val pageViewModel = createPageViewModel(generatorContext)
        val viewModel = MenuViewModel(generatorContext, pageViewModel)

        assertThat(viewModel.generalItems.drop(1)).containsExactly(
            LinkViewModel(pageViewModel, "Doc 1", WorkspaceDocumentationSectionPageViewModel.url(section1)),
            LinkViewModel(pageViewModel, "Doc Title 2", WorkspaceDocumentationSectionPageViewModel.url(section2))
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["main", "branch-2"])
    fun `links to software system pages sorted alphabetically case insensitive`(currentBranch: String) {
        val generatorContext = generatorContext(branches = listOf("main", "branch-2"), currentBranch = currentBranch)
        val system2 = generatorContext.workspace.model.addSoftwareSystem(Location.Internal, "System 2", "")
        val system1 = generatorContext.workspace.model.addSoftwareSystem(Location.Internal, "system 1", "")
        generatorContext.workspace.model.addSoftwareSystem(Location.External, "External", "")
        val pageViewModel = createPageViewModel(generatorContext)
        val viewModel = MenuViewModel(generatorContext, pageViewModel)

        assertThat(viewModel.softwareSystemItems).containsExactly(
            LinkViewModel(
                pageViewModel,
                "system 1",
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

    @Test
    fun `show menu entries for software systems with an unspecified location`() {
        val generatorContext = generatorContext(branches = listOf("main", "branch-2"), currentBranch = "main")
        generatorContext.workspace.model.addSoftwareSystem(Location.Unspecified, "System 1", "")

        MenuViewModel(generatorContext, createPageViewModel(generatorContext, url = HomePageViewModel.url()))
            .let {
                assertThat(it.softwareSystemItems).hasSize(1)
                assertThat(it.softwareSystemItems[0].title).isEqualTo("System 1")
            }
    }

    private fun createPageViewModel(generatorContext: GeneratorContext, url: String = "/master/page"): PageViewModel {
        return object : PageViewModel(generatorContext) {
            override val url = url
            override val pageSubTitle = "subtitle"
        }
    }
}
