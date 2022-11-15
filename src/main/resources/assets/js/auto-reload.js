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
        } else if (event.data === "site-updated") {
            console.log("Site update detected, detect page content change ...")
            reloadIfNeeded();
        } else {
            console.log(event.data)
        }
    }
}

connectToWs();
