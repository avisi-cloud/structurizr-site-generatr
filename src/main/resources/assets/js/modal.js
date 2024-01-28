function openModal(id) {
  document.getElementById(id).classList.add('is-active');
}

function closeModal(id) {
  window.removeEventListener('resize', resetPz);
  document.getElementById(id).classList.remove('is-active');
}

// Add a keyboard event to close all modals
document.addEventListener('keydown', (event) => {
  if (event.code === 'Escape') {
    (document.querySelectorAll('.modal') || []).forEach((modal) => {
      if (modal.classList.contains('is-active')) {
        closeModal(modal.id);
      }
    });
  }
});
