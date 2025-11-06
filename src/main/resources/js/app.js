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

function crearModalDetalleHecho(boton){
  openModal('detalleHecho');
  const modal = document.getElementById('detalleHecho');


  modal.querySelector('.titulo').textContent = boton.dataset.titulo;
  modal.querySelector('.descripcion').textContent = boton.dataset.descripcion;
  modal.querySelector('.multimedia').src = boton.dataset.multimedia;
  modal.querySelector('.fechaAcontecimiento').textContent = boton.dataset.fechaAcontecimiento;
  modal.querySelector('.fechaCarga').textContent = boton.dataset.fechaCarga;
  modal.querySelector('.categoria').textContent= boton.dataset.categoria;
  modal.querySelector('.contribuyente').textContent= boton.dataset.contribuyente;
  modal.querySelector('.provincia').textContent =boton.dataset.provincia;
  modal.querySelector('.boton-solicitar-eliminacion').href="/solicitudesDeEliminacion/nueva?hecho=" + boton.dataset.titulo;

}