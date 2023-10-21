let pz = undefined;

function resetPz() {
  if (pz) {
    pz.resize();
    pz.center();
    pz.reset();
  }
}

function openModal(id, svgId) {
  document.getElementById(id).classList.add('is-active')

  const svgElement = document.getElementById(svgId).firstElementChild;
  svgElement.classList.add('modal-svg')

  pz = svgPanZoom(svgElement, {
    zoomEnabled: true,
    controlIconsEnabled: true,
    fit: true,
    center: true,
    minZoom: 1,
    maxZoom: 5
  });
  resetPz();

  // Reset position on window resize
  window.addEventListener('resize', resetPz);
}

function closeModal(id) {
  if (pz) {
    pz.destroy();
  }
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
