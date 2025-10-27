function openModal(id) {
  document.getElementById(id).classList.add('active');
}
function closeModal(id) {
  document.getElementById(id).classList.remove('active')
}
function extractID(element) {
  const modal = element.closest('.modal'); // busca el ancestro con clase "modal"
  return modal ? modal.id : null;
}
function switchModal(element, nextModalID) {
  const currentID = extractID(element);
  if (currentID) closeModal(element);
  openModal(nextModalID);
}