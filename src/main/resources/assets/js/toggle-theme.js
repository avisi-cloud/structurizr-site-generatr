if (!localStorage.getItem("data-theme")) {
  const prefersDarkMode = window.matchMedia &&
    window.matchMedia('(prefers-color-scheme: dark)').matches;

  localStorage.setItem("data-theme", prefersDarkMode ? "dark" : "light");
}

document.documentElement.setAttribute("data-theme", localStorage.getItem("data-theme"));

function toggleTheme() {
  if (localStorage.getItem("data-theme") === "light") {
    document.documentElement.setAttribute("data-theme", "dark");
    localStorage.setItem("data-theme", "dark");
  } else {
    document.documentElement.setAttribute("data-theme", "light");
    localStorage.setItem("data-theme", "light");
  }
}
