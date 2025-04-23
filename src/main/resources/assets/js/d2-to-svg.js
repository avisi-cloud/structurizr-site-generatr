document.addEventListener("DOMContentLoaded", async () => {
    const divElements = document.querySelectorAll("pre");
    let d2 = new window.D2();
    for (const div of divElements) {
        const d2Text = div.getAttribute("page");
        try {
            const compiled = await d2.compile(d2Text, {
                layout: "dagre",
                sketch: false,
                themeId: null,
                darkThemeId: null,
                scale: 1,
                pad: 50,
                center: true,
                forceAppendix: null,
                target: null,
                animateInterval: null,
                salt: null,
                fontRegular: null,
                fontItalic: null,
                fontSemibold: null,
                fontBold: null,
                noXmlTag: true,
            });
            const svgString = await d2.render(compiled.diagram, compiled.renderOptions);

            const parser = new DOMParser();
            const svgDoc = parser.parseFromString(svgString, "image/svg+xml");
            const svg = svgDoc.documentElement;

            svg.setAttribute("width", "100%");
            svg.setAttribute("height", "auto");
            svg.style.maxWidth = "100%";
            svg.style.height = "auto";
            div.innerHTML = "";
            div.appendChild(svg);
            div.parentElement.style.maxWidth = "800px";
        } catch (err) {
            console.error("D2 rendering failed:", err);
            pre.innerHTML = `<span style="color: red;">Failed to render D2 diagram: ${err.message}</span>`;
        }
    }
});
