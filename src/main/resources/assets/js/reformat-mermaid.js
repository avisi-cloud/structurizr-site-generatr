// Put the Mermaid contents in the expected <div class="mermaid">, and keep the
// original source in a <details> block.
//
// Adapted from https://css-tricks.com/making-mermaid-diagrams-in-markdown/, but
// deliberately NOT via `outerHTML`: assigning the diagram text into an HTML string
// makes the browser re-parse it as HTML. Angle-bracket placeholders that are common
// in sequence diagrams (`<pub>`, `<IP>`, `<tempfile>`, ...) then become real DOM
// elements, Mermaid receives mangled text and renders "Syntax error in text".
// Building the nodes and assigning via textContent keeps the diagram source intact.
document.querySelectorAll("pre.mermaid, pre>code.language-mermaid, div.mermaid").forEach($el => {
  // if the second selector got a hit, reference the parent <pre>
  if ($el.tagName === "CODE")
    $el = $el.parentElement

  const source = $el.textContent

  const diagram = document.createElement("div")
  diagram.className = "mermaid"
  diagram.textContent = source

  const details = document.createElement("details")
  const summary = document.createElement("summary")
  summary.textContent = "Diagram source"
  const pre = document.createElement("pre")
  pre.textContent = source
  details.append(summary, pre)

  $el.replaceWith(diagram, details)
})
