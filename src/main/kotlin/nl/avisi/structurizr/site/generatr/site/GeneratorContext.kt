package nl.avisi.structurizr.site.generatr.site

import com.structurizr.Workspace
import java.io.File

data class GeneratorContext(
    val version: String,
    val workspace: Workspace,
    val exportDir: File,
    val branches: List<String>,
    val currentBranch: String
)
