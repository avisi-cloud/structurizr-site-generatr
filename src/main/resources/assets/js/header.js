function redirect(event, value, href) {
  if (event.key === 'Enter' && value.length >= 1) window.location.href = href + '?q=' + value;
}

// Mobile menu toggle functionality
document.addEventListener('DOMContentLoaded', () => {
  // Get the navbar burger, menu, and overlay elements
  const navbarBurger = document.querySelector('.navbar-burger');
  const siteMenu = document.getElementById('site-menu');
  const mobileOverlay = document.getElementById('mobile-menu-overlay');
  
  if (navbarBurger && siteMenu && mobileOverlay) {

    function setMenu(open) {
      navbarBurger.classList.toggle('is-active', open);
      siteMenu.classList.toggle('is-active', open);
      mobileOverlay.classList.toggle('is-active', open);
      navbarBurger.setAttribute('aria-expanded', open.toString());
      // Lock background scrolling while the drawer is open
      document.documentElement.style.overflow = open ? 'hidden' : '';
    }

    function toggleMenu() {
      setMenu(!siteMenu.classList.contains('is-active'));
    }

    // Add click event listener to the burger
    navbarBurger.addEventListener('click', toggleMenu);

    // Close menu when clicking on the overlay
    mobileOverlay.addEventListener('click', () => setMenu(false));

    // Close menu with the Escape key
    document.addEventListener('keydown', (event) => {
      if (event.key === 'Escape' && siteMenu.classList.contains('is-active')) {
        setMenu(false);
        navbarBurger.focus();
      }
    });

    // Close menu when switching to desktop view
    const desktopQuery = window.matchMedia('(min-width: 1024px)');
    desktopQuery.addEventListener('change', (event) => {
      if (event.matches) setMenu(false);
    });
  }
});
