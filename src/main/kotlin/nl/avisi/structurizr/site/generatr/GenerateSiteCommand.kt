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
        "Comma-separated list of branches to include in the generated site. Not used if '--all-branches' option is set to true"
    ).default("master")
    private val defaultBranch by option(
        ArgType.String, "default-branch", "d",
        "The default branch"
    ).default("master")
    private val version by option(
        ArgType.String, "version", "v",
        "The version of the site"
    ).default("0.0.0")
    private val outputDir by option(
        ArgType.String, "output-dir", "o",
        "Directory where the generated site will be stored. Will be created if it doesn't exist yet."
    ).default("build/site")

    private val allBranches by option(
        ArgType.Boolean, "all-branches", "all",
        "When set to TRUE will generate a site for every branch in the git repository"
    ).default(value = false)
    private val excludeBranches by option(
        ArgType.String, "exclude-branches", "ex",
        "Comma-separated list of branches to exclude from the generated site"
    ).default("")

    override fun execute() {
        val siteDir = File(outputDir).apply { mkdirs() }
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

        val branchNames = if (allBranches)
            clonedRepository.getBranchNames(excludeBranches.split(","))
        else
            branches.split(",")

        println("The following branches will be checked for Structurizr Workspaces: $branchNames")

        val workspaceFileInRepo = File(clonedRepository.cloneDir, workspaceFile)
        val branchesToGenerate = branchNames.filter { branch ->
            println("Checking branch $branch")
            try {
                clonedRepository.checkoutBranch(branch)
                createStructurizrWorkspace(workspaceFileInRepo)
                true
            } catch (e: Exception) {
                println("Bad Branch $branch")
                false
            }
        }

        println("The following branches contain a valid Structurizr workspace: $branchesToGenerate")

        if (!branchesToGenerate.contains(defaultBranch)) {
            throw Exception("$defaultBranch does not contain a valid structurizr workspace. Site generation halted.")
        }

        branchesToGenerate.forEach { branch ->
            println("Generating site for branch $branch")
            clonedRepository.checkoutBranch(branch)

            val workspace = createStructurizrWorkspace(workspaceFileInRepo)
            generateDiagrams(workspace, File(siteDir, branch))
            generateSite(
                version,
                workspace,
                assetsDir?.let { File(cloneDir, it) },
                siteDir,
                branchesToGenerate,
                branch
            )
        }
    }

    private fun generateSiteForModel(siteDir: File) {
        val workspace = createStructurizrWorkspace(File(workspaceFile))
        generateDiagrams(workspace, File(siteDir, defaultBranch))
        generateSite(
            version,
            workspace,
            assetsDir?.let { File(it) },
            siteDir,
            listOf(defaultBranch),
            defaultBranch
        )
    }
}
