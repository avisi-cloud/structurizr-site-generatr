package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.Workspace
import nl.avisi.structurizr.site.generatr.site.context.GeneratorContext

abstract class ViewModelTest {
    protected fun generatorContext(
        workspaceName: String = "workspace name",
        branches: List<String> = listOf("main"),
        currentBranch: String = "main",
        version: String = "1.0.0"
    ) = GeneratorContext(version, Workspace(workspaceName, ""), branches, currentBranch)
}
