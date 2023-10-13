function listree() {

  const subMenuHeadingClass = "listree-submenu-heading";
  const expandedClass = "expanded";
  const collapsedClass = "collapsed";
  const activeClass = "is-active";
  const subMenuHeadings = document.getElementsByClassName(subMenuHeadingClass);

  Array
    .from(subMenuHeadings)
    .forEach(function(subMenuHeading) {
      // Collapse all the subMenuHeadings while searching for is-active class
      let foundActive = false;
      subMenuHeading.classList.add(collapsedClass);
      subMenuHeading.nextElementSibling.style.display = "none";

      // Check if this sub-menu heading is active
      const liElements = subMenuHeading.nextElementSibling.children;
      for (let i = 0; i < liElements.length; i++) {
        if (liElements[i].hasChildNodes()) {
          if (liElements[i].children[0].tagName === "A") {
            if (liElements[i].children[0].classList.contains(activeClass)) {
              foundActive = true;
              break;
            }
          }
        }
      }

      // Expand all parent sub-menus until root menu is reached
      if (foundActive) {
        subMenuHeading.classList.remove(collapsedClass);
        subMenuHeading.classList.add(expandedClass);
        subMenuHeading.nextElementSibling.style.display = "block";

        let currentSubMenu = subMenuHeading.parentElement;
        while (currentSubMenu !== null && !currentSubMenu.classList.contains("listree")) {
          if(currentSubMenu.tagName === "UL"){
            if(currentSubMenu.previousElementSibling != null) {
              currentSubMenu.previousElementSibling.classList.remove(collapsedClass);
              currentSubMenu.previousElementSibling.classList.add(expandedClass);
              currentSubMenu.style.display = "block";
            }
          }
          currentSubMenu = currentSubMenu.parentElement
        }
      }

      // Add the eventlistener to all subMenuHeadings
      subMenuHeading.addEventListener("click", function(event) {
        event.preventDefault();
        const subMenuList = event.target.nextElementSibling;
        if (subMenuList.style.display === "none") {
          subMenuHeading.classList.remove(collapsedClass);
          subMenuHeading.classList.add(expandedClass);
          subMenuHeading.nextElementSibling.style.display = "block";
        } else {
          subMenuHeading.classList.remove(expandedClass);
          subMenuHeading.classList.add(collapsedClass);
          subMenuHeading.nextElementSibling.style.display = "none";
        }
        event.stopPropagation();
      });
    });
}
