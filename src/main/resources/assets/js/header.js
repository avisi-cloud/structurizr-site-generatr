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
    
    function toggleMenu() {
      // Toggle the is-active class on the burger, menu, and overlay
      navbarBurger.classList.toggle('is-active');
      siteMenu.classList.toggle('is-active');
      mobileOverlay.classList.toggle('is-active');
      
      // Update aria-expanded attribute
      const isExpanded = navbarBurger.classList.contains('is-active');
      navbarBurger.setAttribute('aria-expanded', isExpanded.toString());
    }
    
    function closeMenu() {
      navbarBurger.classList.remove('is-active');
      siteMenu.classList.remove('is-active');
      mobileOverlay.classList.remove('is-active');
      navbarBurger.setAttribute('aria-expanded', 'false');
    }
    
    // Add click event listener to the burger
    navbarBurger.addEventListener('click', toggleMenu);
    
    // Close menu when clicking on the overlay
    mobileOverlay.addEventListener('click', closeMenu);
    
    // Close menu on window resize if switching to desktop view
    window.addEventListener('resize', () => {
      if (window.innerWidth > 1023) {
        closeMenu();
      }
    });
  }
});
