// Script taken from flexmark wiki: https://github.com/vsch/flexmark-java/wiki/Extensions#gitlab-flavoured-markdown
(function () {
    document.addEventListener("DOMContentLoaded", function () {
        var mathElems = document.getElementsByClassName("katex");
        var elems = [];
        for (const i in mathElems) {
            if (mathElems.hasOwnProperty(i)) elems.push(mathElems[i]);
        }

        elems.forEach(elem => {
            katex.render(elem.textContent, elem, { throwOnError: false, displayMode: elem.nodeName !== 'SPAN', });
        });
    });
})();

