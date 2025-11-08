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

  console.log(coleccion.fuente)

  modal.querySelector('.titulo').textContent = coleccion.titulo
  modal.querySelector('.descripcion').textContent = coleccion.descripcion

  modal.querySelector('.fuente').textContent = coleccion.fuente
  modal.querySelector('.fuente').value = coleccion.fuente

  const form = modal.querySelector('.form.editar.coleccion');
  form.action = "/colecciones/" + id;

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
}