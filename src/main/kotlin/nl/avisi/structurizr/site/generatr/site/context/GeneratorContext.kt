package nl.avisi.structurizr.site.generatr.site.context

import com.structurizr.Workspace

data class GeneratorContext(
    val version: String,
    val workspace: Workspace,
    val contextPath: String,
    val branches: List<String>,
    val currentBranch: String
) {
    val siteUrlPrefix
        get() = if (contextPath.isBlank()) "" else "/$contextPath"
}
