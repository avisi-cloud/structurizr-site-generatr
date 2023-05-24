package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.asUrlToFile
import nl.avisi.structurizr.site.generatr.site.model.PageViewModel

fun HEAD.markdownAdmonitionStylesheet(viewModel: PageViewModel) {
    link(
        rel = "stylesheet",
        href = "../" + "/admonition.css".asUrlToFile(viewModel.url)
    )
}

fun BODY.markdownAdmonitionScript(viewModel: PageViewModel) {
    script(
        type = ScriptType.textJavaScript,
        src = "../" + "/admonition.js".asUrlToFile(viewModel.url)
    ) { }
}

fun HEAD.katexStylesheet() {
    // loading KaTeX as global on a webpage: https://katex.org/docs/browser.html#loading-as-global
    unsafe { 
        raw("""
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/katex@0.16.6/dist/katex.min.css" integrity="sha384-mXD7x5S50Ko38scHSnD4egvoExgMPbrseZorkbE49evAfv9nNcbrXJ8LLNsDgh9d" crossorigin="anonymous">
        """) 
    }
}

fun HEAD.katexScript() {
    // loading KaTeX as global on a webpage: https://katex.org/docs/browser.html#loading-as-global
    unsafe { 
        raw("""
            <script defer src="https://cdn.jsdelivr.net/npm/katex@0.16.6/dist/katex.min.js" integrity="sha384-j/ZricySXBnNMJy9meJCtyXTKMhIJ42heyr7oAdxTDBy/CYA9hzpMo+YTNV5C+1X" crossorigin="anonymous"></script>
        """)
    }
}

fun HEAD.katexFonts() {
    // loading KaTeX as global on a webpage: https://katex.org/docs/browser.html#loading-as-global
    unsafe {
        raw("""
            <script>
            window.WebFontConfig = {
                custom: {
                families: ['KaTeX_AMS', 'KaTeX_Caligraphic:n4,n7', 'KaTeX_Fraktur:n4,n7',
                    'KaTeX_Main:n4,n7,i4,i7', 'KaTeX_Math:i4,i7', 'KaTeX_Script',
                    'KaTeX_SansSerif:n4,n7,i4', 'KaTeX_Size1', 'KaTeX_Size2', 'KaTeX_Size3',
                    'KaTeX_Size4', 'KaTeX_Typewriter'],
                },
            };
            </script>
            <script defer src="https://cdn.jsdelivr.net/npm/webfontloader@1.6.28/webfontloader.js" integrity="sha256-4O4pS1SH31ZqrSO2A/2QJTVjTPqVe+jnYgOWUVr7EEc=" crossorigin="anonymous"></script>
        """)
    }
}

fun BODY.mermaidScript() {
    // Simple full example, how to include Mermaid: https://mermaid.js.org/config/usage.html#simple-full-example
    script(type = "module") {
        unsafe {
            raw("import mermaid from 'https://cdn.jsdelivr.net/npm/mermaid@10/dist/mermaid.esm.min.mjs';")
        }
    }
}