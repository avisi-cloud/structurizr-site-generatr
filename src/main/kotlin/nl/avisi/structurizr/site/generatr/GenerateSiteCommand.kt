@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
@file:OptIn(ExperimentalCli::class)

package nl.avisi.structurizr.site.generatr

import kotlinx.cli.*
import nl.avisi.structurizr.site.generatr.site.copySiteWideAssets
import nl.avisi.structurizr.site.generatr.site.generateDiagrams
import nl.avisi.structurizr.site.generatr.site.generateRedirectingIndexPage
import nl.avisi.structurizr.site.generatr.site.generateSite
import java.io.File

class GenerateSiteCommand : Subcommand(
    "generate-site",
    "Generate a site for the selected workspace."
) {
    private val gitUrl by option(
        ArgType.String, "git-url", "g",
        "The URL of the Git repository which contains the Structurizr model. " +
                "If a Git repository is provided, it will be cloned and" +
                "--workspace-file and --assets-dir will be treated as paths within the cloned repository. " +
                "If no Git repository is provided, --workspace-file and --assets-dir will be used as-is, and the site" +
                "will only contain one branch, named after the --default-branch option."
    )
    private val gitUsername by option(
        ArgType.String, "git-username", "u",
        "Username for the Git repository"
    )
    private val gitPassword by option(
        ArgType.String, "git-password", "p",
        "Password for the Git repository"
    )
    private val workspaceFile by option(
        ArgType.String, "workspace-file", "w",
        "Relative path within the Git repository of the workspace file"
    ).required()
    private val assetsDir by option(
        ArgType.String, "assets-dir", "a",
        "Relative path within the Git repository where static assets are located"
    )
    private val branches by option(
        ArgType.String, "branches", "b",
        "Comma-separated list of branches to include in the generated site"
    ).default("master")
    private val defaultBranch by option(
        ArgType.String, "default-branch", "d",
        "The default branch"
    ).default("master")
    private val version by option(
        ArgType.String, "version", "v",
        "The version of the site"
    ).default("0.0.0")

    override fun execute() {
        val siteDir = File("build/site").apply { mkdirs() }
        val gitUrl = gitUrl

        generateRedirectingIndexPage(siteDir, defaultBranch)
        copySiteWideAssets(siteDir)

        if (gitUrl != null)
            generateSiteForModelInGitRepository(gitUrl, siteDir)
        else
            generateSiteForModel(siteDir)
    }

    private fun generateSiteForModelInGitRepository(gitUrl: String, siteDir: File) {
        val cloneDir = File("build/model-clone")
        val clonedRepository = ClonedRepository(cloneDir, gitUrl, gitUsername, gitPassword).apply {
            refreshLocalClone()
        }

        val branchNames = branches.split(",")
        val workspaceFileInRepo = File(clonedRepository.cloneDir, workspaceFile)
        branchNames.forEach { branch ->
            println("Generating site for branch $branch")
            clonedRepository.checkoutBranch(branch)

            val workspace = parseStructurizrDslWorkspace(workspaceFileInRepo)
            generateDiagrams(workspace, File(siteDir, branch))
            generateSite(
                version,
                workspace,
                assetsDir?.let { File(cloneDir, it) },
                File(siteDir, branch),
                branchNames,
                branch
            )
        }
    }

    private fun generateSiteForModel(siteDir: File) {
        val workspace = parseStructurizrDslWorkspace(File(workspaceFile))
        generateDiagrams(workspace, File(siteDir, defaultBranch))
        generateSite(
            version,
            workspace,
            assetsDir?.let { File(it) },
            File(siteDir, defaultBranch),
            listOf(defaultBranch),
            defaultBranch
        )
    }
}
