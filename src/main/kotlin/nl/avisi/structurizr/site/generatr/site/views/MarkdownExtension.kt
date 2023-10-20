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
            <link rel="stylesheet" href="${CDN.katexCss()}" crossorigin="anonymous">
        """)
    }
}

fun HEAD.katexScript() {
    // loading KaTeX as global on a webpage: https://katex.org/docs/browser.html#loading-as-global
    unsafe {
        raw("""
            <script defer src="${CDN.katexJs()}" crossorigin="anonymous"></script>
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
            <script defer src="${CDN.webfontloaderJs()}" crossorigin="anonymous"></script>
        """)
    }
}

fun BODY.mermaidScript(viewModel: PageViewModel) {
    // Fix to support mermaid diagrams in markdown:
    script(
        type = ScriptType.textJavaScript,
        src = "../" + "/reformat-mermaid.js".asUrlToFile(viewModel.url)
    ) { }
    // Simple full example, how to include Mermaid: https://mermaid.js.org/config/usage.html#simple-full-example
    script(type = "module") {
        unsafe {
            raw("import mermaid from '${CDN.mermaidJs()}';")
        }
    }
}
