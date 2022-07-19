package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.Workspace
import com.structurizr.documentation.Decision
import com.structurizr.documentation.Format
import nl.avisi.structurizr.site.generatr.site.context.GeneratorContext
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

abstract class ViewModelTest {
    protected fun generatorContext(
        workspaceName: String = "workspace name",
        branches: List<String> = listOf("main"),
        currentBranch: String = "main",
        version: String = "1.0.0"
    ) = GeneratorContext(version, Workspace(workspaceName, ""), branches, currentBranch)

    protected fun pageViewModel(pageHref: String = "/some-page") = object : PageViewModel(generatorContext()) {
        override val url = pageHref
        override val pageSubTitle = "Some page"
    }

    protected fun createDecision(id: String, decisionStatus: String, decisionDate: LocalDate = LocalDate.now()) =
        Decision(id).apply {
            title = "Decision $id"
            status = decisionStatus
            date = Date.from(decisionDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
            format = Format.Markdown
            content = "Decision $id content"
        }
}
