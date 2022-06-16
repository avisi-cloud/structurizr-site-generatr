package nl.avisi.structurizr.site.generatr

import org.eclipse.jgit.api.CreateBranchCommand
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import java.io.File

class ClonedRepository(
    val cloneDir: File,
    private val url: String,
    private val username: String,
    private val password: String
) {
    private val repo = FileRepositoryBuilder.create(File(cloneDir, ".git"))

    fun refreshLocalClone() {
        val credentialsProvider = UsernamePasswordCredentialsProvider(username, password)

        if (cloneDir.isDirectory) {
            Git(repo).pull()
                .setCredentialsProvider(credentialsProvider)
                .call()
        } else {
            cloneDir.deleteRecursively()
            Git.cloneRepository()
                .setURI(url)
                .setDirectory(cloneDir)
                .setCredentialsProvider(credentialsProvider)
                .call()
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
}
