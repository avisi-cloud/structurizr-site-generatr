let pz = undefined;

function resetPz() {
  if (pz) {
    pz.resize();
    pz.center();
    pz.reset();
  }
}

function openSvgModal(id, svgId) {
  openModal(id);

  const svgElement = document.getElementById(svgId).firstElementChild;
  svgElement.classList.add('modal-svg');

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

function closeSvgModal(id) {
  if (pz) {
    pz.destroy();
  }
  closeModal(id);
}
