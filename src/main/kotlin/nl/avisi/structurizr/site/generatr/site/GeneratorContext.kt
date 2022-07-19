package nl.avisi.structurizr.site.generatr.site

import com.structurizr.Workspace

data class GeneratorContext(
    val version: String,
    val workspace: Workspace,
    val branches: List<String>,
    val currentBranch: String
)
