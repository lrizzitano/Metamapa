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

  const form = modal.querySelector('form.form.editar.coleccion');
  form.setAttribute('hx-post', `/colecciones/${encodeURIComponent(coleccion.id)}`);
  htmx.process(form);

  const deleteButton = modal.querySelector('.boton.eliminar');
  deleteButton.setAttribute('hx-delete', `/colecciones/${encodeURIComponent(coleccion.id)}`);
  htmx.process(deleteButton);


}

function crearModalDetalleHecho(boton) {
  openModal('detalleHecho');
  const modal = document.getElementById('detalleHecho');
  const hecho = JSON.parse(boton.dataset.hecho);

  const slideVideo = document.querySelector('.slide-video');
  // probe ocultando el div de controles pero parece que el display:flex tiene prioridad sobre el hidden
  const controles = document.querySelectorAll('.controles a');
  const iframe = document.querySelector('.multimedia-video');

  if (hecho.video) { // la unica que requiere if pq es opcional
    iframe.src = hecho.video;
    slideVideo.hidden = false;
    controles.forEach(control => control.hidden = false);
  } else {
    slideVideo.hidden = true; // lo escondo por si habia uno puesto de antes
    controles.forEach(control => control.hidden = true);
  }

  modal.querySelector('.titulo').textContent = hecho.titulo;
  modal.querySelector('.descripcion').textContent = hecho.descripcion;
  modal.querySelector('.multimedia-imagen').src = hecho.imagen;
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