package nl.avisi.structurizr.site.generatr.site.e2e

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler

class RetryExtension : TestExecutionExceptionHandler {
    companion object {
        private const val MAX_RETRIES = 3

        private val namespace: ExtensionContext.Namespace = ExtensionContext.Namespace.create("RetryExtension")
    }

    override fun handleTestExecutionException(
        context: ExtensionContext,
        throwable: Throwable
    ) {
        val store = context.getStore(namespace)
        val retries = store.getOrDefault("retries", Int::class.java, 0)

        if (retries < MAX_RETRIES) {
            store.put("retries", retries + 1)

            println("Retrying test " + context.displayName + ", attempt " + store.get("retries"))
        } else {
            throw throwable
        }
    }
}