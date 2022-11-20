let hash = undefined;

function connectToWs() {
    const ws = new WebSocket("ws://" + location.host + "/_events");
    ws.onopen = () => {
        console.log("Connected to socket.");
        reloadIfNeeded();
    }
    ws.onclose = (event) => {
        console.log('Socket has been closed. Attempting to reconnect ...', event.reason);
        setTimeout(() => connectToWs(), 1000);
    };
    ws.onmessage = (event) => {
        if (event.data === "site-updating") {
            console.log("Site updating ...")
            showUpdatingSite()
        } else if (event.data === "site-updated") {
            console.log("Site update detected, detect page content change ...")
            hideUpdatingSite()
            hideUpdateSiteError()
            showSite()
            reloadIfNeeded();
        } else {
            console.log(event.data)
            hideUpdatingSite()
            hideSite()
            showUpdateSiteError(event.data)
        }
    }
}

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

function showUpdatingSite() {
    document.getElementById("updating-site").classList.remove("is-hidden")
    document.getElementById("updating-site").attributes.removeNamedItem("value")
}

function hideUpdatingSite() {
    document.getElementById("updating-site").classList.add("is-hidden")
    document.getElementById("updating-site").value = "0"
}

function showUpdateSiteError(content) {
    document.getElementById("update-site-error-message").innerText = content
    document.getElementById("update-site-error").classList.remove("is-hidden")
}

function hideUpdateSiteError() {
    document.getElementById("update-site-error-message").innerText = ""
    document.getElementById("update-site-error").classList.add("is-hidden")
}

function showSite() {
    document.getElementById("site").classList.remove("is-hidden");
}

function hideSite() {
    document.getElementById("site").classList.add("is-hidden");
}

connectToWs();
