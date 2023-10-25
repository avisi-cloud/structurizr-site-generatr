package nl.avisi.structurizr.site.generatr

import org.eclipse.jgit.api.CreateBranchCommand
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.ListBranchCommand
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import java.io.File

class ClonedRepository(
    val cloneDir: File,
    private val url: String,
    private val username: String?,
    private val password: String?
) {
    private val repo = FileRepositoryBuilder.create(File(cloneDir, ".git"))

    fun refreshLocalClone() {
        val credentialsProvider = if (username != null)
            UsernamePasswordCredentialsProvider(username, password)
        else
            null

        if (cloneDir.isDirectory) {
            Git(repo).pull()
                .setCredentialsProvider(credentialsProvider)
                .call()
        } else {
            cloneDir.deleteRecursively()
            val cloneCommand = Git.cloneRepository()
                .setURI(url)
                .setDirectory(cloneDir)
            if (credentialsProvider != null)
                cloneCommand.setCredentialsProvider(credentialsProvider)
            cloneCommand.call()
        }
    }

    fun checkoutBranch(branch: String) {
        val git = Git(repo)

        git.branchCreate()
            .setName(branch)
            .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.SET_UPSTREAM)
            .setStartPoint("origin/$branch")
            .setForce(true)
            .call()
        git.checkout()
            .setName(branch)
            .call()
    }

    fun getBranchNames(excludeBranches: List<String>) =
        Git(repo).branchList().setListMode(ListBranchCommand.ListMode.REMOTE).call()
            .map { it.name.toString().substringAfterLast("/") }
            .onEach { println("Found the following branch: $it") }
            .filter { it !in excludeBranches }

}
