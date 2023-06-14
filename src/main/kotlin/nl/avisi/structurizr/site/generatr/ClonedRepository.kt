package nl.avisi.structurizr.site.generatr

import org.eclipse.jgit.api.CreateBranchCommand
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.ListBranchCommand
import org.eclipse.jgit.lib.Ref
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

    fun getBranchNames(excludeBranches: List<String>): List<String> {
        val branches:List<Ref> = Git(repo).branchList().setListMode(ListBranchCommand.ListMode.ALL).call()
        val branchNames:MutableList<String> = mutableListOf()
        for (branch:Ref in branches){
            val shortBranchName: String = branch.name.toString().substringAfterLast("/")
            println("Found the following Branch $shortBranchName")
            if(!excludeBranches.contains(shortBranchName)){
                branchNames.add(shortBranchName)
            }
        }
        return branchNames
    }
}
