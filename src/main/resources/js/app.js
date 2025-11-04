function openModal(id) {
  document.getElementById(id).classList.add('active');
}
function closeModal(id) {
  document.getElementById(id).classList.remove('active')
}

function switchModal(currentModalID, nextModalID) {
  if (currentModalID) closeModal(currentModalID);
  openModal(nextModalID);
}

function crearModalColeccion(boton) {
  openModal('modificarColeccion')
  const modal = document.getElementById('modificarColeccion')

  const titulo = boton.dataset.titulo;
  const descripcion = boton.dataset.descripcion;

  modal.querySelector('.titulo').textContent = titulo
  modal.querySelector('.descripcion').textContent = descripcion

}