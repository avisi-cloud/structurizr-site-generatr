// solution from https://css-tricks.com/making-mermaid-diagrams-in-markdown/

// select <pre class="mermaid"> _and_ <pre><code class="language-mermaid">
document.querySelectorAll("pre.mermaid, pre>code.language-mermaid, div.mermaid").forEach($el => {
    // if the second selector got a hit, reference the parent <pre>
    if ($el.tagName === "CODE")
      $el = $el.parentElement
    // put the Mermaid contents in the expected <div class="mermaid">
    // plus keep the original contents in a nice <details>
    $el.outerHTML = `
      <div class="mermaid">${$el.textContent}</div>
      <details>
        <summary>Diagram source</summary>
        <pre>${$el.textContent}</pre>
      </details>
    `
  })