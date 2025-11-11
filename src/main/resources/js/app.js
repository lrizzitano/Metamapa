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

  const coleccion = JSON.parse(boton.dataset.coleccion);
  const id = coleccion.id

  modal.querySelector('.titulo').textContent = coleccion.titulo
  modal.querySelector('.descripcion').textContent = coleccion.descripcion

  modal.querySelector('.criterioPertenencia').textContent = coleccion.criterioPertenencia

  const form = modal.querySelector('.form.editar.coleccion');
  form.action = `/colecciones/${encodeURIComponent(coleccion.id)}`;

  const deleteButton = modal.querySelector('.boton.eliminar');
  deleteButton.setAttribute('hx-delete', `/colecciones/${encodeURIComponent(coleccion.id)}`);
  htmx.process(deleteButton);


}

function crearModalDetalleHecho(boton) {
  openModal('detalleHecho');
  const modal = document.getElementById('detalleHecho');
  const hecho = JSON.parse(boton.dataset.hecho);

  modal.querySelector('.titulo').textContent = hecho.titulo;
  modal.querySelector('.descripcion').textContent = hecho.descripcion;
  modal.querySelector('.multimedia').src = hecho.multimedia;
  modal.querySelector('.fechaAcontecimiento').textContent = hecho.fechaAcontecimiento;
  modal.querySelector('.fechaCarga').textContent = hecho.fechaCarga;
  modal.querySelector('.categoria').textContent = hecho.categoria;
  modal.querySelector('.contribuyente').textContent = hecho.contribuyente;
  modal.querySelector('.provincia').textContent = hecho.provincia;

  modal.querySelector('.boton-solicitar-eliminacion')
    .href = "/solicitudesDeEliminacion/nueva?hecho=" + encodeURIComponent(hecho.titulo);
}

function actualizarRutaFormFiltros(id) {
  const form = document.querySelector('.form-filtros');
  form.setAttribute('hx-get', `/colecciones/${id}/hechos`);
  document.getElementById('btn-filtro').disabled = false;

  for (const boton of document.getElementsByClassName('boton')){
    boton.classList.remove('activo');
  }
  document.getElementById(`boton-${id}`).classList.add('activo');
}