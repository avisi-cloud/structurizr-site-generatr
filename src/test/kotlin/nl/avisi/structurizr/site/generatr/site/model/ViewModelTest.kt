package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.Workspace
import com.structurizr.documentation.Decision
import com.structurizr.documentation.Format
import com.structurizr.documentation.Section
import com.structurizr.model.Element
import com.structurizr.view.ImageView
import nl.avisi.structurizr.site.generatr.site.GeneratorContext
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

abstract class ViewModelTest {
    protected val svgFactory = { _: String, _: String -> """<svg viewBox="0 0 800 900"></svg>""" to """<svg viewBox="0 0 800 900"></svg>""" }

    protected fun generatorContext(
        workspaceName: String = "Workspace name",
        branches: List<String> = listOf("main"),
        currentBranch: String = "main",
        version: String = "1.0.0"
    ) = GeneratorContext(version, Workspace(workspaceName, ""), branches, currentBranch, false, svgFactory)

    protected fun pageViewModel(pageHref: String = "/some-page") = object : PageViewModel(generatorContext()) {
        override val url = pageHref
        override val pageSubTitle = "Some page"
    }

    protected fun createDecision(
        id: String = "1",
        decisionStatus: String = "Accepted",
        decisionDate: LocalDate = LocalDate.now()
    ) =
        Decision(id).apply {
            title = "Decision $id"
            status = decisionStatus
            date = Date.from(decisionDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
            format = Format.Markdown
            content = "Decision $id content"
        }

    protected fun createSection(content: String = "# Content") = Section(Format.Markdown, content)

    protected fun createImageView(workspace: Workspace, element: Element, key: String = "imageview-${element.id}", title: String? = "Image View Title"): ImageView = workspace.views.createImageView(element, key).also {
        it.description = "Image View Description"
        it.title = title
        it.contentType = "image/png"
        it.content = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII="
    }
}
