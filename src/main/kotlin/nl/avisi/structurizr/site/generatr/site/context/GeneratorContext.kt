package nl.avisi.structurizr.site.generatr.site.context

import com.structurizr.Workspace

data class GeneratorContext(
    val version: String,
    val workspace: Workspace,
    val branches: List<String>,
    val currentBranch: String
)
