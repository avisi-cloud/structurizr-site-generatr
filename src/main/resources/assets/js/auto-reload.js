let hash = undefined;

function reloadIfNeeded() {
    fetch(window.location + "index.html.md5")
        .then((response) => {
            if (!response.ok) {
                throw Error(response.statusText.toLowerCase())
            }
            return response.text()
        })
        .then((content) => {
            if (!hash) {
                hash = content;
            } else if (hash !== content) {
                console.log("Page content change detected, reloading ...")
                location.reload()
            } else {
                console.log("Page content has not been changed.")
            }
        })
        .catch((error) => {
            if (error.message === "not found") {
                location.href = location.origin
            } else {
                console.log(error)
            }
        });
}

setInterval(reloadIfNeeded, 200);
